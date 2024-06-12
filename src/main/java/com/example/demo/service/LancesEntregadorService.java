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

    public List<LancesEntregador> listarOfertasPorProduto(Integer idProduto) {
        return repository.findByProdutoClienteId(idProduto);
    }

    public List<LancesEntregador> listarOfertasRecebidasPorCliente(Integer clienteId) {
        return repository.findByProdutoCliente_Cliente_Id(clienteId);
    }

    public List<LancesEntregador> listOferta(Integer produtoClienteid) {
        return repository.findByProdutoClienteId(produtoClienteid);
    }

    public List<LancesEntregador> listarOfertasRecebidasComDetalhes(Integer clienteId) {
        return repository.findLancesWithProductDetailsByClienteId(clienteId);
    }

    public List<ProdutoCliente> listarProdutosComOfertasRecebidas(Integer clienteId) {
        return repository.findProdutosComOfertasRecebidasPorCliente(clienteId);
    }
    public List<LancesEntregador> listarOfertasAceitasPorCliente(Integer clienteId) {
        return repository.findByClienteIdAndStatus(clienteId);
    }


    public List<ProdutoCliente> listarProdutosComOfertas(Integer clienteId) {
        return repository.findByProdutoCliente_Cliente_Id(clienteId).stream()
                .map(LancesEntregador::getProdutoCliente)
                .distinct()
                .collect(Collectors.toList());
    }

    public String obterNomeCliente(Integer clienteId) {
        Optional<ProdutoCliente> produtoClienteOptional = clienteRepository.findById(clienteId);
        if (produtoClienteOptional.isPresent()) {
            ProdutoCliente produtoCliente = produtoClienteOptional.get();
            return produtoCliente.getCliente().getNome(); // Supondo que o nome do cliente esteja no atributo 'nome' da classe ProdutoCliente
        } else {
            return "Cliente não encontrado";
        }
    }

    // Novo método para atualizar a mensagem
    public void atualizarMensagem(Integer id, String mensagem) {
        Optional<LancesEntregador> ofertaOptional = repository.findById(id);
        if (ofertaOptional.isPresent()) {
            LancesEntregador oferta = ofertaOptional.get();
            oferta.setMensagem(mensagem);
            repository.save(oferta);
        }
    }

}