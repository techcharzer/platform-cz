package com.cz.platform.security;

import java.util.List;

public interface UserDTO {
	public String getUserId();

	public String getMobileNumber();

	public List<RoleDTO> getRoles();
}