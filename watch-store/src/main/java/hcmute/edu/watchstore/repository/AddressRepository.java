package hcmute.edu.watchstore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.Address;

@Repository
public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    
}
