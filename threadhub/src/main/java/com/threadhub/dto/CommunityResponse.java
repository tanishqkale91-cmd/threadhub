package com.threadhub.dto;

import com.threadhub.model.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityResponse {

    private Long id;
    private String name;
    private String description;
    private UserResponse owner;
    private Long memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommunityResponse fromEntity(Community community, long memberCount) {
        if (community == null) {
            return null;
        }
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .owner(UserResponse.fromEntity(community.getOwner()))
                .memberCount(memberCount)
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .build();
    }
}
