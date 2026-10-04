package com.threadhub.controller;

import com.threadhub.config.SecurityConfig;
import com.threadhub.dto.UserCreateRequest;
import com.threadhub.dto.UserResponse;
import com.threadhub.exception.DuplicateEmailException;
import com.threadhub.exception.DuplicateUsernameException;
import com.threadhub.exception.GlobalExceptionHandler;
import com.threadhub.exception.UserNotFoundException;
import com.threadhub.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = UserResponse.builder()
                .id(1L)
                .username("jane_doe")
                .email("jane@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/users - Should create user and return 201 Created")
    void shouldCreateUser() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("jane_doe")
                .email("jane@example.com")
                .password("password123")
                .build();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("jane_doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("POST /api/users - Should return 400 Bad Request when validation fails")
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("ab") // Too short
                .email("invalid-email") // Bad email format
                .password("") // Blank password
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("POST /api/users - Should return 409 Conflict when username is duplicate")
    void shouldReturnConflictOnDuplicateUsername() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("jane_doe")
                .email("jane@example.com")
                .password("password123")
                .build();

        when(userService.createUser(any(UserCreateRequest.class)))
                .thenThrow(new DuplicateUsernameException("Username already taken: jane_doe"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Username already taken: jane_doe"));
    }

    @Test
    @DisplayName("POST /api/users - Should return 409 Conflict when email is duplicate")
    void shouldReturnConflictOnDuplicateEmail() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("jane_doe")
                .email("jane@example.com")
                .password("password123")
                .build();

        when(userService.createUser(any(UserCreateRequest.class)))
                .thenThrow(new DuplicateEmailException("Email already registered: jane@example.com"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Email already registered: jane@example.com"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return user when found")
    void shouldGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("jane_doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return 404 Not Found when user does not exist")
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 99"));
    }

    @Test
    @DisplayName("GET /api/users/username/{username} - Should return user by username")
    void shouldGetUserByUsername() throws Exception {
        when(userService.getUserByUsername("jane_doe")).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/users/username/jane_doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("jane_doe"));
    }

    @Test
    @DisplayName("GET /api/users/email/{email} - Should return user by email")
    void shouldGetUserByEmail() throws Exception {
        when(userService.getUserByEmail("jane@example.com")).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/users/email/jane@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }
}
