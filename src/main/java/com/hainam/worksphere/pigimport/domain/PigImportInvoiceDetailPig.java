package com.hainam.worksphere.pigimport.domain;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.shared.audit.annotation.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pig_import_invoice_detail_pigs")
@AuditableEntity(ignoreFields = {
        "id", "updatedAt", "updatedBy", "createdAt", "createdBy",
        "isDeleted", "deletedAt", "deletedBy"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigImportInvoiceDetailPig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_id", nullable = false)
    private PigImportInvoiceDetail detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pig_id", nullable = false)
    private Pig pig;

    @Column(name = "pen_id")
    private UUID penId;

    @Column(name = "created_by")
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;
}
