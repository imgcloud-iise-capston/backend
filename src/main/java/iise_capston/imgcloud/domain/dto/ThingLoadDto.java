package iise_capston.imgcloud.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThingLoadDto {
    private Integer ISO;
    private Double FStop;
    private Integer ExposureTime;
    private String GPSLatitude;
    private String GPSLongitude;
    private String RealResolution;
    private String Resolution;
    private String WhiteBalance;
    private String size;
    private Integer metaScore;

}
