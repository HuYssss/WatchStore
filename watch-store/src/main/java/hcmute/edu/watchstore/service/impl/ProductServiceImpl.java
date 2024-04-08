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
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.repository.ProductRepository;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class ProductServiceImpl extends ServiceBase implements ProductService{

    @Autowired
    private ProductRepository productRepository;

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
    @SuppressWarnings("null")
    public Product findProduct(ObjectId id) {
        Optional<Product> product = this.productRepository.findById(id);
        return product.orElse(null);
    }

    @Override
    public ObjectId saveOrUpdate(Product product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveOrUpdate'");
    }

    @Override
    public boolean delete(ObjectId objectId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public ResponseEntity<?> findAll() {
        return success(this.productRepository.findAll());
    }

    @Override
    public long countAll() {
        return this.productRepository.count();
    }
}
