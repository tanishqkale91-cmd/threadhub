package com.threadhub.controller;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.threadhub.config.SecurityConfig;
import com.threadhub.dto.*;
import com.threadhub.exception.AlreadyMemberException;
import com.threadhub.exception.CommunityNotFoundException;
import com.threadhub.exception.GlobalExceptionHandler;
import com.threadhub.model.CommunityMemberRole;
import com.threadhub.model.User;
import com.threadhub.security.JwtService;
import com.threadhub.service.CommunityMemberService;
import com.threadhub.service.CommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = {
        "JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
})
class CommunityControllerTest {

    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommunityService communityService;

    @MockitoBean
    private CommunityMemberService communityMemberService;

    private String validJwtToken;
    private CommunityResponse sampleCommunityResponse;
    private CommunityMemberResponse sampleMemberResponse;

    @BeforeEach
    void setUp() {
        SecretKey secretKey = new SecretKeySpec(TEST_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JWKSource<SecurityContext> jwks = new ImmutableSecret<>(secretKey);
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwks);
        JwtService jwtService = new JwtService(jwtEncoder, 86400000L);

        User user = User.builder().id(1L).username("testuser").email("test@example.com").build();
        validJwtToken = jwtService.generateToken(user);

        UserResponse ownerResponse = UserResponse.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        sampleCommunityResponse = CommunityResponse.builder()
                .id(10L)
                .name("spring")
                .description("Spring boot discussion")
                .owner(ownerResponse)
                .memberCount(1L)
                .createdAt(LocalDateTime.now())
                .build();

        sampleMemberResponse = CommunityMemberResponse.builder()
                .userId(1L)
                .username("testuser")
                .role(CommunityMemberRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/communities - Should reject unauthenticated user with 401 Unauthorized")
    void shouldRejectUnauthenticatedCommunityCreation() throws Exception {
        String jsonRequest = """
                {
                    "name": "spring",
                    "description": "Spring framework"
                }
                """;

        mockMvc.perform(post("/api/communities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/communities - Should allow authenticated user to create community (201 Created)")
    void shouldAllowAuthenticatedUserToCreateCommunity() throws Exception {
        when(communityService.createCommunity(any(CommunityCreateRequest.class))).thenReturn(sampleCommunityResponse);

        String jsonRequest = """
                {
                    "name": "spring",
                    "description": "Spring framework"
                }
                """;

        mockMvc.perform(post("/api/communities")
                        .header("Authorization", "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("spring"))
                .andExpect(jsonPath("$.owner.username").value("testuser"));
    }

    @Test
    @DisplayName("POST /api/communities - Should return 400 Bad Request when validation fails")
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        String invalidJsonRequest = """
                {
                    "name": "ab",
                    "description": "Too short name"
                }
                """;

        mockMvc.perform(post("/api/communities")
                        .header("Authorization", "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("GET /api/communities - Should permit public access to list communities")
    void shouldAllowPublicCommunityListing() throws Exception {
        when(communityService.getAllCommunities()).thenReturn(List.of(sampleCommunityResponse));

        mockMvc.perform(get("/api/communities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("spring"));
    }

    @Test
    @DisplayName("GET /api/communities/{id} - Should return 404 Not Found when community does not exist")
    void shouldReturnNotFoundWhenCommunityMissing() throws Exception {
        when(communityService.getCommunityById(99L)).thenThrow(new CommunityNotFoundException("Community not found with id: 99"));

        mockMvc.perform(get("/api/communities/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("POST /api/communities/{id}/join - Should reject unauthenticated user with 401 Unauthorized")
    void shouldRejectUnauthenticatedJoin() throws Exception {
        mockMvc.perform(post("/api/communities/10/join"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/communities/{id}/join - Should allow authenticated user to join community")
    void shouldAllowAuthenticatedUserToJoin() throws Exception {
        when(communityMemberService.joinCommunity(10L)).thenReturn(sampleMemberResponse);

        mockMvc.perform(post("/api/communities/10/join")
                        .header("Authorization", "Bearer " + validJwtToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.role").value("MEMBER"));
    }

    @Test
    @DisplayName("POST /api/communities/{id}/join - Should return 409 Conflict when user is already a member")
    void shouldReturnConflictWhenAlreadyMember() throws Exception {
        when(communityMemberService.joinCommunity(10L)).thenThrow(new AlreadyMemberException("User is already a member"));

        mockMvc.perform(post("/api/communities/10/join")
                        .header("Authorization", "Bearer " + validJwtToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("GET /api/communities/{id}/members - Should permit public access to member listing")
    void shouldAllowPublicMemberListing() throws Exception {
        when(communityMemberService.getCommunityMembers(10L)).thenReturn(List.of(sampleMemberResponse));

        mockMvc.perform(get("/api/communities/10/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].role").value("MEMBER"));
    }
}
