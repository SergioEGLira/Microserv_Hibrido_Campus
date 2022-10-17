package apiauthuser.services;

import java.util.Optional;

import apiauthuser.enums.RoleType;
import apiauthuser.models.RoleModel;

public interface RoleService {
	
	Optional<RoleModel> findByRoleName(RoleType roleType);

}
