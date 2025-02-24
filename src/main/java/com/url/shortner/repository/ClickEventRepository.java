package com.url.shortner.repository;

import com.url.shortner.models.ClickEvent;
import com.url.shortner.models.URLMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByUrlMappingAndClickDateBetween(URLMapping urlMapping, LocalDateTime startDate, LocalDateTime endDate);
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<URLMapping> urlMappings, LocalDateTime startDate, LocalDateTime endDate);

}
