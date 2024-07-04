package iise_capston.imgcloud.service;

import com.amazonaws.services.s3.AmazonS3Client;
import iise_capston.imgcloud.domain.repository.BrisqueMemberRepository;
import iise_capston.imgcloud.domain.repository.MetadataMemberRepository;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.BrisqueMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteFileService {
    private final ThingImageMemberRepository thingImageMemberRepository;
    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final BrisqueMemberRepository brisqueMemberRepository;
    private final MetadataMemberRepository metadataMemberRepository;  // 메타데이터 리포지토리 추가 (외래키제약조건땜에)


    private String iniBucketName = "imgcloud-iise";
    String bucketNamePerson =iniBucketName+"/person";
    String bucketNameThing = iniBucketName+"/thing";

    private final AmazonS3Client amazonS3Client;

    @Transactional
    public void deleteThingImages(List<Long> id){
        for(int i=0;i<id.size();i++){
            deleteThingImage(id.get(i));
        }
    }
    //연결된 s3, brisquescore 다 삭제
    @Transactional
    public void deleteThingImage(Long id){
        Optional<ThingImageMember> thingImageMember = thingImageMemberRepository.findById(id);
        if (thingImageMember.isPresent()) {
            ThingImageMember member = thingImageMember.get();
            metadataMemberRepository.deleteByThingId(member); //메타데이터 삭제
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

    @Transactional
    public void deletePeopleImages(List<Long> id){
        for(int i=0;i<id.size();i++){
            deletePeopleImage(id.get(i));
        }
    }
    //연결된 s3, brisquescore 다 삭제
    @Transactional
    public void deletePeopleImage(Long id){
        Optional<PeopleImageMember> peopleImageMember = peopleImageMemberRepository.findById(id);
        if (peopleImageMember.isPresent()) {
            PeopleImageMember member = peopleImageMember.get();
            metadataMemberRepository.deleteByPeopleId(member); //메타데이터 삭제
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
