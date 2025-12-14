package com.techlab.ecommerce_api.repositories;

import com.techlab.ecommerce_api.models.LineaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long> {
    List<LineaPedido> findByPedidoId(Long pedidoId);
}