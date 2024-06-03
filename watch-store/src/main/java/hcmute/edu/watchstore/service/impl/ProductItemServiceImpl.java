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

    // tạo hoặc chỉnh sửa một product item
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

    // tìm một product item
    @Override
    public ProductItem findProductItem(ObjectId itemId) {
        Optional<ProductItem> item = this.productItemRepository.findById(itemId);
        return item.orElse(null);
    }

    // lấy danh sách product item từ danh sách id
    // vì product item bao gồm id, id sản phẩm và số lượng nên cần product item response đê trả về sản phẩm cho FE dễ xử lý
    @Override
    public List<ProductItemResponse> findProductItemResponse(List<ObjectId> itemId) {
        List<ProductItem> items = this.productItemRepository.findAll();
        List<ProductResponse> products = this.productService.findAll();
        List<ProductItemResponse> responses = new ArrayList<>();
        if (items.isEmpty() || products.isEmpty()) 
            return null;
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

    // xóa một product item
    @Override
    public boolean deleteItem(ObjectId itemId) {
        try {
            this.productItemRepository.deleteById(itemId);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }
    
    // tìm item trong danh sách
    public ProductItem findItem(ObjectId id, List<ProductItem> items) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // tìm product trong danh sách do id của product là ObjectId và FE sẽ không hiểu nên cần có product respone id là String để FE dễ xử lý hơn
    public ProductResponse findProduct(ObjectId id, List<ProductResponse> products) {
        return products.stream()
                 .filter(product -> product.getId().equals(id.toHexString()))
                 .findFirst()
                 .orElse(null);
    }
}
