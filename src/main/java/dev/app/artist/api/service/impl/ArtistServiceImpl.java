package dev.app.artist.api.service.impl;

import dev.app.artist.api.dto.ApiResponse;
import dev.app.artist.api.entity.Artist;
import dev.app.artist.api.service.ArtistService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

/**
 * @author Anish Panthi
 */
@Service
public class ArtistServiceImpl implements ArtistService {

  private final JdbcClient jdbcClient;

  public ArtistServiceImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public Optional<Artist> findOne(Long id) {
    return jdbcClient.sql("SELECT id,name,genre,origin FROM artist WHERE id = :id")
        .param("id", id)
        .query(Artist.class)
        .optional();
  }

  public ApiResponse create(Artist artist) {
    int result = jdbcClient.sql("INSERT INTO artist(name,genre,origin) values(?,?,?)")
        .params(List.of(artist.name(), artist.genre(), artist.origin()))
        .update();
    if (result == 1) {
      return new ApiResponse(LocalDateTime.now(), "Artist created successfully");
    } else {
      return new ApiResponse(LocalDateTime.now(), "Failed to create artist");
    }
  }

  @Override
  public ApiResponse update(Artist artist, Long id) {
    int result = jdbcClient.sql("update artist set name = ?, genre = ?, origin = ? where id = ?")
        .params(List.of(artist.name(), artist.genre(), artist.origin(), id))
        .update();
    if (result > 0) {
      return new ApiResponse(LocalDateTime.now(), "Artist updated successfully");
    } else {
      return new ApiResponse(LocalDateTime.now(), "Failed to update artist");
    }
  }

  @Override
  public ApiResponse delete(Long id) {
    Optional<Artist> findOneFirst = findOne(id);
    if (findOneFirst.isPresent()) {
      jdbcClient.sql("delete from artist where id = :id")
          .param("id", id)
          .update();
      return new ApiResponse(LocalDateTime.now(), "Artist deleted successfully");
    } else {
      return new ApiResponse(LocalDateTime.now(), "Artist not available to delete");
    }
  }

  @Override
  public List<Artist> findAll() {
    return jdbcClient.sql("SELECT id,name,genre,origin FROM artist")
        .query(Artist.class)
        .list();
  }
}
