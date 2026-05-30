package com.hlb.webproject_wp.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDTO {

    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String nickname;

    @Size(max = 20)
    private String role;
}
