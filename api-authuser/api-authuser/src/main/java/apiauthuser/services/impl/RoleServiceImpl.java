package apiauthuser.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apiauthuser.enums.RoleType;
import apiauthuser.models.RoleModel;
import apiauthuser.repositories.RoleRepository;
import apiauthuser.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
    RoleRepository roleRepository;

	@Override
    public Optional<RoleModel> findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType);
    }
}
