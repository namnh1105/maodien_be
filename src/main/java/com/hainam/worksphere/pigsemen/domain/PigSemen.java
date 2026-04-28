package com.hainam.worksphere.pigsemen.domain;

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
@Table(name = "pig_semen")
@AuditableEntity(ignoreFields = {
    "id", "updatedAt", "updatedBy", "createdAt", "createdBy",
    "isDeleted", "deletedAt", "deletedBy"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigSemen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    @Column(name = "boar_pig_id")
    private UUID boarPigId;

    @Column(name = "boar_breed", length = 100)
    private String boarBreed;

    @Column(name = "collection_date")
    private LocalDate collectionDate;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "motility")
    private Double motility;

    @Column(name = "quality", length = 50)
    private String quality;

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

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;
}
