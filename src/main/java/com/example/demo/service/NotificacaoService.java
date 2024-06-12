package com.example.demo.service;

import com.example.demo.modelo.CadastroCliente;
import com.example.demo.modelo.LancesEntregador;
import com.example.demo.modelo.Notificacao;
import com.example.demo.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoService {




    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public void criarNotificacao(CadastroCliente cliente, String mensagem, LancesEntregador lance, String tipo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setCliente(cliente);
        notificacao.setMensagem(mensagem);
        notificacao.setLida(false);
        notificacao.setDataHora(LocalDateTime.now());
        notificacao.setLance(lance);
        notificacao.setTipo(tipo); // Set the type of notification
        notificacaoRepository.save(notificacao);
    }

    public void marcarComoLida(Integer id) {
        Optional<Notificacao> notificacaoOpt = notificacaoRepository.findById(id);
        if (notificacaoOpt.isPresent()) {
            Notificacao notificacao = notificacaoOpt.get();
            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
        }
    }

    public List<Notificacao> buscarPorClienteId(Integer clienteId) {
        return notificacaoRepository.findByClienteId(clienteId);
    }


}
