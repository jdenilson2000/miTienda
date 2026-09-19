package com.tienda.mitienda.repository;

import com.tienda.mitienda.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Método personalizado para obtener solo los productos marcados como activos
    List<Producto> findByActivoTrue();

    // Método para buscar productos por nombre (Buscador)
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
