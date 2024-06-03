package hcmute.edu.watchstore.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.response.ProductResponse;
import hcmute.edu.watchstore.entity.Product;

public interface ProductService {
    ObjectId saveOrUpdate(Product product);
    Product findProduct(ObjectId id);
    List<ProductResponse> findAll();
    List<Product> findAllNormal();
    ResponseEntity<?> findProductById(ObjectId productId);
    ResponseEntity<?> getAll();
}
