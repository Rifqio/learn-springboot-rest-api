package rifqio.learningrestfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import rifqio.learningrestfulapi.dto.request.contact.CreateContactRequestDTO;
import rifqio.learningrestfulapi.dto.response.WebResponse;
import rifqio.learningrestfulapi.entity.User;
import rifqio.learningrestfulapi.repository.ContactRepository;
import rifqio.learningrestfulapi.repository.UserRepository;
import utils.BCrypt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    String baseUrl = "/api/contact";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void createUser() {
        User user = new User();
        user.setUsername("cobalagi");
        user.setName("cobalagi");
        user.setPassword(BCrypt.hashpw("cobalagi", BCrypt.gensalt()));
        user.setToken("cobalagi");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequestDTO request = new CreateContactRequestDTO();
        request.setFirstName("");
        request.setEmail("bukanemail");

        mockMvc.perform(post(baseUrl)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "cobalagi")
                )
//                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    WebResponse<String> response = objectMapper.readValue(content, new TypeReference<WebResponse<String>>() {
                    });
                    assertEquals("Contact failed to create", response.getMessage());
                    assertNull(response.getData());
                    assertNotNull(response.getErrors());
                });

    }

    @Test
    void createContactUnauthorized() throws Exception {
        mockMvc.perform(post(baseUrl))
                .andExpect(status().isUnauthorized());
    }
}