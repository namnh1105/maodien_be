package com.hainam.worksphere.reproductioncycle.domain;

import com.hainam.worksphere.shared.audit.annotation.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reproduction_cycles")
@AuditableEntity(ignoreFields = {
    "id", "updatedAt", "updatedBy", "createdAt", "createdBy",
    "isDeleted", "deletedAt", "deletedBy"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReproductionCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cycle_code", nullable = false, unique = true, length = 30)
    private String cycleCode;

    @Column(name = "mating_id")
    private UUID matingId;

    @Column(name = "conception_date")
    private LocalDate conceptionDate;

    @Column(name = "expected_farrow_date")
    private LocalDate expectedFarrowDate;

    @Column(name = "actual_farrow_date")
    private LocalDate actualFarrowDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "born_count")
    private Integer bornCount;

    @Column(name = "alive_count")
    private Integer aliveCount;

    @Column(name = "dead_count")
    private Integer deadCount;

    @Column(name = "average_weight")
    private Double averageWeight;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;
}
