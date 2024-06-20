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
import hcmute.edu.watchstore.dto.response.ProductItemResponse;
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
        String message = handleManageProductInCart(productItem, getCartUser(userId));
        if (message == null)
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());

        return success(message);
    }

    // complete
    @Override
    public ResponseEntity<?> findCartByUser(ObjectId userId) {
        Cart cartUser = getCartUser(userId);
        
        List<ProductItemResponse> responses = getProductItemResp(cartUser.getProductItems());

        return success(responses);
    }


    // helper funtion
    public List<ProductItemResponse> getProductItemResp(List<ObjectId> pItemId) {
        return this.productItemService.findProductItemResponse(pItemId);
    }

    public Cart getCartUser(ObjectId userId) {
        Optional<Cart> userCart = this.cartRepository.findByUser(userId);
        return userCart.orElse(null);
    }

    public String handleManageProductInCart(ProductItem productItem, Cart userCart) {
        List<ProductItemResponse> cartResp = getProductItemResp(userCart.getProductItems());
        boolean itemPresent = false;
        if (cartResp != null && productItem.getProduct() != null) {
            for (ProductItemResponse resp : cartResp) {
                if (resp.getProduct().getId().equals(productItem.getProduct().toHexString())) {
                    itemPresent = true;
                    productItem.setId(new ObjectId(resp.getId()));
                    productItem.setQuantity(productItem.getQuantity() + resp.getQuantity());
                }
            }
        }

        List<ObjectId> newItem = userCart.getProductItems();

        ObjectId newId = this.productItemService.saveOrEditItem(productItem);
        if (itemPresent == false)
            newItem.add(newId);
        
        try {
            userCart.setProductItems(newItem);
            this.cartRepository.save(userCart);
            return newId.toHexString();
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public List<ObjectId> deleteCart(ObjectId cartId) {
        Optional<Cart> cart = this.cartRepository.findById(cartId);

        try {
            this.cartRepository.deleteById(cartId);
            return cart.get().getProductItems();
        } catch (MongoException e) {
            return null;
        }
        
    }

    @Override
    public ObjectId saveCart(Cart cart) {
        if (cart.getId() == null) {
            cart.setId(new ObjectId());
        }

        try {
            this.cartRepository.save(cart);
            return cart.getId();
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> updateCart(List<ProductItemResponse> listResp, ObjectId userId) {
        Cart userCart = getCartUser(userId);

        if (userCart == null) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }

        List<ObjectId> cartItem = userCart.getProductItems();
        List<ProductItem> currentCart = this.productItemService.findItemByList(cartItem);
        List<ProductItem> presentCart = new ArrayList<>(currentCart);
        List<ObjectId> itemDelete = new ArrayList<>();

        boolean isDelete = true;

        for(ProductItem item : currentCart) {
            for(ProductItemResponse resp : listResp) {
                if (item.getId().toHexString().equals(resp.getId())) {
                    item.setQuantity(resp.getQuantity());
                    isDelete = false;
                }
            }

            if (isDelete) {
                presentCart.remove(item);
                itemDelete.add(item.getId());
                cartItem.remove(item.getId());
            }

            isDelete = true;
        }

        
        try {
            userCart.setProductItems(cartItem);
            saveCart(userCart);
            this.productItemService.updateItem(presentCart);
            this.productItemService.deleteItemAdvance(itemDelete, false);
            return success("Update cart user success !!!");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    
}
