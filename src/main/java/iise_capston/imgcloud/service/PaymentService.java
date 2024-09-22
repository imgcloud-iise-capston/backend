package iise_capston.imgcloud.service;

import com.siot.IamportRestClient.IamportClient;
import iise_capston.imgcloud.domain.repository.OrderMemberRepository;
import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.oauth.OauthMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrderMemberRepository orderMemberRepository;
    private final OauthMemberRepository oauthMemberRepository;

    public void payment(OauthMember oauthMember){

    }

}
