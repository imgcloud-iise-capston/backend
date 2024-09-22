package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.oauth.OauthMemberRepository;
import iise_capston.imgcloud.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private static OauthMemberRepository oauthMemberRepository;
    private static PaymentService paymentService;

    @PostMapping("/payment/item/1")
    ResponseEntity<String> payment(
            @RequestPart("userId") Long userId
    ){
        OauthMember oauthMember = oauthMemberRepository.findByUserId(userId).get();

        paymentService.payment(oauthMember);

        return ResponseEntity.ok("paid");
    }

}
