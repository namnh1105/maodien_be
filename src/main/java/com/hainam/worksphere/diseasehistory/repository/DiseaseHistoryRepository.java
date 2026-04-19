package com.hainam.worksphere.diseasehistory.repository;

import com.hainam.worksphere.diseasehistory.domain.DiseaseHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DiseaseHistoryRepository extends JpaRepository<DiseaseHistory, UUID> {
    @Query("SELECT d FROM DiseaseHistory d WHERE d.isDeleted = false")
    List<DiseaseHistory> findAllActive();

    @Query("SELECT d FROM DiseaseHistory d WHERE d.id = :id AND d.isDeleted = false")
    Optional<DiseaseHistory> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DiseaseHistory d WHERE d.historyCode = :code AND d.isDeleted = false")
    boolean existsActiveByHistoryCode(@Param("code") String code);
}
