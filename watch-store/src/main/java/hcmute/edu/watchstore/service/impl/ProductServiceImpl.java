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
import hcmute.edu.watchstore.dto.response.ProductResponse;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.entity.VariantProduct;
import hcmute.edu.watchstore.repository.ProductRepository;
import hcmute.edu.watchstore.repository.VariantProductRepository;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class ProductServiceImpl extends ServiceBase implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantProductRepository variantProductRepository;

    @Override
    public ResponseEntity<?> findProductById(ObjectId productId) {

        if (productId == null) {
            return error(ResponseCode.PRODUCT_NOT_FOUND.getCode(), ResponseCode.PRODUCT_NOT_FOUND.getMessage());
        }

        Optional<Product> product = this.productRepository.findById(productId);

        if (product.isPresent()) {
            ProductResponse productResponse = new ProductResponse(product.get());

            List<VariantProduct> variantProducts = new ArrayList<>();

            for (ObjectId vPId : product.get().getVariantProducts()) {
                if (vPId != null) {
                    Optional<VariantProduct> vPS = this.variantProductRepository.findById(vPId);
                    if (vPS.isPresent()) 
                        variantProducts.add(vPS.get());
                }
            }

            productResponse.setVariantProducts(variantProducts);
            return success(productResponse);

        }
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
    
}
