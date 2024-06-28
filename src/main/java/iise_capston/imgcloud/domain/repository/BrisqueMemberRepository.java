package iise_capston.imgcloud.domain.repository;

import iise_capston.imgcloud.member.BrisqueMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrisqueMemberRepository extends JpaRepository<BrisqueMember,Long> {
    Optional<BrisqueMember> findBythingId(ThingImageMember thingImageMember);
    Optional<BrisqueMember> findBypeopleId(PeopleImageMember peopleImageMember);
}
