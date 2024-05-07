package dev.app.artist.api.entity;

/**
 * Artist Entity
 *
 * @param id
 * @param name
 * @param genre
 * @param origin
 * @author Anish
 */
public record Artist(
    Long id, String name, String genre, String origin) {

}
