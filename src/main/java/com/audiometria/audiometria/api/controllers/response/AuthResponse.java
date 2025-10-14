package com.audiometria.audiometria.api.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String token;
    private String uid;
    private String email;
    private String displayName;


    public AuthResponse(String message) {
    }
}
