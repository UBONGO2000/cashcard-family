package com.example.backend.service;



import com.example.backend.domain.User;
import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.dto.auth.LoginRequest;
import com.example.backend.dto.user.RegisterUserRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            JwtService jwtService,
            UserService userService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterUserRequest request) {
        UserResponse createdUser = userService.register(request);

        User user = userRepository.findByEmail(createdUser.email())
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        return AuthResponse.of(token, createdUser);
    }

//    public AuthResponse login(LoginRequest request) {
//        User user = userRepository.findByEmail(request.email())
//                .orElseThrow(() -> new UnauthorizedException("Email ou mot de passe incorrect"));
//
//        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
//            throw new UnauthorizedException("Email ou mot de passe incorrect");
//        }
//
//        String token = jwtService.generateToken(
//                user.getEmail(),
//                user.getRole().name(),
//                user.getId()
//        );
//
//        return AuthResponse.of(token, UserResponse.from(user));
//    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));

        return UserResponse.from(user);
    }


    public AuthResponse login(LoginRequest request) {
        System.out.println(">>> LOGIN tentative pour : " + request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Email ou mot de passe incorrect"));

        System.out.println(">>> Utilisateur trouvé : " + user.getEmail());
        System.out.println(">>> Hash stocké : " + user.getPassword());
        System.out.println(">>> Matches : " + passwordEncoder.matches(request.password(), user.getPassword()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Email ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        return AuthResponse.of(token, UserResponse.from(user));
    }
}