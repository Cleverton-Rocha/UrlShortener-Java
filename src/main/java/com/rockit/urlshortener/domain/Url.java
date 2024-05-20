package com.rockit.urlshortener.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {

  @Id
  private String id;

  private String originalUrl;

  @Indexed(expireAfterSeconds = 0)
  private LocalDateTime expiresAt;
}
