package apiauthuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import apiauthuser.dtos.UserDto;
import apiauthuser.enums.UserStatus;
import apiauthuser.enums.UserType;
import apiauthuser.models.UserModel;
import apiauthuser.services.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
    UserService userService;
	
	@PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class) 
    		@JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
		log.debug("POST registerUser userDTO received {} ", userDto.toString());
		if(userService.existsByUsername(userDto.getUsername())){
			log.warn("Usuário {} já anteriormente criado...", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Username já existe!");
        }
        if(userService.existsByEmail(userDto.getEmail())){
        	log.warn("Email {} já existe...", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email já existe!");
        }
        
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("Usuário {} salvo com sucesso!", userModel.getUserId());
        return  ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	}

}
