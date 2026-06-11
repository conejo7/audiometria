package com.audiometria.audiometria.api.service.musica;

import com.audiometria.audiometria.api.repository.entities.User;
import com.audiometria.audiometria.api.repository.entities.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }


    public User findByFilterUser(String username) {
        return userRepository.findByFilterUser(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

}
