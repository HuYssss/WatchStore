package hcmute.edu.watchstore.service.impl;

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

    // thêm một sp vào giỏ hàng
    @Override
    public ResponseEntity<?> addProductToCart(ProductItem productItem, ObjectId userId) {
        if (handleManageProductInCart(productItem, getCartUser(userId)))
            return success("Add product to cart success !!!");

        return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
    }

    // lấy toàn bộ sản phẩm có trong giỏ hàng
    @Override
    public ResponseEntity<?> findCartByUser(ObjectId userId) {
        Cart cartUser = getCartUser(userId);
        
        List<ProductItemResponse> responses = getProductItemResp(cartUser.getProductItems());

        return success(responses);
    }

    // chỉnh sửa số lượng sản phẩm có trong giò hảng
    @Override
    public ResponseEntity<?> editProductInCart(ProductItem productItem, ObjectId userId) {
        ProductItem item = this.productItemService.findProductItem(productItem.getId());
        if (item != null) {
            item.setProduct(productItem.getProduct());
            item.setQuantity(productItem.getQuantity());
            this.productItemService.saveOrEditItem(item);

            return success("Edit product item success !!!");
        }
        else
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
    }


    // lấy danh sách ProductItemResponse(id, product, quantity) từ list productItemId
    // em sử dụng ProductItemResponse vì ProductItem ở db chỉ có id, id sản phẩm và số lượng
    public List<ProductItemResponse> getProductItemResp(List<ObjectId> pItemId) {
        return this.productItemService.findProductItemResponse(pItemId);
    }

    // lấy giỏ hàng user từ database
    public Cart getCartUser(ObjectId userId) {
        Optional<Cart> userCart = this.cartRepository.findByUser(userId);
        return userCart.orElse(null);
    }

    // handle cho các hành động thêm xóa sửa sản phẩm có trong giỏ hàng
    public boolean handleManageProductInCart(ProductItem productItem, Cart userCart) {
        List<ProductItemResponse> cartResp = getProductItemResp(userCart.getProductItems());
        boolean itemPresent = false;
        if (cartResp != null) {
            for (ProductItemResponse resp : cartResp) {
                if (resp.getProduct().getId().equals(productItem.getProduct().toHexString())) {
                    itemPresent = true;
                    productItem.setId(new ObjectId(resp.getId()));
                    productItem.setQuantity(productItem.getQuantity() + resp.getQuantity());
                }
            }
        }

        List<ObjectId> newItem = userCart.getProductItems();

        // delete product item and handle delete in user's cart
        if (productItem.getQuantity() == 0 && this.productItemService.deleteItem(productItem.getId())) {
            newItem.remove(productItem.getId());
        } else {
            ObjectId newId = this.productItemService.saveOrEditItem(productItem);
            if (itemPresent == false) {
                newItem.add(newId);
            }
        }
        
        // update cart user
        try {
            userCart.setProductItems(newItem);
            this.cartRepository.save(userCart);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    // xóa sản phẩm có trong giỏ hàng
    @Override
    public ResponseEntity<?> deleteProductInCart(ProductItem productItem, ObjectId userId) {
        if (handleManageProductInCart(productItem, getCartUser(userId)))
            return success("Delete product in cart success !!!");

        return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
    }
}
