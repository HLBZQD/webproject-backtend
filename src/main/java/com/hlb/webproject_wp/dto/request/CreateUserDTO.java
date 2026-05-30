package com.hlb.webproject_wp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be 6-100 chars")
    private String password;

    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String nickname;

    @Size(max = 20)
    private String role;
}
