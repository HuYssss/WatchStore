package hcmute.edu.watchstore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.Coupon;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, ObjectId> {
    
}
