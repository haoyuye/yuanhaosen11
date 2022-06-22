package com.ssp.dto;

import com.ssp.entities.User;
import lombok.Data;

@Data
public class UserDto extends User {
    String code;
}
