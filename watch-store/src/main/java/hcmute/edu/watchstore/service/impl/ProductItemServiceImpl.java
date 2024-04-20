package hcmute.edu.watchstore.service.impl;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.repository.ProductItemRepository;
import hcmute.edu.watchstore.service.ProductItemService;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class ProductItemServiceImpl extends ServiceBase implements ProductItemService {

    @Autowired
    private ProductItemRepository productItemRepository;
    
    @Autowired
    private ProductService productService;

    @Override
    public ObjectId saveOrEditItem(ProductItem pItem) {
        
        if (pItem.getId() == null) {
            ObjectId itemId = new ObjectId();
            pItem.setId(itemId);
        }

        try {
            this.productItemRepository.save(pItem);
            return pItem.getId();
        } catch(Exception e) {
            return null;
        }

    }

    @Override
    public ProductItem findProductItem(ObjectId itemId) {
        Optional<ProductItem> item = this.productItemRepository.findById(itemId);
        return item.orElse(null);
    }

    @Override
    public ProductItemResponse findProductItemResponse(ObjectId itemId) {
        ProductItem item = findProductItem(itemId);
        Product product = this.productService.findProduct(item.getProduct());
        return new ProductItemResponse(item.getId(), product, item.getQuantity());
    }

    @Override
    public boolean deleteItem(ObjectId itemId) {
        try {
            this.productItemRepository.deleteById(itemId);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }
    
}
