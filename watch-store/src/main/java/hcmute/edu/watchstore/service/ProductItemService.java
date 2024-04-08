package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.entity.ProductItem;

public interface ProductItemService {
    ObjectId saveOrEditItem(ProductItem productIt);
    ProductItem findProductItem(ObjectId itemId);
    ProductItemResponse findProductItemResponse(ObjectId itemId);
    boolean deleteItem(ObjectId itemId);
}
