package apicourse.validation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import apicourse.dtos.CourseDto;
import apicourse.enums.UserType;
import apicourse.models.UserModel;
import apicourse.security.AuthenticationCurrentUserService;
import apicourse.services.UserService;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    UserService userService;
    
    @Autowired
    AuthenticationCurrentUserService authenticationCurrentUserService;
    
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto, errors);
        if(!errors.hasErrors()){
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors){
    	 UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
         if(currentUserId.equals(userInstructor)) {    	    	
	    	Optional<UserModel> userModelOptional = userService.findById(userInstructor);
	        if(!userModelOptional.isPresent()){
	            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
	        }
	        else if(userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())){
	            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
	        }
         } else {
             throw new AccessDeniedException("Forbidden");
         }
     }
         
}
