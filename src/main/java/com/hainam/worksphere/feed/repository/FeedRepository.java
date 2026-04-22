package com.hainam.worksphere.feed.repository;

import com.hainam.worksphere.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedRepository extends JpaRepository<Feed, UUID> {

    @Query("SELECT f FROM Feed f WHERE f.isDeleted = false")
    List<Feed> findAllActive();

    @Query("SELECT f FROM Feed f WHERE f.id = :id AND f.isDeleted = false")
    Optional<Feed> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Feed f WHERE f.name = :name AND f.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
