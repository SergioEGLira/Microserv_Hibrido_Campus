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

import apicourse.dtos.NotificationCommandDto;
import apicourse.models.CourseModel;
import apicourse.models.LessonModel;
import apicourse.models.ModuleModel;
import apicourse.models.UserModel;
import apicourse.publishers.NotificationCommandPublisher;
import apicourse.repositories.CourseRepository;
import apicourse.repositories.LessonRepository;
import apicourse.repositories.ModuleRepository;
import apicourse.repositories.UserRepository;
import apicourse.services.CourseService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
    CourseRepository courseRepository;

	@Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;
	
    @Autowired
    UserRepository userRepository;
            
    @Autowired
    NotificationCommandPublisher notificationCommandPublisher;    
    
	@Transactional
    @Override
    public void delete(CourseModel courseModel) {
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
        courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        courseRepository.delete(courseModel);
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

	@Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        courseRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(CourseModel course, UserModel user) {
        courseRepository.saveCourseUser(course.getCourseId(), user.getUserId());
        try {
            var notificationCommandDto = new NotificationCommandDto();
            notificationCommandDto.setTitle("Bem-Vindo(a) ao Curso: " + course.getName());
            notificationCommandDto.setMessage(user.getFullName() + " a sua inscrição foi realizada com sucesso!");
            notificationCommandDto.setUserId(user.getUserId());
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDto);
        } catch (Exception e) {
            log.warn("Houve erro no envio da notificação!");
        }
    }
}
