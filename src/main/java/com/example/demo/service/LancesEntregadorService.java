package com.example.demo.service;

import com.example.demo.modelo.LancesEntregador;
import com.example.demo.modelo.ProdutoCliente;
import com.example.demo.repository.LanceEntregadorRepository;
import com.example.demo.repository.ProdutoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LancesEntregadorService {
    @Autowired
    private LanceEntregadorRepository repository;

    @Autowired
    private ProdutoClienteRepository clienteRepository;

    public LancesEntregador lancesEntregadorSalvar(LancesEntregador entregador) {
        return repository.save(entregador);
    }

    public List<LancesEntregador> lancesEntregadorList() {
        return repository.findAll();
    }

    public Optional<LancesEntregador> buscarLanceId(Integer id) {
        return repository.findById(id);
    }

    public void deletarOferta(Optional<LancesEntregador> entregador) {
        repository.delete(entregador.get());
    }


    public List<LancesEntregador> listarOfertasRecebidasComDetalhes(Integer clienteId) {
        return repository.findLancesWithProductDetailsByClienteId(clienteId);
    }

    public List<LancesEntregador> listarOfertasAceitasPorCliente(Integer clienteId) {
        return repository.findByClienteIdAndStatus(clienteId);
    }


}