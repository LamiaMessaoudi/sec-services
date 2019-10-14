package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dao.AppRoleRepository;
import dao.AppUserRepository;
import entities.AppRole;
import entities.AppUser;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	AppUserRepository appUserRepository;
	@Autowired
	AppRoleRepository appRoleRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public AppUser saveUser(String username, String password, String confirmedPassword) {

		AppUser user=appUserRepository.findByUsername(username);
		if(user!=null) throw new RuntimeException("User already Exists"); 
		if(!password.equals(confirmedPassword))  throw new RuntimeException("Please confir your password"); 
		AppUser appUser=new AppUser();
		appUser.setUsername(username);
		appUser.setActivated(true);
		appUser.setPassword(bCryptPasswordEncoder.encode(password));
		appUserRepository.save(appUser);
		addRoleToUser(username, "USER");
		return appUser;
	}

	@Override
	public AppRole save(AppRole role) {
		return appRoleRepository.save(role);
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findByUsername(username);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppUser appUser=appUserRepository.findByUsername(username);
		AppRole appRole=appRoleRepository.findByRoleName(rolename);
		appUser.getRoles().add(appRole);
	}

}
