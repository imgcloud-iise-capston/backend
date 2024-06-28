package iise_capston.imgcloud.service;

import com.amazonaws.services.s3.AmazonS3Client;
import iise_capston.imgcloud.domain.repository.BrisqueMemberRepository;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.BrisqueMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteFileService {
    private final ThingImageMemberRepository thingImageMemberRepository;
    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final BrisqueMemberRepository brisqueMemberRepository;

    private String iniBucketName = "imgcloud-iise";
    String bucketNamePerson =iniBucketName+"/person";
    String bucketNameThing = iniBucketName+"/thing";

    private final AmazonS3Client amazonS3Client;
    public void deleteThingImages(List<Long> id){
        for(int i=0;i<id.size();i++){
            deleteThingImage(id.get(i));
        }
    }
    //연결된 s3, brisquescore 다 삭제
    public void deleteThingImage(Long id){
        Optional<ThingImageMember> thingImageMember = thingImageMemberRepository.findById(id);
        if (thingImageMember.isPresent()) {
            String key = thingImageMember.get().getImageKey();
            String smallKey = thingImageMember.get().getSmallImageKey();
            thingImageMemberRepository.delete(thingImageMember.get());
            amazonS3Client.deleteObject(bucketNameThing, key);
            amazonS3Client.deleteObject(bucketNameThing,smallKey);
            Optional<BrisqueMember> brisqueMember = brisqueMemberRepository.findBythingId(thingImageMember.get());
            if(brisqueMember.isPresent()) {
                brisqueMemberRepository.delete(brisqueMember.get());
            }
            else{
                System.out.println("No brisqueMember");
            }
        } else {
            System.out.println("ThingImageMember with ID " + id + " not found");
        }
    }

    public void deletePeopleImages(List<Long> id){
        for(int i=0;i<id.size();i++){
            deletePeopleImage(id.get(i));
        }
    }
    //연결된 s3, brisquescore 다 삭제
    public void deletePeopleImage(Long id){
        Optional<PeopleImageMember> peopleImageMember = peopleImageMemberRepository.findById(id);
        if (peopleImageMember.isPresent()) {
            String key = peopleImageMember.get().getImageKey();
            String smallKey = peopleImageMember.get().getSmallImageKey();
            peopleImageMemberRepository.delete(peopleImageMember.get());
            amazonS3Client.deleteObject(bucketNamePerson, key);
            amazonS3Client.deleteObject(bucketNamePerson,smallKey);
            Optional<BrisqueMember> brisqueMember = brisqueMemberRepository.findBypeopleId(peopleImageMember.get());
            if(brisqueMember.isPresent()) {
                brisqueMemberRepository.delete(brisqueMember.get());
            }
            else{
                System.out.println("No brisqueMember");
            }
        } else {
            System.out.println("PeopleImageMember with ID " + id + " not found");
        }
    }
}
