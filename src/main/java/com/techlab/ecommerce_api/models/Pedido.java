package com.techlab.ecommerce_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private Double total = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LineaPedido> lineasPedido = new ArrayList<>();

    public void agregarLinea(LineaPedido linea) {
        linea.setPedido(this);
        this.lineasPedido.add(linea);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = lineasPedido.stream()
                .mapToDouble(LineaPedido::calcularSubtotal)
                .sum();
    }

    public boolean tieneLineas() {
        return lineasPedido != null && !lineasPedido.isEmpty();
    }
}