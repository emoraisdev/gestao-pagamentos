package br.com.fiap.msclientes.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordEncoderService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
