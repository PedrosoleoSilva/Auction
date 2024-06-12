package com.example.demo.Controller;

import com.example.demo.modelo.Notificacao;
import com.example.demo.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacao>> getNotificacoesPorClienteId(@PathVariable Integer clienteId) {
        List<Notificacao> notificacoes = notificacaoService.buscarPorClienteId(clienteId);
        if (notificacoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(notificacoes);
        }
        return ResponseEntity.ok(notificacoes);
    }
    @PutMapping("/{id}/marcarComoLida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Integer id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }
}