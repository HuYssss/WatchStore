package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.dto.response.CategoryResponse;
import hcmute.edu.watchstore.entity.Category;

public interface CategoryService {
    ObjectId saveOrUpdate(Category category);
    boolean delete(ObjectId categoryId);
    Category findCategory(ObjectId categoryId);
    CategoryResponse getCategoryResp(ObjectId categoryId);
}
