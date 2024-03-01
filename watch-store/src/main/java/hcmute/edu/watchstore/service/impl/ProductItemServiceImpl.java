package hcmute.edu.watchstore.service.impl;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.repository.ProductItemRepository;
import hcmute.edu.watchstore.service.ProductItemService;

@Service
public class ProductItemServiceImpl extends ServiceBase implements ProductItemService {

    @Autowired
    private ProductItemRepository productItemRepository;

    @Override
    public ObjectId createProductItem(ProductItem pItem) {
        
        ObjectId itemId = new ObjectId();

        pItem.setId(itemId);

        try {
            this.productItemRepository.save(pItem);
            return itemId;
        } catch(Exception e) {
            return null;
        }

    }

    @Override
    public ProductItem findProductItem(ObjectId itemId) {
        Optional<ProductItem> item = this.productItemRepository.findById(itemId);
        if (item.isPresent()) 
            return item.get();
        else
            return null;
    }
    
}
