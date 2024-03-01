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
import hcmute.edu.watchstore.dto.response.CartResponse;
import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.repository.CartRepository;
import hcmute.edu.watchstore.service.CartService;
import hcmute.edu.watchstore.service.ProductItemService;

@Service
public class CartServiceImpl extends ServiceBase implements CartService {

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseEntity<?> addProductToCart(ProductItem productItem, ObjectId userId) {
        Optional<Cart> userCart = this.cartRepository.findByUser(userId);

        if (!userCart.isPresent()) {
            return error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
        }
    
        try {
            ObjectId productItemId = this.productItemService.createProductItem(productItem);

            userCart.get().getProductItems().add(productItemId);

            this.cartRepository.save(userCart.get());

            return success(productItem);

        } catch (Exception e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
            
    }

    @Override
    public ResponseEntity<?> findCartByUser(ObjectId userId) {
        Optional<Cart> userCart = this.cartRepository.findByUser(userId);
        if (userCart.isPresent()) {
            CartResponse cartResp = new CartResponse();
            List<ProductItem> itemList = new ArrayList<>();

            for(ObjectId id : userCart.get().getProductItems()) {
                ProductItem item = this.productItemService.findProductItem(id);
                if (item != null) 
                    itemList.add(item);
            }

            cartResp.setProductItems(itemList);

            return success(cartResp);
        }
        else
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
    }
    
}
