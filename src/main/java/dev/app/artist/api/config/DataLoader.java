package dev.app.artist.api.config;

import dev.app.artist.api.entity.Artist;
import dev.app.artist.api.service.ArtistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author Anish Panthi
 */
@Configuration
public class DataLoader implements CommandLineRunner {

  private final ArtistService artistService;

  public DataLoader(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Override
  public void run(String... args) throws Exception {
    if (artistService.findAll().isEmpty()) {
      artistService.create(new Artist(null, "The Beatles", "Rock", "Liverpool"));
    }
  }
}
