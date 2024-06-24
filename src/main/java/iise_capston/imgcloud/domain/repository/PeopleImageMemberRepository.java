package iise_capston.imgcloud.domain.repository;

import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeopleImageMemberRepository extends JpaRepository<PeopleImageMember,Long> {
    Optional<PeopleImageMember> findByimageTitle(String title);
}
