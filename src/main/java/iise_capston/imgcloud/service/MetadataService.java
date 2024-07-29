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

    public long saveThingMetaData(Double fstop,Integer iso, Integer exposureTime, String realResolution, String resolution, String gpsLatitude,
                             String gpsLongitude, String whiteBalance, ThingImageMember thingImageMember, String size, Integer metaScore){
        MetadataMember metadataMember = new MetadataMember();
        if(iso==null){
            metadataMember.setIso(0);
        }
        else{
            metadataMember.setIso(iso);
        }
        metadataMember.setGpsLatitude(gpsLatitude);
        metadataMember.setGpsLongitude(gpsLongitude);
        if(fstop==null){
            metadataMember.setFStop(0);
        }
        else{
            metadataMember.setFStop(fstop);
        }
        if(exposureTime==null){
            metadataMember.setExposureTime(0);
        }
        else{
            metadataMember.setExposureTime(exposureTime);
        }
        metadataMember.setResolution(resolution);
        metadataMember.setRealResolution(realResolution);
        metadataMember.setWhiteBalance(whiteBalance);
        metadataMember.setSize(size);
        metadataMember.setMetaScore(metaScore);

        ThingImageMember thingSave = thingImageMemberRepository.findById(thingImageMember.getThingId()).get();

        metadataMember.setThingId(thingSave);

        metadataMemberRepository.save(metadataMember);

        return metadataMember.getMetaDataId();
    }
    public long savePeopleMetaData(Double fstop,Integer iso, Integer exposureTime, String realResolution, String resolution, String gpsLatitude,
                             String gpsLongitude, String whiteBalance, PeopleImageMember peopleImageMember, String size, Integer metaScore){
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
        metadataMember.setMetaScore(metaScore);

        PeopleImageMember peopleImage = peopleImageMemberRepository.findById(peopleImageMember.getPeopleId()).get();

        metadataMember.setPeopleId(peopleImage);

        metadataMemberRepository.save(metadataMember);

        return metadataMember.getMetaDataId();
    }

}
