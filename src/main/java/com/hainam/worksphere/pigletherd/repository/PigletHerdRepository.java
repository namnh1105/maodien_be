package com.hainam.worksphere.pigletherd.repository;

import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigletHerdRepository extends JpaRepository<PigletHerd, UUID> {

    @Query("SELECT h FROM PigletHerd h WHERE h.isDeleted = false")
    List<PigletHerd> findAllActive();

    @Query("SELECT h FROM PigletHerd h WHERE h.id = :id AND h.isDeleted = false")
    Optional<PigletHerd> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM PigletHerd h WHERE h.herdCode = :code AND h.isDeleted = false")
    boolean existsActiveByHerdCode(@Param("code") String code);

    @Query("SELECT h FROM PigletHerd h WHERE h.mother.id = :motherId AND h.isDeleted = false")
    List<PigletHerd> findActiveByMotherId(@Param("motherId") UUID motherId);

    @Query("SELECT h FROM PigletHerd h WHERE h.mother.id IN :motherIds AND h.isDeleted = false")
    List<PigletHerd> findActiveByMotherIds(@Param("motherIds") List<UUID> motherIds);
}
