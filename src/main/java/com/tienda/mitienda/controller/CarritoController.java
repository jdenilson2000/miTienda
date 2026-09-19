package com.tienda.mitienda.controller;

import com.tienda.mitienda.entity.Producto;
import com.tienda.mitienda.service.CarritoService;
import com.tienda.mitienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;

    public CarritoController(CarritoService carritoService, ProductoService productoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    @GetMapping
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.getTotal());
        return "cliente/carrito";
    }

    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam("id") Long id, @RequestParam(value = "cantidad", defaultValue = "1") Integer cantidad) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto != null && producto.getStock() >= cantidad) {
            carritoService.agregarProducto(producto, cantidad);
        }
        return "redirect:/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizarCantidad(@RequestParam("id") Long id, @RequestParam("cantidad") Integer cantidad) {
        carritoService.modificarCantidad(id, cantidad);
        return "redirect:/carrito";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Long id) {
        carritoService.eliminarProducto(id);
        return "redirect:/carrito";
    }
}