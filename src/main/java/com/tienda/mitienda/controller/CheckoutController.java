package com.tienda.mitienda.controller;

import com.tienda.mitienda.entity.Cliente;
import com.tienda.mitienda.entity.Pedido;
import com.tienda.mitienda.service.CarritoService;
import com.tienda.mitienda.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    public CheckoutController(CarritoService carritoService, PedidoService pedidoService) {
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String formularioCheckout(Model model) {
        if (carritoService.getItems().isEmpty()) {
            return "redirect:/carrito";
        }
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("total", carritoService.getTotal());
        return "cliente/checkout";
    }

    @PostMapping("/procesar")
    public String procesarPedido(@ModelAttribute("cliente") Cliente cliente) {
        if (carritoService.getItems().isEmpty()) {
            return "redirect:/";
        }

        Pedido pedidoRealizado = pedidoService.procesarPedido(cliente, carritoService.getItems());

        // Limpiar carrito en sesión
        carritoService.limpiar();

        return "redirect:/checkout/confirmacion/" + pedidoRealizado.getId();
    }

    @GetMapping("/confirmacion/{id}")
    public String verConfirmacion(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        if (pedido == null) {
            return "redirect:/";
        }
        model.addAttribute("pedido", pedido);
        return "cliente/confirmacion";
    }
}