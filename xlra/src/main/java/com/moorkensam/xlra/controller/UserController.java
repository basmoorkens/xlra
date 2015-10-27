package com.moorkensam.xlra.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;

@ManagedBean
@ViewScoped
public class UserController {

	@Inject
	private UserService userService;

	private User user;

	private String newPassword;

	private String retypedPassword;

	@PostConstruct
	public void initialize() {
		// fetch user from session;..
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRetypedPassword() {
		return retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}

	public void saveUser() {
		if (newPassword.equals(retypedPassword)) {
			user.setPassword(newPassword);
			userService.updateUser(user);
		} else {
			MessageUtil
					.addErrorMessage("Passwords do not match",
							"The new password and the retyped new password should be the same!");
		}
	}

}
