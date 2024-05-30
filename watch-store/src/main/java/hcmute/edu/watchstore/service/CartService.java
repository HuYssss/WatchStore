package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.ProductItem;

public interface CartService {
    ResponseEntity<?> addProductToCart(ProductItem productItem, ObjectId userId);
    ResponseEntity<?> findCartByUser(ObjectId userId);
    ResponseEntity<?> editProductInCart(ProductItem productItem, ObjectId userId);
    ResponseEntity<?> deleteProductInCart(ProductItem productItem, ObjectId userId);
    boolean deleteCart(ObjectId cartId);
    ObjectId saveCart(Cart cart);
}
