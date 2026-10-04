package com.threadhub.dto;

import com.threadhub.model.CommunityMember;
import com.threadhub.model.CommunityMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityMemberResponse {

    private Long userId;
    private String username;
    private CommunityMemberRole role;
    private LocalDateTime joinedAt;

    public static CommunityMemberResponse fromEntity(CommunityMember member) {
        if (member == null) {
            return null;
        }
        return CommunityMemberResponse.builder()
                .userId(member.getUser().getId())
                .username(member.getUser().getUsername())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
