package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;

@ManagedBean
@SessionScoped
public class UserSessionController {

	private final static Logger logger = LogManager.getLogger();

	@Inject
	private UserService userService;

	private User loggedInUser;

	@PostConstruct
	public void initialize() {
		String loggedInUserName = FacesContext.getCurrentInstance()
				.getExternalContext().getRemoteUser();
		loggedInUser = userService.getUserByEmail(loggedInUserName);
		logger.info("Start user session for " + loggedInUser.getEmail());
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean hasPermission(String permission) {
		for (Permission p : loggedInUser.getAllPermissions()) {
			if (p.getKey().equalsIgnoreCase(permission)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasNonePermissions(List<String> permissions) {
		for (String p : permissions) {
			for (Permission perm : loggedInUser.getAllPermissions()) {
				if (perm.getKey().equalsIgnoreCase(p)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAdmin() {
		for (Role role : loggedInUser.getRoles()) {
			if (role.getName().equalsIgnoreCase("admin")) {
				return true;
			}
		}
		return false;
	}
}
