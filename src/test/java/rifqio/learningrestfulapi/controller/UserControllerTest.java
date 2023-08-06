package rifqio.learningrestfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import rifqio.learningrestfulapi.dto.response.UserResponse;
import rifqio.learningrestfulapi.dto.response.WebResponse;
import rifqio.learningrestfulapi.dto.request.users.UpdateUserRequestDTO;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.repository.UserRepository;
import utils.BCrypt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMVc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMVc.perform(get("/api/users/me")).andExpectAll(status().isUnauthorized());
    }

    @Test
    void getUserWrongToken() throws Exception {
        mockMVc.perform(get("/api/users/me")
                        .header("X-API-TOKEN", "noToken"))
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("coba");
        user.setName("coba");
        user.setPassword(BCrypt.hashpw("coba", BCrypt.gensalt()));
        user.setToken("coba");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);
        userRepository.save(user);

        mockMVc.perform(get("/api/users/me")
                        .header("X-API-TOKEN", "coba"))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("coba", response.getData().getUsername());
                    assertEquals("coba", response.getData().getName());
                })
        ;
    }

    @Test
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setUsername("coba2");
        user.setName("coba2");
        user.setPassword(BCrypt.hashpw("coba2", BCrypt.gensalt()));
        user.setToken("coba2");
        user.setTokenExpiredAt(System.currentTimeMillis() - 100000L);
        userRepository.save(user);

        mockMVc.perform(get("/api/users/me")
                        .header("X-API-TOKEN", "coba2"))
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    void updateUserUnauthorized() throws Exception {
        mockMVc.perform(patch("/api/users/me")).andExpectAll(status().isUnauthorized());
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("coba2");
        user.setName("coba2");
        user.setPassword(BCrypt.hashpw("coba2", BCrypt.gensalt()));
        user.setToken("coba2");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
        userRepository.save(user);

        UpdateUserRequestDTO request = new UpdateUserRequestDTO();
        request.setName("Johnny");
        request.setPassword("johnny12345");

        mockMVc.perform(patch("/api/users/me")
                .contentType("application/json")
                .accept("application/json")
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "coba2")
        ).andExpectAll(status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                    .getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });
            assertNull(response.getErrors());
            assertEquals("Johnny", response.getData().getName());
            assertEquals("coba2", response.getData().getUsername());
        })
        ;
    }
}