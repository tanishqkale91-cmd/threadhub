package com.threadhub.service;

import com.threadhub.dto.CommunityCreateRequest;
import com.threadhub.dto.CommunityResponse;
import com.threadhub.exception.CommunityNotFoundException;
import com.threadhub.exception.DuplicateCommunityNameException;
import com.threadhub.model.Community;
import com.threadhub.model.CommunityMember;
import com.threadhub.model.CommunityMemberRole;
import com.threadhub.model.User;
import com.threadhub.repository.CommunityMemberRepository;
import com.threadhub.repository.CommunityRepository;
import com.threadhub.repository.UserRepository;
import com.threadhub.security.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private CommunityMemberRepository communityMemberRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommunityService communityService;

    private MockedStatic<SecurityUtils> securityUtilsMock;
    private User testUser;
    private Community sampleCommunity;

    @BeforeEach
    void setUp() {
        securityUtilsMock = mockStatic(SecurityUtils.class);

        testUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .build();

        sampleCommunity = Community.builder()
                .id(10L)
                .name("java")
                .description("Java programming")
                .owner(testUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        securityUtilsMock.close();
    }

    @Test
    @DisplayName("Authenticated user should create community and automatically become OWNER member")
    void shouldCreateCommunityAndMakeCreatorOwner() {
        securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(Optional.of(1L));

        CommunityCreateRequest request = CommunityCreateRequest.builder()
                .name("java")
                .description("Java programming")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(communityRepository.existsByName("java")).thenReturn(false);
        when(communityRepository.save(any(Community.class))).thenReturn(sampleCommunity);

        CommunityResponse response = communityService.createCommunity(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("java");
        assertThat(response.getOwner().getUsername()).isEqualTo("john");
        assertThat(response.getMemberCount()).isEqualTo(1L);

        ArgumentCaptor<CommunityMember> memberCaptor = ArgumentCaptor.forClass(CommunityMember.class);
        verify(communityMemberRepository, times(1)).save(memberCaptor.capture());

        CommunityMember savedMember = memberCaptor.getValue();
        assertThat(savedMember.getRole()).isEqualTo(CommunityMemberRole.OWNER);
        assertThat(savedMember.getUser().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw DuplicateCommunityNameException when community name exists")
    void shouldThrowExceptionOnDuplicateCommunityName() {
        securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(Optional.of(1L));

        CommunityCreateRequest request = CommunityCreateRequest.builder()
                .name("java")
                .description("Java programming")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(communityRepository.existsByName("java")).thenReturn(true);

        assertThatThrownBy(() -> communityService.createCommunity(request))
                .isInstanceOf(DuplicateCommunityNameException.class)
                .hasMessageContaining("Community name already taken");

        verify(communityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get community by ID")
    void shouldGetCommunityById() {
        when(communityRepository.findById(10L)).thenReturn(Optional.of(sampleCommunity));
        when(communityMemberRepository.countByCommunityId(10L)).thenReturn(5L);

        CommunityResponse response = communityService.getCommunityById(10L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getMemberCount()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should throw CommunityNotFoundException when ID not found")
    void shouldThrowExceptionWhenIdNotFound() {
        when(communityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> communityService.getCommunityById(99L))
                .isInstanceOf(CommunityNotFoundException.class);
    }

    @Test
    @DisplayName("Should get community by name")
    void shouldGetCommunityByName() {
        when(communityRepository.findByName("java")).thenReturn(Optional.of(sampleCommunity));
        when(communityMemberRepository.countByCommunityId(10L)).thenReturn(3L);

        CommunityResponse response = communityService.getCommunityByName("java");

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("java");
        assertThat(response.getMemberCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should list all communities")
    void shouldListCommunities() {
        when(communityRepository.findAll()).thenReturn(List.of(sampleCommunity));
        when(communityMemberRepository.countByCommunityId(10L)).thenReturn(1L);

        List<CommunityResponse> list = communityService.getAllCommunities();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo("java");
    }
}
