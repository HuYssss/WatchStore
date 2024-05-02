package hcmute.edu.watchstore.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Product;

public interface ProductService {
    ObjectId saveOrUpdate(Product product);
    Product findProduct(ObjectId id);
    List<Product> findAll();
    long countAll();
    ResponseEntity<?> findProductById(ObjectId productId);
    ResponseEntity<?> findProductByKeyword(String keyword);
    ResponseEntity<?> addProductToCategory(ObjectId productId, ObjectId categoryId);
    ResponseEntity<?> createOrUpdateProduct(Product product);
    ResponseEntity<?> delete(ObjectId objectId);
    ResponseEntity<?> getAll();
}
