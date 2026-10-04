package com.threadhub.repository;

import com.threadhub.model.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

    Optional<CommunityMember> findByCommunityIdAndUserId(Long communityId, Long userId);

    boolean existsByCommunityIdAndUserId(Long communityId, Long userId);

    void deleteByCommunityIdAndUserId(Long communityId, Long userId);

    List<CommunityMember> findByCommunityId(Long communityId);

    long countByCommunityId(Long communityId);
}
