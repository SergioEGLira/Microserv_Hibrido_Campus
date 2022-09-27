package apicourse.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apicourse.models.CourseModel;
import apicourse.models.CourseUserModel;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

	boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

	@Query(value="select * from tb_courses_users where course_course_id = :courseId", nativeQuery = true)
    List<CourseUserModel> findAllCourseUserIntoCourse(@Param("courseId") UUID courseId);
	
	 boolean existsByUserId(UUID userId);
	 
	 void deleteAllByUserId(UUID userId);
}
