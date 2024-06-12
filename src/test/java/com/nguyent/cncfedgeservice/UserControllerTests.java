package com.nguyent.cncfedgeservice;

import com.nguyent.cncfedgeservice.config.SecurityConfig;
import com.nguyent.cncfedgeservice.user.User;
import com.nguyent.cncfedgeservice.user.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenUnauthenticatedThenRedirectsToLoginPage() {
        webTestClient.get()
                .uri("/profile")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedThenReturnCurrentUser() {
        var expectedUser = new User(
                "michaeljackson",
                "Michael",
                "Jackson",
                "Male",
                "29/08/2958",
                "example@gmail.com",
                true,
                List.of("user")
        );
        webTestClient
                .mutateWith(configureOidcLoginMutator(expectedUser))
                .get()
                .uri("/profile")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(User.class)
                .value(user -> {
                    assertThat(user).isEqualTo(expectedUser);
                });
    }


    private SecurityMockServerConfigurers.OidcLoginMutator configureOidcLoginMutator(User expectdUser) {
        return SecurityMockServerConfigurers.mockOidcLogin()
                .idToken(builder -> builder
                        .claim(StandardClaimNames.PREFERRED_USERNAME, expectdUser.username())
                        .claim(StandardClaimNames.GIVEN_NAME, expectdUser.firstName())
                        .claim(StandardClaimNames.FAMILY_NAME, expectdUser.lastName())
                        .claim(StandardClaimNames.BIRTHDATE, expectdUser.birthdate())
                        .claim(StandardClaimNames.GENDER, expectdUser.gender())
                        .claim(StandardClaimNames.EMAIL, expectdUser.email())
                        .claim(StandardClaimNames.EMAIL_VERIFIED, expectdUser.emailVerified())
                        .claim("roles", expectdUser.roles())
                );
    }

}
