package com.tienda.mitienda.controller;

import com.tienda.mitienda.entity.Producto;
import com.tienda.mitienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogoController {

    private final ProductoService productoService;

    public CatalogoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Ruta principal: Muestra la tienda y procesa búsquedas si se envía parámetro 'buscar'
    @GetMapping("/")
    public String verCatalogo(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        List<Producto> productos = productoService.buscarProductos(buscar);

        model.addAttribute("productos", productos);
        model.addAttribute("criterioBusqueda", buscar != null ? buscar : "");

        return "cliente/tienda"; // Renderiza templates/cliente/tienda.html
    }
}