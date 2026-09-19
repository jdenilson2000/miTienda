package com.tienda.mitienda.service;

import com.tienda.mitienda.dto.ItemCarrito;
import com.tienda.mitienda.entity.*;
import com.tienda.mitienda.repository.ClienteRepository;
import com.tienda.mitienda.repository.PedidoRepository;
import com.tienda.mitienda.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Pedido procesarPedido(Cliente cliente, List<ItemCarrito> itemsCarrito) {
        // 1. Guardar cliente
        Cliente clienteGuardado = clienteRepository.save(cliente);

        // 2. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(clienteGuardado);

        double total = 0.0;

        // 3. Procesar ítems, actualizar stock y construir detalles
        for (ItemCarrito item : itemsCarrito) {
            Producto prod = item.getProducto();

            // Descontar stock en base de datos
            prod.setStock(prod.getStock() - item.getCantidad());
            productoRepository.save(prod);

            DetallePedido detalle = new DetallePedido(prod, item.getCantidad(), prod.getPrecio());
            pedido.agregarDetalle(detalle);

            total += detalle.getSubtotal();
        }

        pedido.setTotal(total);

        // 4. Guardar pedido con sus detalles en SQL Server
        return pedidoRepository.save(pedido);
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }
}