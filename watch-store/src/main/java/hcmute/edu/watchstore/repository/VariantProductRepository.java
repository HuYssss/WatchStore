package hcmute.edu.watchstore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.VariantProduct;

@Repository
public interface VariantProductRepository extends MongoRepository<VariantProduct, ObjectId> {
    
}
