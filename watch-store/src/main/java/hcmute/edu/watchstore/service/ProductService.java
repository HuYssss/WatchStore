package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<?> findProductById(ObjectId productId);
    ResponseEntity<?> findProductByKeyword(String keyword);
}
