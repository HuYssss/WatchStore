package hcmute.edu.watchstore.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hcmute.edu.watchstore.entity.AuthToken;


@Repository
public interface AuthTokenRepository extends MongoRepository<AuthToken, ObjectId> {
    Optional<AuthToken> findByToken(String token);
}
