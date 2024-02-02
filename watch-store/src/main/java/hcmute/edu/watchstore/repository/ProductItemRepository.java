package hcmute.edu.watchstore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.ProductItem;

@Repository
public interface ProductItemRepository extends MongoRepository<ProductItem, ObjectId> {
    
}
