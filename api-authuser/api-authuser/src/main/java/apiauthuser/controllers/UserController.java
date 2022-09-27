package apiauthuser.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import apiauthuser.dtos.UserDto;
import apiauthuser.models.UserModel;
import apiauthuser.services.UserService;
import apiauthuser.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private Environment env;
	
	@Autowired
	UserService userService;

	@GetMapping
	public ResponseEntity<Page<UserModel>> getAllUsersPaged(SpecificationTemplate.UserSpec spec,
						@PageableDefault(page = 0, size = 20, 
										sort = "userId", direction = Sort.Direction.ASC) 
										Pageable pageable,
										@RequestParam(required = false) UUID courseId){
		
		logger.info("PORTA UTILIZADA EM NOSSO TESTE DE INSTANCIAS = " + env.getProperty("local.server.port"));
		
		Page<UserModel> userModelPage = null;
		if(courseId != null) {
			userModelPage = userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable);
        } else {
            userModelPage = userService.findAll(spec, pageable);
        }
		if(!userModelPage.isEmpty()){
            for(UserModel user : userModelPage.toList()){
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }		
		return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
	}

	@GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
		Optional<UserModel> userModelOptional = userService.findById(userId);
		if(!userModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User não encontrado...");
        } else{
            return  ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
        }
	}
	
	@DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
		log.debug("DELETE deleteUser userId received {} ", userId);
		Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User não encontrado...");
        } else{
            userService.delete(userModelOptional.get());
            log.debug("DELETE deleteUser userId received {} ", userId);
            log.info("Usuário {} foi deletado com sucesso!", userId);
            return  ResponseEntity.status(HttpStatus.OK).body("User deletado com sucesso.");
        }
    }

	@PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,                                             
    		@RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
		log.debug("PUT updateUser userDTO received {} ", userDto.toString());
		Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User não encontrado.");
        } else{
            var userModel = userModelOptional.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            log.debug("PUT updateUser userModel received {} ", userModel.getUserId());
            log.info("Usuário {} atualizado com sucesso!", userModel.getUserId());
            return  ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

	@PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,                                                 
    		@RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User não encontrado.");
        } if(!userModelOptional.get().getPassword().equals(userDto.getOldPassword())){
        	log.warn("Erro: Senhas divergentes para o userId {}...", userId);        	
        	return  ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Senhas divergentes...");  
        } else{
            var userModel = userModelOptional.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return  ResponseEntity.status(HttpStatus.OK).body("Senha atualizada com sucesso.");
        }
    }
	
	 @PutMapping("/{userId}/image")
	    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
	    		@RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
	        Optional<UserModel> userModelOptional = userService.findById(userId);
	        if(!userModelOptional.isPresent()){
	            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User não encontrado.");
	        } else{
	            var userModel = userModelOptional.get();
	            userModel.setImageUrl(userDto.getImageUrl());
	            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
	            userService.save(userModel);
	            return  ResponseEntity.status(HttpStatus.OK).body(userModel);
	    }
	}
}
