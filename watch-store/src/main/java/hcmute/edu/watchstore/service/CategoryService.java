package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Category;

public interface CategoryService {
    ResponseEntity<?> findAll();
    Category findCategory(ObjectId categoryId);
    ResponseEntity<?> getCategoryResp(ObjectId categoryId);
}
