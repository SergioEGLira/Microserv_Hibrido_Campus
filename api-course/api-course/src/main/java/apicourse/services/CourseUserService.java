package apicourse.services;

import java.util.UUID;

import apicourse.models.CourseModel;
import apicourse.models.CourseUserModel;

public interface CourseUserService {
	
	boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);
	
	CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel);

	boolean existsByUserId(UUID userId);

	void deleteCourseUserByUser(UUID userId);

}
