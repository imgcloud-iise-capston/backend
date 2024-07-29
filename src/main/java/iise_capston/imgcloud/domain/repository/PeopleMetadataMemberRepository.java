package iise_capston.imgcloud.domain.repository;

import iise_capston.imgcloud.member.MetadataMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeopleMetadataMemberRepository extends JpaRepository<MetadataMember, Long> {
    Optional<MetadataMember> findBypeopleId(PeopleImageMember peopleImageMember);
}
