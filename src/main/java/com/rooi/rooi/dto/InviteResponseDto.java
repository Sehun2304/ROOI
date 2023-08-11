package com.rooi.rooi.dto;

import com.rooi.rooi.entity.Board;
import com.rooi.rooi.entity.Permission;
import lombok.Getter;

@Getter
public class InviteResponseDto {
	private String username;

	public InviteResponseDto(Permission permission) {
		this.username = permission.getUser().getUsername();
	}
}
