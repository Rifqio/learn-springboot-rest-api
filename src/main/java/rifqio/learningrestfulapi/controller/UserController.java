package rifqio.learningrestfulapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rifqio.learningrestfulapi.dto.UserResponse;
import rifqio.learningrestfulapi.dto.WebResponse;
import rifqio.learningrestfulapi.dto.users.UpdateUserRequestDTO;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.service.UserService;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("me")
    public WebResponse<UserResponse> get(User user) {
        try {
            UserResponse userData = userService.get(user);
            return WebResponse.<UserResponse>builder()
                    .message("Get User Data Success")
                    .data(userData)
                    .build();
        } catch (Exception err) {
            return WebResponse.<UserResponse>builder()
                    .message("Get User Data Failed")
                    .errors(err.getMessage())
                    .build();
        }
    }

    @PatchMapping("me")
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequestDTO request) {
        try {
            UserResponse userResponse = userService.update(user, request);
            return WebResponse.<UserResponse>builder()
                    .message("User Updated")
                    .data(userResponse)
                    .build();

        } catch (Exception err) {
            return WebResponse.<UserResponse>builder()
                    .message("User Update Failed")
                    .errors(err.getMessage())
                    .build();
        }

    }
}
