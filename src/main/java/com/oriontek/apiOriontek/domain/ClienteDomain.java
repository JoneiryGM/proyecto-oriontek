package com.oriontek.apiOriontek.domain;


import com.oriontek.apiOriontek.dto.request.ClienteRequest;
import com.oriontek.apiOriontek.dto.response.ClienteResponse;
import com.oriontek.apiOriontek.dto.response.DireccionResponse;
import com.oriontek.apiOriontek.utils.ToDTO;
import com.oriontek.apiOriontek.utils.TransformFrom;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClienteDomain implements ToDTO<ClienteResponse>, TransformFrom<ClienteRequest, ClienteDomain> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @OneToMany(mappedBy = "clienteDomain", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DireccionDomain> direcciones = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void addDireccion(DireccionDomain direccionDomain) {
        direcciones.add(direccionDomain);
        direccionDomain.setClienteDomain(this);
    }

    public void removeDireccion(DireccionDomain direccionDomain) {
        direcciones.remove(direccionDomain);
        direccionDomain.setClienteDomain(null);
    }

    @Override
    public ClienteResponse toDto() {
        List<DireccionResponse> dirs = this.direcciones == null
                ? List.of()
                : this.direcciones.stream().map(DireccionDomain::toDto).toList();

        return new ClienteResponse(
                this.id,
                this.nombre,
                this.apellido,
                this.email,
                this.telefono,
                this.activo,
                dirs,
                this.createdAt,
                this.updatedAt
        );
    }

    @Override
    public ClienteDomain from(ClienteRequest request) {
        return ClienteDomain.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .telefono(request.telefono())
                .build();
    }

    public void updateFrom(ClienteRequest request) {
        this.nombre    = request.nombre();
        this.apellido  = request.apellido();
        this.email     = request.email();
        this.telefono  = request.telefono();
    }

}