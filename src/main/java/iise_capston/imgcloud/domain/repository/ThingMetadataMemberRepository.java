package iise_capston.imgcloud.domain.repository;

import iise_capston.imgcloud.member.MetadataMember;
import iise_capston.imgcloud.member.ThingImageMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThingMetadataMemberRepository extends JpaRepository<MetadataMember,Long> {
    Optional<MetadataMember> findBythingId(ThingImageMember thingImageMember);
}
