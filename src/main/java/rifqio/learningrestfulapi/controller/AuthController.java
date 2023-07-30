package rifqio.learningrestfulapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rifqio.learningrestfulapi.dto.auth.LoginUserRequestDTO;
import rifqio.learningrestfulapi.dto.auth.RegisterUserRequestDTO;
import rifqio.learningrestfulapi.dto.TokenResponse;
import rifqio.learningrestfulapi.dto.WebResponse;
import rifqio.learningrestfulapi.service.AuthService;
import rifqio.learningrestfulapi.service.UserService;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public WebResponse<String> register(@RequestBody RegisterUserRequestDTO request) {
        try {
            userService.register(request);

            return WebResponse.<String>builder()
                    .message("Register Success")
                    .data(request.getUsername())
                    .build();

        } catch (Exception err) {
            return WebResponse.<String>builder()
                    .message("Register Failed")
                    .errors(err.getMessage())
                    .build();
        }
    }

    @PostMapping("login")
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequestDTO request) {
        try {
            var tokenResponse = authService.login(request);
            return WebResponse.<TokenResponse>builder()
                    .message("Login Success")
                    .data(tokenResponse)
                    .build();
        } catch (Exception err) {
            return WebResponse.<TokenResponse>builder()
                    .message("Login Failed")
                    .errors(err.getMessage())
                    .build();
        }
    }
}
