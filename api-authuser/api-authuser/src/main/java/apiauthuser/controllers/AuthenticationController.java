package apiauthuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import apiauthuser.dtos.JwtDto;
import apiauthuser.dtos.LoginDto;
import apiauthuser.dtos.UserDto;
import apiauthuser.enums.RoleType;
import apiauthuser.enums.UserStatus;
import apiauthuser.enums.UserType;
import apiauthuser.models.RoleModel;
import apiauthuser.models.UserModel;
import apiauthuser.security.JwtProvider;
import apiauthuser.services.RoleService;
import apiauthuser.services.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
    UserService userService;
	
	@Autowired
	RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;
	
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;
    
	@PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class) 
    		@JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
		log.debug("POST registerUser userDTO received {} ", userDto.toString());
		if(userService.existsByUsername(userDto.getUsername())){
			log.warn("Usu??rio {} j?? anteriormente criado...", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Username j?? existe!");
        }
        if(userService.existsByEmail(userDto.getEmail())){
        	log.warn("Email {} j?? existe...", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email j?? existe!");
        }
        
        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);
        userService.saveUser(userModel);
        
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("Usu??rio {} salvo com sucesso!", userModel.getUserId());
        return  ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	}

	@PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }
}
