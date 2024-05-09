package dev.app.artist.api.controller;

import dev.app.artist.api.dto.ApiResponse;
import dev.app.artist.api.entity.Artist;
import dev.app.artist.api.service.ArtistService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anish Panthi
 */
@RestController
@RequestMapping("/api/artists")
@Log4j2
public class ArtistController {

  private static final String CLIENT_REGISTRATION_ID = "azure-entra";

  private final ArtistService artistService;

  public ArtistController(ArtistService artistService) {
    this.artistService = artistService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Artist> getArtist(@PathVariable Long id) {
    return new ResponseEntity<>(
        artistService.findOne(id).orElse(new Artist(0L, "Unknown", "Unknown", "Unknown")),
        HttpStatus.OK);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Artist> getArtists(
      @RegisteredOAuth2AuthorizedClient(CLIENT_REGISTRATION_ID) OAuth2AuthorizedClient client) {
    log.info("Access Token: {}", client.getAccessToken().getTokenValue());
    log.info("Refresh Token: {}", client.getRefreshToken() == null
        ? "Refresh token not received"
        : client.getRefreshToken().getTokenValue());
    return artistService.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse createArtist(@RequestBody Artist artist) {
    return artistService.create(artist);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse updateArtist(@RequestBody Artist artist, @PathVariable Long id) {
    return artistService.update(artist, id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse deleteArtist(@PathVariable Long id) {
    return artistService.delete(id);
  }

}
