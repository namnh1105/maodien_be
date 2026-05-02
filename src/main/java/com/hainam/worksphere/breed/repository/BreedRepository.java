package com.hainam.worksphere.breed.repository;

import com.hainam.worksphere.breed.domain.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BreedRepository extends JpaRepository<Breed, UUID> {

    @Query("SELECT b FROM Breed b WHERE b.isDeleted = false")
    List<Breed> findAllActive();

    @Query("SELECT b FROM Breed b WHERE b.id = :id AND b.isDeleted = false")
    Optional<Breed> findActiveById(@Param("id") UUID id);

    @Query("SELECT b FROM Breed b WHERE b.name = :name AND b.isDeleted = false")
    Optional<Breed> findActiveByName(@Param("name") String name);

    @Query("SELECT b FROM Breed b WHERE b.code = :code AND b.isDeleted = false")
    Optional<Breed> findActiveByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Breed b WHERE b.name = :name AND b.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
