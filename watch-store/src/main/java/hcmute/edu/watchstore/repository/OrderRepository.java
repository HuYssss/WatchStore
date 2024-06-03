package hcmute.edu.watchstore.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.Order;

// interface dùng để truy suất collection Order
@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    Optional<Order> findByUser(ObjectId user);
}
