package rifqio.learningrestfulapi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rifqio.learningrestfulapi.dto.auth.LoginUserRequestDTO;
import rifqio.learningrestfulapi.dto.TokenResponse;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.repository.UserRepository;
import utils.BCrypt;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequestDTO request) {
        validationService.validate(request);
        // to validate the username if exist
        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        // to validate the password if match
        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (30L * 24 * 60 * 60 * 10000);
    }
}
