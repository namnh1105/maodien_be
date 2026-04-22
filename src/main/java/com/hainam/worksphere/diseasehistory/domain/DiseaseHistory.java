package com.hainam.worksphere.diseasehistory.domain;

import com.hainam.worksphere.shared.audit.annotation.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "disease_histories")
@AuditableEntity(ignoreFields = {"id", "createdAt", "updatedAt", "createdBy", "updatedBy", "isDeleted", "deletedAt", "deletedBy"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "pig_id")
    private UUID pigId;
    @Column(name = "disease_name", length = 200)
    private String diseaseName;
    @Column(name = "sick_date")
    private LocalDate sickDate;
    @Column(name = "recovery_date")
    private LocalDate recoveryDate;
    @Column(name = "severity", length = 20)
    private String severity;
    @Column(name = "expected_treatment_days")
    private Integer expectedTreatmentDays;
    @Column(name = "status", length = 50)
    private String status;
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

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
    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    @Column(name = "deleted_at")
    private Instant deletedAt;
    @Column(name = "deleted_by")
    private UUID deletedBy;
}
