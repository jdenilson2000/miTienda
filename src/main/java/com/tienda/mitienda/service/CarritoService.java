package com.tienda.mitienda.service;

import com.tienda.mitienda.dto.ItemCarrito;
import com.tienda.mitienda.entity.Producto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope // Mantiene una instancia única del carrito por cada usuario conectado
public class CarritoService {

    private final List<ItemCarrito> items = new ArrayList<>();

    public void agregarProducto(Producto producto, Integer cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(producto, cantidad));
    }

    public void modificarCantidad(Long productoId, Integer cantidad) {
        if (cantidad <= 0) {
            eliminarProducto(productoId);
            return;
        }
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(productoId)) {
                item.setCantidad(cantidad);
                break;
            }
        }
    }

    public void eliminarProducto(Long productoId) {
        items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public Double getTotal() {
        return items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
    }

    public void limpiar() {
        items.clear();
    }
}