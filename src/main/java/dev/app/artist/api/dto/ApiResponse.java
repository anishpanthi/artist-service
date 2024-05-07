package dev.app.artist.api.dto;

import java.time.LocalDateTime;

/**
 * @author Anish Panthi
 */
public record ApiResponse(LocalDateTime timestamp, String message) {

}
