package com.hainam.worksphere.pen.domain;

import com.hainam.worksphere.shared.audit.annotation.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pens")
@AuditableEntity(ignoreFields = {
    "id", "updatedAt", "updatedBy", "createdAt", "createdBy",
    "isDeleted", "deletedAt", "deletedBy"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pen_code", nullable = false, unique = true, length = 30)
    private String penCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @Enumerated(EnumType.STRING)
    @Column(name = "pen_type", length = 20)
    private PenType penType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private PenStatus status = PenStatus.EMPTY;

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
