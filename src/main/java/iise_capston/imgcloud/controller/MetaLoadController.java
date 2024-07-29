package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.dto.PeopleMetadataDto;
import iise_capston.imgcloud.domain.dto.ThingMetadataDto;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.PeopleMetadataMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingMetadataMemberRepository;
import iise_capston.imgcloud.member.MetadataMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MetaLoadController {
    private PeopleMetadataDto peopleMetadataDto;
    private ThingMetadataDto thingMetadataDto;

    private ThingMetadataMemberRepository thingMetadataMemberRepository;
    private PeopleMetadataMemberRepository peopleMetadataMemberRepository;
    private ThingImageMemberRepository thingImageMemberRepository;
    private PeopleImageMemberRepository peopleImageMemberRepository;

    @GetMapping("load/meta/thing")
    public MetadataMember getThingMeta(@RequestParam Long thingId){
        ThingImageMember thingImageMember = thingImageMemberRepository.findById(thingId).get();
        MetadataMember thingMetadata = thingMetadataMemberRepository.findBythingId(thingImageMember).get();
        return thingMetadata;
    }

    @GetMapping("load/meta/people")
    public MetadataMember getPeopleMeta(@RequestParam Long peopleId){
        PeopleImageMember peopleImageMember = peopleImageMemberRepository.findById(peopleId).get();
        MetadataMember peopleMetadata = peopleMetadataMemberRepository.findBypeopleId(peopleImageMember).get();
        return peopleMetadata;
    }
}
