package com.example.demo.service;

import com.example.demo.modelo.LancesEntregador;
import com.example.demo.modelo.ProdutoCliente;
import com.example.demo.repository.CadastroClienteRepository;
import com.example.demo.repository.LanceEntregadorRepository;
import com.example.demo.repository.NotificacaoRepository;
import com.example.demo.repository.ProdutoClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoClienteService {

    @Autowired
    private ProdutoClienteRepository repository;

    @Autowired
    private LanceEntregadorRepository lancesRepository;

    @Autowired
    private CadastroClienteRepository cadastroClienteRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public List<ProdutoCliente> produtoClienteList(){
        return repository.findAll();
    }
    public ProdutoCliente postarProdutoCliente(ProdutoCliente produtoCliente) {
        return repository.save(produtoCliente);
    }
    public Optional<ProdutoCliente> buscarProdutoid(Integer id){
        return repository.findById(id);
    }
    public List<ProdutoCliente> listarProdutosPorClienteComOfertas(Integer clienteId) {
        return repository.findProdutosPorClienteComOfertas(clienteId);
    }
    // Novo método para listar todos os produtos de um cliente específico
    public List<ProdutoCliente> listarTodosProdutosPorClienteId(Integer clienteId) {
        return repository.findAllByClienteId(clienteId);
    }
    public void deletarProduto(Optional<ProdutoCliente> produtoCliente){
        repository.delete(produtoCliente.get());
    }
    @Transactional
    public void deletarProdutoPorId(Integer id){
        Optional<ProdutoCliente> produto = repository.findById(id);
        if (produto.isPresent()) {
            List<LancesEntregador> lances = lancesRepository.findByProdutoClienteId(id);
            // Deletar todas as notificações associadas a cada lance
            for (LancesEntregador lance : lances) {
                notificacaoRepository.deleteByLanceId(lance.getId());
            }
            // Deletar todos os lances
            lancesRepository.deleteByProdutoClienteId(id);
            // Deletar o produto
            repository.deleteById(id);
        }
    }
    public List<ProdutoCliente> listarProdutosPorClienteId(Integer clienteId) {
        return repository.findByClienteId(clienteId);
    }
    public List<ProdutoCliente> listarProdutosPorClienteIdWithLances(Integer clienteId) {
        return repository.findByClienteIdWithLances(clienteId);
    }

}