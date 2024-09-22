package iise_capston.imgcloud.oauth;

import iise_capston.imgcloud.member.OauthMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthMemberRepository extends JpaRepository<OauthMember,Long> {
    Optional<OauthMember> findByemail(String email);
    Optional<OauthMember> findByUserId(Long userId);


}
