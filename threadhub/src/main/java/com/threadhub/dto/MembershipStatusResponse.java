package com.threadhub.dto;

import com.threadhub.model.CommunityMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipStatusResponse {

    private boolean isMember;
    private CommunityMemberRole role;
}
