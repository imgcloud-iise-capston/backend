package iise_capston.imgcloud.member;

import com.siot.IamportRestClient.response.Payment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class OrderMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    private Long price;
    private String itemName;
    private String orderUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private OauthMember oauthMember;
}
