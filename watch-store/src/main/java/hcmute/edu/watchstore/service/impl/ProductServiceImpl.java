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

    // tìm sản phẩm (hàm trả về cho FE)
    @Override
    public ResponseEntity<?> findProductById(ObjectId productId) {
        Product product = findProduct(productId);

        if (product != null) 
            return success(product);
        else
            return error(ResponseCode.PRODUCT_NOT_FOUND.getCode(), ResponseCode.PRODUCT_NOT_FOUND.getMessage());
    }

    // tìm dản phẩm (hàm hỗ trợ)
    @Override
    public Product findProduct(ObjectId id) {
        Optional<Product> product = this.productRepository.findById(id);
        return product.orElse(null);
    }

    // thêm hoặc xóa sản phẩm
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

    // lấy toàn bộ sản phẩm
    @Override
    public ResponseEntity<?> getAll() {
        return success(findAll());
    }

    // lấy danh sách toàn bộ sản phẩm và trả về List<ProductResponse> có id là String để FE dễ xử lí
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

    // handle khi xóa hoặc thêm sản phẩm
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

    // lấy danh sách toàn bộ sản phẩm trong database
    @Override
    public List<Product> findAllNormal() {
        return this.productRepository.findAll();
    }
}
