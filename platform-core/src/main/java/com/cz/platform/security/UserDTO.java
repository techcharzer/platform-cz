package com.cz.platform.security;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
	private String userName;
	private List<RoleDTO> roles;
}
