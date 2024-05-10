package dev.app.artist.api.service.impl;

import dev.app.artist.api.dto.ApiResponse;
import dev.app.artist.api.entity.Artist;
import dev.app.artist.api.service.ArtistService;
import io.micrometer.observation.annotation.Observed;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

/**
 * @author Anish Panthi
 */
@Service
@Log4j2
@Observed(name = "artist_repository")
public class ArtistServiceImpl implements ArtistService {

  private final JdbcClient jdbcClient;

  public ArtistServiceImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Observed(contextualName = "findOne")
  @Override
  public Optional<Artist> findOne(Long id) {
    log.info("Fetching artist with id: {}", id);
    return jdbcClient
        .sql("SELECT id,name,genre,origin FROM artist WHERE id = :id")
        .param("id", id)
        .query(Artist.class)
        .optional();
  }

  @Observed(contextualName = "save")
  @Override
  public ApiResponse create(Artist artist) {
    int result =
        jdbcClient
            .sql("INSERT INTO artist(name,genre,origin) values(?,?,?)")
            .params(List.of(artist.name(), artist.genre(), artist.origin()))
            .update();
    if (result == 1) {
      log.info("Artist created successfully");
      return new ApiResponse(LocalDateTime.now(), "Artist created successfully");
    } else {
      log.info("Failed to create artist");
      return new ApiResponse(LocalDateTime.now(), "Failed to create artist");
    }
  }

  @Observed(contextualName = "update")
  @Override
  public ApiResponse update(Artist artist, Long id) {
    int result =
        jdbcClient
            .sql("update artist set name = ?, genre = ?, origin = ? where id = ?")
            .params(List.of(artist.name(), artist.genre(), artist.origin(), id))
            .update();
    if (result > 0) {
      log.info("Artist updated successfully");
      return new ApiResponse(LocalDateTime.now(), "Artist updated successfully");
    } else {
      log.info("Failed to update artist");
      return new ApiResponse(LocalDateTime.now(), "Failed to update artist");
    }
  }

  @Observed(contextualName = "delete")
  @Override
  public ApiResponse delete(Long id) {
    log.info("Deleting artist with id: {}", id);
    Optional<Artist> findOneFirst = findOne(id);
    log.info("Artist found: {}", findOneFirst.isPresent());
    if (findOneFirst.isPresent()) {
      jdbcClient.sql("delete from artist where id = :id").param("id", id).update();
      log.info("Artist deleted successfully");
      return new ApiResponse(LocalDateTime.now(), "Artist deleted successfully");
    } else {
      log.info("Artist not available to delete");
      return new ApiResponse(LocalDateTime.now(), "Artist not available to delete");
    }
  }

  @Observed(contextualName = "findAll")
  @Override
  public List<Artist> findAll() {
    log.info("Fetching all artists");
    return jdbcClient.sql("SELECT id,name,genre,origin FROM artist").query(Artist.class).list();
  }
}
