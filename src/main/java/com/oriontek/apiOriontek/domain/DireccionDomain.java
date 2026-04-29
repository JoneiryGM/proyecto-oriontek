package com.oriontek.apiOriontek.domain;

import com.oriontek.apiOriontek.dto.request.DireccionRequest;
import com.oriontek.apiOriontek.dto.response.DireccionResponse;
import com.oriontek.apiOriontek.utils.ToDTO;
import com.oriontek.apiOriontek.utils.TransformFrom;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "direcciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DireccionDomain implements ToDTO<DireccionResponse>, TransformFrom<DireccionRequest, DireccionDomain> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteDomain clienteDomain;

    @Column(nullable = false, length = 200)
    private String calle;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String provincia;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String pais = "República Dominicana";

    @Column(nullable = false)
    @Builder.Default
    private Boolean principal = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public DireccionResponse toDto() {
        return new DireccionResponse(
                this.id,
                this.clienteDomain != null ? this.clienteDomain.getId() : null,
                this.calle,
                this.ciudad,
                this.provincia,
                this.codigoPostal,
                this.pais,
                this.principal,
                this.createdAt,
                this.updatedAt
        );
    }

    @Override
    public DireccionDomain from(DireccionRequest request) {
        return DireccionDomain.builder()
                .calle(request.calle())
                .ciudad(request.ciudad())
                .provincia(request.provincia())
                .codigoPostal(request.codigoPostal())
                .pais(request.pais() != null ? request.pais() : "República Dominicana")
                .principal(Boolean.TRUE.equals(request.principal()))
                .build();
    }

    public void updateFrom(DireccionRequest request) {
        this.calle        = request.calle();
        this.ciudad       = request.ciudad();
        this.provincia    = request.provincia();
        this.codigoPostal = request.codigoPostal();
        if (request.pais() != null) this.pais = request.pais();
        if (request.principal() != null) this.principal = request.principal();
    }
}