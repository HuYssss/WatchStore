package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.ProductItem;

public interface CartService {
    ResponseEntity<?> addProductToCart(ProductItem productIt, ObjectId userId);
    ResponseEntity<?> findCartByUser(ObjectId userId);
}
