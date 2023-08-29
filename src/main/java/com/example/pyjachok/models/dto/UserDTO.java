package com.example.pyjachok.models.dto;

import com.example.pyjachok.models.Gender;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {
    private String nickname;
    private String password;
    private String email;
    private String birth;
    private Gender gender;
}
