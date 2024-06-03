package hcmute.edu.watchstore.controller;

import java.security.Principal;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.dto.request.OrderRequest;
import hcmute.edu.watchstore.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/order")
public class OrderController extends ControllerBase{

    @Autowired
    private OrderService orderService;

    // lấy toàn bộ order của user
    @GetMapping("")
    public ResponseEntity<?> findOrderByUser(Principal principal) {
        return this.orderService.getOrderUser(findIdByUsername(principal.getName()));
    }

    // tạo một order mới
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderReq, Principal principal) {
        return this.orderService.createOrder(orderReq, findIdByUsername(principal.getName()));
    }

    // xóa order của user
    @PostMapping("/delete/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable ObjectId orderId, Principal principal) {
        return this.orderService.cancelOrderr(orderId, findIdByUsername(principal.getName()));
    }
    
    
}
