package apicourse.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import apicourse.clients.AuthUserClient;
import apicourse.models.CourseModel;
import apicourse.models.CourseUserModel;
import apicourse.models.LessonModel;
import apicourse.models.ModuleModel;
import apicourse.repositories.CourseRepository;
import apicourse.repositories.CourseUserRepository;
import apicourse.repositories.LessonRepository;
import apicourse.repositories.ModuleRepository;
import apicourse.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
    CourseRepository courseRepository;

	@Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;
	
    @Autowired
    CourseUserRepository courseUserRepository;
    
    @Autowired
    AuthUserClient authUserClient;
    
	@Transactional
    @Override
    public void delete(CourseModel courseModel) {
		boolean deleteCourseUserInAuthUser = false;
		List<ModuleModel> moduleModelList = moduleRepository.findAllLModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()){
            for(ModuleModel module : moduleModelList){
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
        if(!courseUserModelList.isEmpty()){
            courseUserRepository.deleteAll(courseUserModelList);
            deleteCourseUserInAuthUser = true;
        }
        courseRepository.delete(courseModel);
        if(deleteCourseUserInAuthUser){
            authUserClient.deleteCourseInAuthUser(courseModel.getCourseId());
        }
    }

	@Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

	@Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

	@Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
