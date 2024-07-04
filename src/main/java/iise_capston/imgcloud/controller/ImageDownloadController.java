package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ImageDownloadController {

    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final ThingImageMemberRepository thingImageMemberRepository;

    @PostMapping("/download/people")
    public ResponseEntity<List<String>> downloadPeopleImages(@RequestBody List<Long> peopleIds) {
        List<String> imageUrls = peopleIds.stream()
                .map(peopleImageMemberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PeopleImageMember::getImageUrl)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageUrls);
    }

    @PostMapping("/download/thing")
    public ResponseEntity<List<String>> downloadThingImages(@RequestBody List<Long> thingIds) {
        List<String> imageUrls = thingIds.stream()
                .map(thingImageMemberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ThingImageMember::getImageUrl)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageUrls);
    }
}