package iise_capston.imgcloud.service;

import iise_capston.imgcloud.ImgcloudApplication;
import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.oauth.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    private static final Logger logger = LoggerFactory.getLogger(OauthService.class);

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType){
        logger.info(authCodeRequestUrlProviderComposite.provide(oauthServerType));

        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }


    public OauthMember login(OauthServerType oauthServerType, String authCode){
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType,authCode);
        logger.info("email: "+oauthMember.email());
        OauthMember saved = oauthMemberRepository.findByemail(oauthMember.email())
                .orElseGet(()-> oauthMemberRepository.save(oauthMember));

        return saved;
    }
}
