package hcmute.edu.watchstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController extends ControllerBase{
    
    @Autowired
    private CartService cartService;

    @PostMapping("/addProductToCart")
    public ResponseEntity<?> addProductToCart(@RequestBody ProductItem productItem, Principal principal) {
        return this.cartService.addProductToCart(productItem, findIdByUsername(principal.getName()));
    }

    @GetMapping("")
    public ResponseEntity<?> findCartByUser(Principal principal) {
        return this.cartService.findCartByUser(findIdByUsername(principal.getName()));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCartUser(@RequestBody List<ProductItemResponse> cartReq ,Principal principal) {
        return this.cartService.updateCart(cartReq, findIdByUsername(principal.getName()));
    }
}
