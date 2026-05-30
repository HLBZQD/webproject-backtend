package com.hlb.webproject_wp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminUserVO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatarUrl;
    private String role;
    private LocalDateTime createdAt;
}
