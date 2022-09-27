package apicourse.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import apicourse.models.CourseModel;

public interface CourseService {

	void delete(CourseModel courseModel);

	Optional<CourseModel> findById(UUID courseId);

	Object save(CourseModel courseModel);

	Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);
}
