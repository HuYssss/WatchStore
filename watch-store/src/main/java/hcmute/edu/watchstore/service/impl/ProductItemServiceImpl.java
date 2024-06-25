package hcmute.edu.watchstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.dto.response.ProductResponse;
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
    public List<ProductItemResponse> findProductItemResponse(List<ObjectId> itemId) {
        List<ProductItem> items = this.productItemRepository.findAll();
        List<ProductResponse> products = this.productService.findAll();
        List<ProductItemResponse> responses = new ArrayList<>();
        if (items.isEmpty() || products.isEmpty())
            return responses;
        for (ObjectId id : itemId) {
            ProductItem item = findItem(id, items);
            if (item != null) {
                ProductItemResponse response = new ProductItemResponse(
                    item.getId().toHexString(),
                    findProduct(item.getProduct(), products),
                    item.getQuantity()
                );
                responses.add(response);
            }
        }
        return responses;
        
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
    
    public ProductItem findItem(ObjectId id, List<ProductItem> items) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public ProductResponse findProduct(ObjectId id, List<ProductResponse> products) {
        return products.stream()
                 .filter(product -> product.getId().equals(id.toHexString()))
                 .findFirst()
                 .orElse(null);
    }

    @Override
    public List<ProductItem> findItemByList(List<ObjectId> itemId) {
        List<ProductItem> allItems = this.productItemRepository.findAll();
        List<ProductItem> response = new ArrayList<>();
        for(ObjectId id : itemId) {
            ProductItem item = findItem(id, allItems);
            if (item != null) {
                response.add(item);
            }
        }
        return response;
    }

    @Override
    public boolean deleteItemAdvance(List<ObjectId> listItemId, boolean handleQuantityProduct) {
        try {
            if (handleQuantityProduct) {
                List<ProductItem> items = findItemByList(listItemId);
                List<Product> products = this.productService.findAllNormal();
                List<Product> updated = new ArrayList<>();
                for(ProductItem item : items) {
                    Product product = findProductNormal(item.getProduct(), products);
                    if (product != null) {
                        int amount = product.getAmount();
                        amount = amount + item.getQuantity();
                        product.setAmount(amount);
                        updated.add(product);
                    }
                }
                this.productService.saveProductByList(updated);
            }
            this.productItemRepository.deleteAllById(listItemId);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    public Product findProductNormal(ObjectId id, List<Product> products) {
        return products.stream()
                 .filter(product -> product.getId().equals(id))
                 .findFirst()
                 .orElse(null);
    }

    @Override
    public boolean updateItem(List<ProductItem> listItem) {
        try {
            this.productItemRepository.saveAll(listItem);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    @Override
    public boolean cancelItem(List<ObjectId> listItem) {
        try {
            List<ProductItem> items = findItemByList(listItem);
            List<Product> products = this.productService.findAllNormal();
            List<Product> updated = new ArrayList<>();
            for(ProductItem item : items) {
                Product product = findProductNormal(item.getProduct(), products);
                if (product != null) {
                    int amount = product.getAmount();
                    amount = amount + item.getQuantity();
                    product.setAmount(amount);
                    updated.add(product);
                }
            }
            this.productService.saveProductByList(updated);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
