package iise_capston.imgcloud.service;

import iise_capston.imgcloud.domain.repository.MetadataMemberRepository;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.MetadataMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MetadataService {

    private final MetadataMemberRepository metadataMemberRepository;
    private final ThingImageMemberRepository thingImageMemberRepository;
    private final PeopleImageMemberRepository peopleImageMemberRepository;

    public void saveThingMetaData(Double fstop,Integer iso, Integer exposureTime, String realResolution, String resolution, String gpsLatitude,
                             String gpsLongitude, String whiteBalance, String size,ThingImageMember thingImageMember){
        MetadataMember metadataMember = new MetadataMember();
        metadataMember.setIso(iso);
        metadataMember.setGpsLatitude(gpsLatitude);
        metadataMember.setGpsLongitude(gpsLongitude);
        metadataMember.setFStop(fstop);
        metadataMember.setExposureTime(exposureTime);
        metadataMember.setResolution(resolution);
        metadataMember.setRealResolution(realResolution);
        metadataMember.setWhiteBalance(whiteBalance);
        metadataMember.setSize(size);

        ThingImageMember thingSave = thingImageMemberRepository.findById(thingImageMember.getThingId()).get();

        metadataMember.setThingId(thingSave);

        metadataMemberRepository.save(metadataMember);
    }
    public void savePeopleMetaData(Double fstop, Integer iso, Integer exposureTime, String realResolution, String resolution, String gpsLatitude,
                                   String gpsLongitude, String whiteBalance, String size, PeopleImageMember peopleImageMember){
        MetadataMember metadataMember = new MetadataMember();
        metadataMember.setIso(iso);
        metadataMember.setGpsLatitude(gpsLatitude);
        metadataMember.setGpsLongitude(gpsLongitude);
        metadataMember.setFStop(fstop);
        metadataMember.setExposureTime(exposureTime);
        metadataMember.setResolution(resolution);
        metadataMember.setRealResolution(realResolution);
        metadataMember.setWhiteBalance(whiteBalance);
        metadataMember.setSize(size);

        PeopleImageMember peopleImage = peopleImageMemberRepository.findById(peopleImageMember.getPeopleId()).get();

        metadataMember.setPeopleId(peopleImage);

        metadataMemberRepository.save(metadataMember);
    }

}
