package hcmute.edu.watchstore.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.ProductItem;

public interface CartService {
    ResponseEntity<?> addProductToCart(ProductItem productItem, ObjectId userId);
    ResponseEntity<?> findCartByUser(ObjectId userId);
    ResponseEntity<?> updateCart(List<ProductItemResponse> listResp, ObjectId userId);
    List<ObjectId> deleteCart(ObjectId cartId);
    ObjectId saveCart(Cart cart);
}
