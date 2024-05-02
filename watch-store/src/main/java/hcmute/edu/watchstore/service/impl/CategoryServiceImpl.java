package hcmute.edu.watchstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

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
        if (category.getId() == null) 
            category.setId(new ObjectId());

        if (category.getProduct() == null) 
            category.setProduct(new ArrayList<>());

        try {
            this.categoryRepository.save(category);
            return category.getId();
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public boolean delete(ObjectId categoryId) {
        try {
            this.categoryRepository.deleteById(categoryId);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    @Override
    public ResponseEntity<?> createCategory(Category category) {
        return (saveOrUpdate(category) != null) 
            ? success("Create new category success !!!") 
            : error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
    }

    @Override
    public ResponseEntity<?> deleteCategory(ObjectId categoryId) {
        Category category = findCategory(categoryId);
        if(delete(categoryId)) {
            handleDeleteCategory(category.getProduct());
            return success("Delete category success !!!");
        }
        return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
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

    public void handleDeleteCategory(List<ObjectId> productList) {
        if (!productList.isEmpty()) {
            List<Product> allProduct = this.productService.findAll();
            for (ObjectId id : productList) {
                Product p = findProduct(id, allProduct);
                p.setCategory(new ObjectId("662a058d43948d98f91010b8"));
                this.productService.saveOrUpdate(p);
            }

            Category category = findCategory(new ObjectId("662a058d43948d98f91010b8"));
            List<ObjectId> products = category.getProduct();
            products.addAll(productList);
            this.categoryRepository.save(category);
        }
    }

    public Product findProduct(ObjectId id, List<Product> products) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ResponseEntity<?> findAll() {
        return success(this.categoryRepository.findAll());
    }
}
