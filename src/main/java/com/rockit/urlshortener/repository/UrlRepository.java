package com.rockit.urlshortener.repository;

import com.rockit.urlshortener.domain.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {

}
