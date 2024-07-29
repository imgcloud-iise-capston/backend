package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.dto.PeopleImageUploadDto;
import iise_capston.imgcloud.domain.dto.ThingImageUploadDto;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.BrisqueMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import iise_capston.imgcloud.service.BrisqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ImageLoadController {

    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final ThingImageMemberRepository thingImageMemberRepository;
    private final BrisqueService brisqueService;

    @GetMapping("load/people")
    public List<PeopleImageUploadDto> getPeopleImages(@RequestParam Long userId) {
        // 주어진 userId로 PeopleImageMember를 조회
        List<PeopleImageMember> peopleImages = peopleImageMemberRepository.findByUserPeopleId_UserId(userId);

        return peopleImages.stream().map(image -> {
            double brisqueScore = brisqueService.getBrisqueByPeopleImage(image)
                    .map(BrisqueMember::getBrisqueScore)
                    .orElse(0.0);

            return new PeopleImageUploadDto(
                    image.getPeopleId(),
                    image.getSmallImageUrl(),
                    image.getImageTitle(),
                    image.getImageUrl(),
                    brisqueScore
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("load/thing")
    public List<ThingImageUploadDto> getThingImages(@RequestParam Long userId) {
        // 주어진 userId로 ThingImageMember를 조회
        List<ThingImageMember> thingImages = thingImageMemberRepository.findByUserThingId_UserId(userId);

        // DTO로 변환하여 반환
        return thingImages.stream().map(image -> {
            double brisqueScore = brisqueService.getBrisqueByThingImage(image)
                    .map(BrisqueMember::getBrisqueScore)
                    .orElse(0.0);

            return new ThingImageUploadDto(
                    image.getThingId(),
                    image.getSmallImageUrl(),
                    image.getImageTitle(),
                    image.getImageUrl(),
                    brisqueScore
            );
        }).collect(Collectors.toList());
    }


}