package hcmute.edu.watchstore.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.dto.request.BuyNowRequest;
import hcmute.edu.watchstore.dto.request.OrderRequest;
import hcmute.edu.watchstore.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/order")
public class OrderController extends ControllerBase{

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<?> findOrderByUser(Principal principal) {
        return this.orderService.getOrderUser(findIdByUsername(principal.getName()));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderReq, Principal principal) throws UnsupportedEncodingException {
        return this.orderService.createOrder(orderReq, findIdByUsername(principal.getName()));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable ObjectId orderId, Principal principal) {
        return this.orderService.cancelOrderr(orderId, findIdByUsername(principal.getName()));
    }

    @PostMapping("/delivered/{orderId}")
    public ResponseEntity<?> orderIsDelivered(@PathVariable ObjectId orderId, Principal principal) {
        return this.orderService.isOrderDelivered(orderId, findIdByUsername(principal.getName()));
    }

    @PostMapping("/buyNow")
    public ResponseEntity<?> buyNow(@RequestBody BuyNowRequest buyNowRequest, Principal principal) throws UnsupportedEncodingException {   
        return this.orderService.buyNow(buyNowRequest, findIdByUsername(principal.getName()));
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllOrder() {
        return this.orderService.getAll();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approvalOrder")
    public ResponseEntity<?> approvalOrder(@RequestParam ObjectId orderId) {
        return this.orderService.approvalOrder(orderId);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cancelOrder")
    public ResponseEntity<?> cancelOrderAdmin(@RequestParam ObjectId orderId) {
        return this.orderService.setStateOrder(orderId, "cancel");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orderDetail/{orderId}")
    public ResponseEntity<?> orderDetail(@PathVariable ObjectId orderId) {
        return this.orderService.getOrderDetail(orderId);
    }
}
