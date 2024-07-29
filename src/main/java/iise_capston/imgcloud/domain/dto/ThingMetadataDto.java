package iise_capston.imgcloud.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThingMetadataDto {
    private List<Integer> ISO;
    private List<Double> FStop;
    private List<Integer> ExposureTime;
    private List<String> GPSLatitude;
    private List<String> GPSLongitude;
    private List<String> RealResolution;
    private List<String> Resolution;
    private List<String> WhiteBalance;
    private List<String> size;
    private List<Integer> metaScore;
}
