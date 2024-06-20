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
    long countAll();
    ResponseEntity<?> findProductById(ObjectId productId);
    ResponseEntity<?> findProductByKeyword(String keyword);
    ResponseEntity<?> addProductToCategory(ObjectId productId, ObjectId categoryId);
    ResponseEntity<?> removeProductFromCategory(ObjectId productId);
    ResponseEntity<?> createOrUpdateProduct(Product product);
    ResponseEntity<?> delete(ObjectId objectId);
    ResponseEntity<?> getAll();
    ResponseEntity<?> getByOption(String option, String value, double from, double to);
    boolean saveProductByList(List<Product> products);
}
