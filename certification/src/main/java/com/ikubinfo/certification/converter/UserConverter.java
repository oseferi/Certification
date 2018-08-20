package com.ikubinfo.certification.converter;

import com.ikubinfo.certification.dto.PasswordDto;
import com.ikubinfo.certification.model.User;

public class UserConverter {

public static PasswordDto toPasswordDto(User user) {
	PasswordDto passwordDto = new PasswordDto();
	passwordDto.setId(user.getId());
	passwordDto.setUsername(user.getUsername());
	return passwordDto;
}

}
