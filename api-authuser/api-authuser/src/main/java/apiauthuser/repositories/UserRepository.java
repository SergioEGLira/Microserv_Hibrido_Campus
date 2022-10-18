package apiauthuser.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import apiauthuser.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>, 
										JpaSpecificationExecutor<UserModel> {
	
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findByUsername(String username);
}