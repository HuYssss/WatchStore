package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Order;

public interface OrderService {
    ResponseEntity<?> getOrderUser(ObjectId userId);
    ResponseEntity<?> createOrder(Order order, ObjectId userId);
    ResponseEntity<?> cancelOrderr(ObjectId orderId, ObjectId userId);
}
