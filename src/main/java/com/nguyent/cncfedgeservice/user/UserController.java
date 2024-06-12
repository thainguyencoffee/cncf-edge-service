package com.nguyent.cncfedgeservice.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class UserController {

    @GetMapping("/profile")
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        var user = new User(
                oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getGender(),
                oidcUser.getBirthdate(),
                oidcUser.getEmail(),
                oidcUser.getEmailVerified(),
                oidcUser.getClaimAsStringList("roles")
        );
        return Mono.just(user);
    }

}
