package com.prince.project.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.prince.project.DAO.UserDAO;
import com.prince.project.config.AppConfig;
import com.prince.project.model.AppUser;
import com.prince.project.model.Role;
import com.prince.project.model.UserRole;
import com.prince.project.repository.RoleRepository;
import com.prince.project.repository.UserRepository;
import com.prince.project.repository.UserRoleRepository;
import com.prince.project.utils.EncrytedPasswordUtils;
import com.prince.project.utils.WebUtils;

@Service("appUserService")
public class AppUserService {
	@Autowired
    private UserDAO userDAO;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public void createUser(AppUser appUser) {
		appUser.setPassword(EncrytedPasswordUtils.encrytePassword(appUser.getPassword()));
    	appUser.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
    	appUser.setCode(WebUtils.prepareRandomString(8));
    	appUser.setEmailVerifyHash(WebUtils.prepareRandomString(40));
    	appUser.setScore(0);
    	appUser.setStatus(AppConfig.NEW);
        userRepository.save(appUser);
	}
	public void addRole(AppUser appUser, int roleId) {
		Role role = roleRepository.getOne(roleId);
		UserRole userRole = new UserRole(appUser, role);
		userRoleRepository.save(userRole);
		
	}
	public void confimAccount(String token) {
		AppUser user = userDAO.findUserAccountByToken(token);
        user.setStatus(AppConfig.ACTIVE);
        // doi token
        user.setEmailVerifyHash(WebUtils.prepareRandomString(40));
        
        addRole(user, AppConfig.USER);
        userRepository.save(user);
        
	}
	public void inResetPassword(AppUser appUser) {
		appUser.setStatus(AppConfig.IN_RESET_PASSWORD);
		userRepository.save(appUser);
	}
	public void resetPassword(AppUser appUser, String newPassWord) {
		appUser.setPassword(newPassWord);
		userRepository.save(appUser);
	}
	public boolean isUsernameAlreadyInUse(String username) {
		boolean userInDb = true;
		if (userDAO.findUserAccountByName(username) == null) userInDb = false;
		return userInDb;
	}

}
