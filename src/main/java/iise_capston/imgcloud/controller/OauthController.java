package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.oauth.OauthServerType;
import iise_capston.imgcloud.service.OauthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
public class OauthController {

    private final OauthService oauthService;
    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

    @SneakyThrows
    @GetMapping("/oauth/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
            ){
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        logger.info(redirectUrl);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/oauth/login/{oauthServerType}")
    ResponseEntity<OauthMember> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ){
        OauthMember login = oauthService.login(oauthServerType, code);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("logout");
    }
}
