package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.dto.request.OrderRequest;

public interface OrderService {
    ResponseEntity<?> getOrderUser(ObjectId userId);
    ResponseEntity<?> createOrder(OrderRequest order, ObjectId userId);
    ResponseEntity<?> cancelOrderr(ObjectId orderId, ObjectId userId);

    ResponseEntity<?> getByState(String state);
    ResponseEntity<?> setStateOrder(ObjectId orderId, String state);
}
