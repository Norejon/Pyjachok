package com.example.pyjachok.models.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;
}
