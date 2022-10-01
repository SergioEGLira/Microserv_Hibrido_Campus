package apicourse.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import apicourse.models.UserModel;

public interface UserService {

	Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

	UserModel save(UserModel userModel);

	void delete(UUID userId);
}
