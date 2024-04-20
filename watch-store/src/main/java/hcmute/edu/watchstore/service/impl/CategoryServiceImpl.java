package hcmute.edu.watchstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.dto.response.CategoryResponse;
import hcmute.edu.watchstore.entity.Category;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.repository.CategoryRepository;
import hcmute.edu.watchstore.service.CategoryService;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class CategoryServiceImpl extends ServiceBase implements CategoryService  {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

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
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        return category.orElse(null);
    }

    @Override
    public ResponseEntity<?> getCategoryResp(ObjectId categoryId) {
        Category category = findCategory(categoryId);

        if (category == null) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        List<Product> products = new ArrayList<>();
        for (ObjectId id : category.getProduct()) {
            Product product = this.productService.findProduct(id);
            if (product != null) 
                products.add(product);
        }

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoryId);
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setProduct(products);

        return success(categoryResponse);
    }
    
}
