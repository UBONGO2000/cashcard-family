package com.example.backend.service;


import com.example.backend.domain.User;
import com.example.backend.domain.UserRole;
import com.example.backend.dto.user.RegisterUserRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.exception.DuplicateEmailException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Cet email est déjà utilisé");
        }

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserRole.PARENT
        );

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'id : " + userId));

        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'email : " + email));

        return UserResponse.from(user);
    }
}