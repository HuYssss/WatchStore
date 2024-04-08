package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Product;

public interface ProductService {
    ResponseEntity<?> findProductById(ObjectId productId);
    ResponseEntity<?> findProductByKeyword(String keyword);
    Product findProduct(ObjectId id);
    ObjectId saveOrUpdate(Product product);
    boolean delete(ObjectId objectId);
    ResponseEntity<?> findAll();
    long countAll();
}
