package com.audiometria.audiometria.api.controllers.Auth;

import com.audiometria.audiometria.api.controllers.request.AuthRequest;
import com.audiometria.audiometria.api.controllers.response.AuthResponse;
import com.audiometria.audiometria.api.repository.entities.User;
import com.audiometria.audiometria.api.service.musica.UserService;
import com.audiometria.audiometria.api.service.seguridad.AuthService;
import lombok.Getter;
import lombok.Setter;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://musicaapp-production.up.railway.app",
        "https://audiometria-production.up.railway.app"
})
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {


            String token = authService.login(request.getUsername(), request.getPassword());

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials"));
            }
            User user = userService.findByUsername(request.getUsername());

            return ResponseEntity.ok(new AuthResponse(
                    token,
                    String.valueOf(user.getId()),
                    user.getEmail(),
                    user.getUsername()
            ));
//            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(e.getMessage()));

        }
    }

}
