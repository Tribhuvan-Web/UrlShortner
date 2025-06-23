package com.url.shortner.repository;

import com.url.shortner.models.URLMapping;
import com.url.shortner.models.UrlMappingWithoutSignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLMappings extends JpaRepository<UrlMappingWithoutSignUp, Long> {

    UrlMappingWithoutSignUp findByShortUrl(String shortUrl);
}
