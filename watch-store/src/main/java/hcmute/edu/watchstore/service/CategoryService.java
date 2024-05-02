package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Category;

public interface CategoryService {
    ResponseEntity<?> createCategory(Category category);
    ResponseEntity<?> deleteCategory(ObjectId categoryId);
    ResponseEntity<?> findAll();
    ObjectId saveOrUpdate(Category category);
    boolean delete(ObjectId categoryId);
    Category findCategory(ObjectId categoryId);
    ResponseEntity<?> getCategoryResp(ObjectId categoryId);
}
