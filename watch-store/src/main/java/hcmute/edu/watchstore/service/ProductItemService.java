package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.entity.ProductItem;

public interface ProductItemService {
    ObjectId createProductItem(ProductItem productIt);
    ProductItem findProductItem(ObjectId itemId);
}
