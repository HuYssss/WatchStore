package hcmute.edu.watchstore.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.Address;

// interface dùng để truy suất collection Address
@Repository
public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    List<Address> findByUser(ObjectId user);
}
