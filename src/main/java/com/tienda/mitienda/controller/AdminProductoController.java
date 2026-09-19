package com.tienda.mitienda.controller;

import com.tienda.mitienda.entity.Producto;
import com.tienda.mitienda.service.ProductoService;
import com.tienda.mitienda.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private final ProductoService productoService;
    private final UploadFileService uploadFileService;

    public AdminProductoController(ProductoService productoService, UploadFileService uploadFileService) {
        this.productoService = productoService;
        this.uploadFileService = uploadFileService;
    }

    // Listar todos los productos
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        return "admin/productos/lista";
    }

    // Formulario de creación
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "admin/productos/form";
    }

    // Guardar nuevo producto o cambios
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

        if (producto.getId() == null) {
            // Producto nuevo
            String nombreImagen = uploadFileService.guardarImagen(file);
            producto.setImagenUrl(nombreImagen);
        } else {
            // Edición de producto
            if (file.isEmpty()) {
                Producto prodExistente = productoService.obtenerPorId(producto.getId());
                producto.setImagenUrl(prodExistente.getImagenUrl());
            } else {
                String nombreImagen = uploadFileService.guardarImagen(file);
                producto.setImagenUrl(nombreImagen);
            }
        }

        productoService.guardar(producto);
        return "redirect:/admin/productos";
    }

    // Formulario para editar
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        return "admin/productos/form";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        if (p != null) {
            uploadFileService.eliminarImagen(p.getImagenUrl());
            productoService.eliminar(id);
        }
        return "redirect:/admin/productos";
    }
}