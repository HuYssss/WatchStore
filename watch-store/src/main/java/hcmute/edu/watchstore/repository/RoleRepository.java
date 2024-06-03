package hcmute.edu.watchstore.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.Role;

// interface dùng để truy suất collection Role
@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {
}