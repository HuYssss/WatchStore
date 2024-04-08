package hcmute.edu.watchstore.service.impl;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.entity.Order;
import hcmute.edu.watchstore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

    @Override
    public ResponseEntity<?> getOrderUser(ObjectId userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderUser'");
    }

    @Override
    public ResponseEntity<?> createOrder(Order order, ObjectId userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createOrder'");
    }

    @Override
    public ResponseEntity<?> cancelOrderr(ObjectId orderId, ObjectId userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelOrderr'");
    }
    
}
