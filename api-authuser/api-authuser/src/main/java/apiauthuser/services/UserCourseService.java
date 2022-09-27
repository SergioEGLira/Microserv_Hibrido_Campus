package apiauthuser.services;

import java.util.UUID;

import apiauthuser.models.UserCourseModel;
import apiauthuser.models.UserModel;

public interface UserCourseService {

	boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

	UserCourseModel save(UserCourseModel userCourseModel);

	boolean existsByCourseId(UUID courseId);

	void deleteUserCourseByCourse(UUID courseId);

}
