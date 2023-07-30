package rifqio.learningrestfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import rifqio.learningrestfulapi.dto.auth.RegisterUserRequestDTO;
import rifqio.learningrestfulapi.dto.WebResponse;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.repository.UserRepository;
import utils.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

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
    void testRegisterSuccess() throws Exception {
        RegisterUserRequestDTO request = new RegisterUserRequestDTO();
        request.setUsername("");
        request.setPassword("");
        request.setName("");
        mockMVc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });

    }

    @Test
    void logoutFailed() throws Exception {
        mockMVc.perform(delete("/api/auth/logout")
                        .accept("application/json"))
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 10000));
        userRepository.save(user);

        mockMVc.perform(delete("/api/auth/logout")
                        .accept("application/json")
                        .header("X-API-TOKEN", user.getToken()))
                .andExpectAll(status().isOk());
    }
}