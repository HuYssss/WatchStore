package hcmute.edu.watchstore.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController extends ControllerBase{
    
    @Autowired
    private CartService cartService;

    // thêm sản phẩm vào giỏ hàng
    @PostMapping("/addProductToCart")
    public ResponseEntity<?> addProductToCart(@RequestBody ProductItem productItem, Principal principal) {
        return this.cartService.addProductToCart(productItem, findIdByUsername(principal.getName()));
    }

    // lấy toàn bộ sản phẩm có trong giỏ hàng của user
    @GetMapping("")
    public ResponseEntity<?> findCartByUser(Principal principal) {
        return this.cartService.findCartByUser(findIdByUsername(principal.getName()));
    }

    // xóa sản phẩm có trong giỏ hàng
    @PostMapping("/deleteProduct")
    public ResponseEntity<?> deleteProductInCart(@RequestBody ProductItem productItem, Principal principal) {
        productItem.setQuantity(0);
        return this.cartService.deleteProductInCart(productItem, findIdByUsername(principal.getName()));
    }
}
