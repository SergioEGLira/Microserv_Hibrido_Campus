package apicourse.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import apicourse.models.CourseModel;
import apicourse.models.UserModel;

public interface CourseService {

	void delete(CourseModel courseModel);

	Optional<CourseModel> findById(UUID courseId);

	Object save(CourseModel courseModel);

	Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);

	boolean existsByCourseAndUser(UUID courseId, UUID userId);

	void saveSubscriptionUserInCourse(UUID courseId, UUID userId);

	void saveSubscriptionUserInCourseAndSendNotification(CourseModel course, UserModel user);

}
