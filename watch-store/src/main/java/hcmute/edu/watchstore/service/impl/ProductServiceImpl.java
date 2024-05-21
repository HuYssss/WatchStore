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
import hcmute.edu.watchstore.dto.response.ProductResponse;
import hcmute.edu.watchstore.entity.Category;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.repository.CategoryRepository;
import hcmute.edu.watchstore.repository.ProductRepository;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class ProductServiceImpl extends ServiceBase implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> findProductById(ObjectId productId) {
        Product product = findProduct(productId);

        if (product != null) 
            return success(product);
        else
            return error(ResponseCode.PRODUCT_NOT_FOUND.getCode(), ResponseCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Override
    public ResponseEntity<?> findProductByKeyword(String keyword) {
        List<Product> products = this.productRepository.findAll();

        List<Product> result = new ArrayList<>();

        for (Product p : products)
            if (p.getProductName().contains(keyword))
                result.add(p);

        if (!result.isEmpty()) 
            return success(result);
        else
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
    }

    @Override
    public Product findProduct(ObjectId id) {
        Optional<Product> product = this.productRepository.findById(id);
        return product.orElse(null);
    }

    public ObjectId saveOrUpdate(Product product) {

        if (product.getId() == null) 
            product.setId(new ObjectId());
        
        try {
            this.productRepository.save(product);
            return product.getId();
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> delete(ObjectId objectId) {
        Optional<Product> product = this.productRepository.findById(objectId);
        if (product.isPresent()) {
            this.productRepository.deleteById(objectId);
            handleManageProduct(objectId, product.get().getCategory(), "delete");
            return success("Delete product success !!!");
        }
        return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
    }

    @Override
    public ResponseEntity<?> createOrUpdateProduct(Product product) {
        if (product.getCategory() == null) 
            product.setCategory(new ObjectId("662a058d43948d98f91010b8"));

        if (product.getState() == null)
            product.setState("saling");

        ObjectId productId = saveOrUpdate(product);
        if (productId != null) {
            handleManageProduct(productId, product.getCategory(), "add");
            return success("Create new product success !!!");
        }
        return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
    }

    @Override
    public ResponseEntity<?> addProductToCategory(ObjectId productId, ObjectId categoryId) {
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isPresent()) {
            if (product.get().getCategory() != null) {
                Category currentCategory = this.categoryRepository.findById(product.get().getCategory()).orElse(null);
                if (currentCategory != null) {
                    List<ObjectId> products = currentCategory.getProduct();
                    products.remove(productId);
                    currentCategory.setProduct(products);
                    this.categoryRepository.save(currentCategory);
                } 
                else
                    return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
            }

            try {
                product.get().setCategory(categoryId);
                saveOrUpdate(product.get());
                handleManageProduct(productId, categoryId, "add");
                return success("Add product to category success !!!");
            } catch (MongoException e) {
                return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
            }
        }
        return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
    }

    @Override
    public ResponseEntity<?> getAll() {
        return success(findAll());
    }

    @Override
    public long countAll() {
        return this.productRepository.count();
    }

    @Override
    public List<ProductResponse> findAll() {
        List<Product> list = this.productRepository.findAll();
        if (!list.isEmpty()) {
            List<ProductResponse> responses = new ArrayList<>();
            for(Product p : list) {
                ProductResponse resp = new ProductResponse(p);
                responses.add(resp);
            }
            return responses;
        }
        return null;
    }

    public void handleManageProduct(ObjectId productId, ObjectId categoryId, String message) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        List<ObjectId> productList = category.get().getProduct();
        if (category != null) {

            if (message.equals("delete")) 
                productList.remove(productId);

            if (message.equals("add")) 
                productList.add(productId);
            
            try {
                category.get().setProduct(productList);
                this.categoryRepository.save(category.get());
            } catch (MongoException e) {
                throw new MongoException("Can't update category !!!");
            }
        }
    }

    @Override
    public List<Product> findAllNormal() {
        return this.productRepository.findAll();
    }

    @Override
    public boolean saveProductByList(List<Product> products) {
        try {
            this.productRepository.saveAll(products);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    public Product findProduct(ObjectId id, List<Product> products) {
        return products.stream()
                 .filter(product -> product.getId().equals(id))
                 .findFirst()
                 .orElse(null);
    }

}