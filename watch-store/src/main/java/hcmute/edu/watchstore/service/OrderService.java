package hcmute.edu.watchstore.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.request.BuyNowRequest;
import hcmute.edu.watchstore.dto.request.OrderRequest;

public interface OrderService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getOrderUser(ObjectId userId);
    ResponseEntity<?> createOrder(OrderRequest order, ObjectId userId) throws UnsupportedEncodingException;
    ResponseEntity<?> cancelOrderr(ObjectId orderId, ObjectId userId);
    ResponseEntity<?> getByState(String state);
    ResponseEntity<?> setStateOrder(ObjectId orderId, String state);
    List<ObjectId> deleteOrder(List<ObjectId> orderIds);
    ResponseEntity<?> approvalOrder(ObjectId orderId);
    boolean isUserOrderShipping(ObjectId userId);
    ResponseEntity<?> isOrderDelivered(ObjectId orderId, ObjectId userId);
    ResponseEntity<?> getOrderDetail(ObjectId orderId);
    ResponseEntity<?> buyNow(BuyNowRequest request, ObjectId userId) throws UnsupportedEncodingException;   
}
