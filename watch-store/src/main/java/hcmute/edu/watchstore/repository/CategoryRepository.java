package hcmute.edu.watchstore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    
}
