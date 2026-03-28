package cn.whpu.library.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    private String username;
    private String password;
}
