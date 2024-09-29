package hexlet.code.controller;

import hexlet.code.dto.AuthDTO;
import hexlet.code.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public String login(@RequestBody AuthDTO loginData) {
        var authentification = new UsernamePasswordAuthenticationToken(
                loginData.getUsername(), loginData.getPassword()
        );
        authenticationManager.authenticate(authentification);
        var token = jwtUtils.generateToken(loginData.getUsername());
        return token;
    }
}
