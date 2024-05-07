package dev.app.artist.api.service;

import dev.app.artist.api.dto.ApiResponse;
import dev.app.artist.api.entity.Artist;
import java.util.List;
import java.util.Optional;

/**
 * @author Anish Panthi
 */
public interface ArtistService {

  Optional<Artist> findOne(Long id);

  ApiResponse create(Artist artist);

  ApiResponse update(Artist artist, Long id);

  ApiResponse delete(Long id);

  List<Artist> findAll();
}
