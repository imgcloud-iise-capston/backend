package iise_capston.imgcloud.domain.dto;

import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThingImageUploadDto {
    private List<MultipartFile> bigImageFiles = new ArrayList<>();
    private List<MultipartFile> smallImageFiles = new ArrayList<>();
    private ThingImageMember thingImageMember;
    private List<String> imageTitle = new ArrayList<>();
    private OauthMember oauthMember;
    private List<Integer> brisqueScore = new ArrayList<>();

    public ThingImageUploadDto(Long thingId, String smallImageUrl, String imageTitle, String imageUrl, double brisqueScore) {
        this.thingId = thingId;
        this.smallImageUrl = smallImageUrl;
        this.imageTitle = new ArrayList<>();
        this.imageTitle.add(imageTitle);
        this.brisqueScore = new ArrayList<>();
        this.brisqueScore.add((int) brisqueScore);
        this.imageUrl = imageUrl;
    }

    private Long thingId;
    private String smallImageUrl;
    private String imageUrl;
}
