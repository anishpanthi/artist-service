package dev.app.artist.api;

import co.elastic.apm.attach.ElasticApmAttacher;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArtistServiceApplication {

  public static void main(String[] args) {
    ElasticApmAttacher.attach();
    SpringApplication.run(ArtistServiceApplication.class, args);
  }

  @Bean
  ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
    return new ObservedAspect(observationRegistry);
  }
}
