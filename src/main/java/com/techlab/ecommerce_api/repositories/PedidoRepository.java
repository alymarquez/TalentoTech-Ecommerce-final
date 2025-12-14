package com.techlab.ecommerce_api.repositories;

import com.techlab.ecommerce_api.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByEstado(String estado);
    
    @Query("SELECT p FROM Pedido p ORDER BY p.fechaCreacion DESC")
    List<Pedido> findAllOrderByFechaDesc();
    
    @Query("SELECT p FROM Pedido p JOIN p.lineasPedido lp WHERE lp.producto.stock < 10")
    List<Pedido> findPedidosConStockBajo();
}