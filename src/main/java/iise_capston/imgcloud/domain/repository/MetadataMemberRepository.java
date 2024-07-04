package iise_capston.imgcloud.domain.repository;

import iise_capston.imgcloud.member.MetadataMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataMemberRepository extends JpaRepository<MetadataMember,Long> {
    void deleteByPeopleId(PeopleImageMember peopleId);

    void deleteByThingId(ThingImageMember thingId);
}
