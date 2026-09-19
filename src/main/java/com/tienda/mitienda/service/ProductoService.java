package com.tienda.mitienda.service;

import com.tienda.mitienda.entity.Producto;
import com.tienda.mitienda.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Carga productos iniciales si la tabla de la BD está vacía
    @PostConstruct
    public void cargarDatosIniciales() {
        if (productoRepository.count() == 0) {
            productoRepository.save(new Producto("Cuaderno Anillado A5", "Cuaderno tapa dura ideal para apuntes universitarios.", 18.50, 20, "producto1.jpg", true));
            productoRepository.save(new Producto("Set de Lapiceros Gel", "Pack de 6 lapiceros tinta gel punta fina 0.5mm.", 12.00, 15, "producto2.jpg", true));
            productoRepository.save(new Producto("Planner Semanal Desk", "Organizador semanal de escritorio 50 hojas desglosables.", 25.00, 10, "producto3.jpg", true));
        }
    }

    // Obtener todos los productos activos
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    // Buscar productos por nombre
    public List<Producto> buscarProductos(String criterio) {
        if (criterio != null && !criterio.trim().isEmpty()) {
            return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(criterio);
        }
        return obtenerProductosActivos();
    }

    // Obtener producto por ID
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // Obtener todos los productos (activos e inactivos para el Administrador)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Guardar o actualizar producto
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Eliminar o desactivar producto
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}