package dev.app.artist.api.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anish Panthi
 */
@RestController
@Log4j2
@Observed(name = "home_controller", contextualName = "home")
public class HomeController {

  private static final String CLIENT_REGISTRATION_ID = "azure-entra";

  private final OAuth2AuthorizedClientRepository authorizedClientRepository;

  public HomeController(OAuth2AuthorizedClientRepository authorizedClientRepository) {
    this.authorizedClientRepository = authorizedClientRepository;
  }

  @GetMapping
  public String home() {
    log.info("Started landing page...");
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof OAuth2User) {
      OAuth2AuthorizedClient client =
          authorizedClientRepository.loadAuthorizedClient(
              CLIENT_REGISTRATION_ID, SecurityContextHolder.getContext().getAuthentication(), null);
      log.info("Access Token: {}", client.getAccessToken().getTokenValue());
      log.info(
          "Refresh Token: {}",
          client.getRefreshToken() == null
              ? "Refresh token not received"
              : client.getRefreshToken().getTokenValue());
    } else if (principal instanceof Jwt jwt) {
      log.info("API User: {}", jwt.getClaims());
    } else {
      log.info("Unknown User: {}", principal);
    }
    return "Welcome to Artist API ??🙏🏼🎨🎤🎸🎹🎻🥁🎷🎺🎶🎵🎼🎧";
  }
}
