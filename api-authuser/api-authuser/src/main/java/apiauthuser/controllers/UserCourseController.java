package apiauthuser.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import apiauthuser.clients.CourseClient;
import apiauthuser.models.UserModel;
import apiauthuser.services.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

	@Autowired
	CourseClient courseClient;
	
	@Autowired
    UserService userService;

	@GetMapping("/users/{userId}/courses")
    public ResponseEntity<Object> getAllCoursesByUser(
    		@PageableDefault(page = 0, size = 20, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId){
		Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }
	
}
