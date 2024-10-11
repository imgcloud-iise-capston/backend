package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import iise_capston.imgcloud.service.downloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ImageDownloadController {

    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final ThingImageMemberRepository thingImageMemberRepository;
    private final downloadService downloadService;

    @PostMapping("/download/people")
    public ResponseEntity<List<String>> downloadPeopleImages(@RequestBody List<Long> peopleIds) {

        List<String> keys = new ArrayList<>();
        for(long id : peopleIds){
            PeopleImageMember peopleImageMember = peopleImageMemberRepository.findById(id).get();
            String key = peopleImageMember.getImageKey();
            System.out.println(key);
            keys.add(key);
        }

        List<String> urls = new ArrayList<>();
        for(String k : keys){
            urls.add(downloadService.presignedDownloadUrl("person", k));
            System.out.println(urls.get(0));
        }
//        List<String> imageUrls = peopleIds.stream()
//                .map(peopleImageMemberRepository::findById)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .map(PeopleImageMember::getImageUrl)
//                .collect(Collectors.toList());

        return ResponseEntity.ok(urls);
    }

    @PostMapping("/download/thing")
    public ResponseEntity<List<String>> downloadThingImages(@RequestBody List<Long> thingIds) {
        List<String> keys = new ArrayList<>();
        for(long id : thingIds){
            ThingImageMember thingImageMember = thingImageMemberRepository.findById(id).get();
            String key = thingImageMember.getImageKey();
            System.out.println(key);
            keys.add(key);
        }

        List<String> urls = new ArrayList<>();
        for(String k : keys){
            urls.add(downloadService.presignedDownloadUrl("thing", k));
            System.out.println(urls.get(0));
        }
        return ResponseEntity.ok(urls);
    }
}