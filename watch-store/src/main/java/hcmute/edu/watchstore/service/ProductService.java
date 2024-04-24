package hcmute.edu.watchstore.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Product;

public interface ProductService {
    ResponseEntity<?> findProductById(ObjectId productId);
    ResponseEntity<?> findProductByKeyword(String keyword);
    Product findProduct(ObjectId id);
    ObjectId saveOrUpdate(Product product);
    boolean delete(ObjectId objectId);
    ResponseEntity<?> getAll();
    List<Product> findAll();
    long countAll();
}
