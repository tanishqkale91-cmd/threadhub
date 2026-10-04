package com.threadhub.service;

import com.threadhub.dto.UserCreateRequest;
import com.threadhub.dto.UserResponse;
import com.threadhub.exception.DuplicateEmailException;
import com.threadhub.exception.DuplicateUsernameException;
import com.threadhub.exception.UserNotFoundException;
import com.threadhub.model.User;
import com.threadhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UserCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = UserCreateRequest.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("secret123")
                .build();

        sampleUser = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .password("encoded_secret123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should successfully create a user and encode raw password")
    void shouldCreateUser() {
        when(userRepository.existsByUsername(createRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded_secret123");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = userService.createUser(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("john_doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");

        verify(passwordEncoder, times(1)).encode("secret123");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo("encoded_secret123");
        assertThat(savedUser.getPassword()).isNotEqualTo("secret123");
    }

    @Test
    @DisplayName("Should throw DuplicateUsernameException when username already exists")
    void shouldThrowExceptionWhenDuplicateUsername() {
        when(userRepository.existsByUsername(createRequest.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(DuplicateUsernameException.class)
                .hasMessageContaining("Username already taken");

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void shouldThrowExceptionWhenDuplicateEmail() {
        when(userRepository.existsByUsername(createRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email already registered");

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should get user by ID")
    void shouldGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("john_doe");
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user ID not found")
    void shouldThrowUserNotFoundExceptionWhenIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with id: 99");
    }

    @Test
    @DisplayName("Should get user by username")
    void shouldGetUserByUsername() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserByUsername("john_doe");

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("john_doe");
    }

    @Test
    @DisplayName("Should get user by email")
    void shouldGetUserByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserByEmail("john@example.com");

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("john@example.com");
    }
}
