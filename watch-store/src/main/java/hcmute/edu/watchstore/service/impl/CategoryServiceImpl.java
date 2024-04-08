package hcmute.edu.watchstore.service.impl;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.dto.response.CategoryResponse;
import hcmute.edu.watchstore.entity.Category;
import hcmute.edu.watchstore.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Override
    public ObjectId saveOrUpdate(Category category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveOrUpdate'");
    }

    @Override
    public boolean delete(ObjectId categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Category findCategory(ObjectId categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findCategory'");
    }

    @Override
    public CategoryResponse getCategoryResp(ObjectId categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryResp'");
    }
    
}
