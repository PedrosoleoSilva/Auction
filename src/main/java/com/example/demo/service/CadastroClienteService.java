package com.example.demo.service;

import com.example.demo.modelo.CadastroCliente;
import com.example.demo.modelo.LancesEntregador;
import com.example.demo.modelo.ProdutoCliente;
import com.example.demo.repository.CadastroClienteRepository;
import com.example.demo.repository.LanceEntregadorRepository;
import com.example.demo.repository.NotificacaoRepository;
import com.example.demo.repository.ProdutoClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroClienteService {

    @Autowired
    private CadastroClienteRepository repository;
    @Autowired
    private ProdutoClienteRepository produtoClienteRepository;

    @Autowired
    private LanceEntregadorRepository lanceEntregadorRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(CadastroClienteService.class);


    public List<CadastroCliente> listaClientes() {
        return repository.findAll();
    }

    public CadastroCliente cadastarCliente(CadastroCliente cadastro) {

        Optional<CadastroCliente> existingUser = repository.findByEmail(cadastro.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        if (!cadastro.getEmail().endsWith("@gmail.com") && !cadastro.getEmail().endsWith("@hotmail.com")) {
            throw new RuntimeException("Email deve conter @gmail.com ou @hotmail.com");
        }
        cadastro.setSenha(passwordEncoder.encode(cadastro.getSenha()));
        logger.debug("Cliente cadastrado com sucesso: {}", cadastro);

        return repository.save(cadastro);
    }

    public void atualizarCliente(CadastroCliente clienteExistente, CadastroCliente cliente) {
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEmail(cliente.getEmail());

        // Só codifique e atualize a senha se não for null
        if (cliente.getSenha() != null && !cliente.getSenha().isEmpty()) {
            clienteExistente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        }

        clienteExistente.setTipoUsuario(cliente.getTipoUsuario());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteExistente.setSexo(cliente.getSexo());


        repository.save(clienteExistente);
    }


    public Optional<CadastroCliente> buscarClienteId(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public void deletaClienteComRegistrosAssociados(Integer clienteId) {
        Optional<CadastroCliente> clienteOptional = buscarClienteId(clienteId);
        if (clienteOptional.isPresent()) {
            CadastroCliente cliente = clienteOptional.get();

            // Deletar todas as notificações associadas
            List<LancesEntregador> lances = lanceEntregadorRepository.findByClienteId(clienteId);
            for (LancesEntregador lance : lances) {
                notificacaoRepository.deleteByLanceId(lance.getId());
            }

            // Deletar todas as ofertas associadas
            lanceEntregadorRepository.deleteAll(lances);

            // Deletar todos os produtos associados
            List<ProdutoCliente> produtos = produtoClienteRepository.findByClienteId(clienteId);
            produtoClienteRepository.deleteAll(produtos);

            // Deletar o cliente
            repository.delete(cliente);
        }
    }

    public Optional<CadastroCliente> login(String email, String senha) {
        Optional<CadastroCliente> clienteOptional = repository.findByEmail(email);
        if (clienteOptional.isPresent()) {
            CadastroCliente cliente = clienteOptional.get();
            if (passwordEncoder.matches(senha, cliente.getSenha())) {
                return clienteOptional;
            }
        }
        return Optional.empty();
    }
}