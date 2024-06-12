package com.example.demo.Controller;

import com.example.demo.modelo.CadastroCliente;
import com.example.demo.service.CadastroClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cadastro")
@CrossOrigin
public class CadastroClienteController {

    @Autowired
    private CadastroClienteService service;

    @GetMapping
    public ResponseEntity<List<CadastroCliente>> listarClientes() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listaClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CadastroCliente>> buscarId(@PathVariable(value = "id") Integer id) {
        Optional<CadastroCliente> cliente = service.buscarClienteId(id);
        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cliente);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUsuario(@RequestBody CadastroCliente cadastroCliente){
        String email = cadastroCliente.getEmail();
        String senha = cadastroCliente.getSenha();
        Optional<CadastroCliente> clienteOptional = service.login(email, senha);
        if(clienteOptional.isPresent()){
            CadastroCliente cliente = clienteOptional.get();
            return ResponseEntity.ok(Map.of(
                    "clienteId", cliente.getId(),
                    "nomeUsuario", cliente.getNome(),
                    "email", cliente.getEmail(),
                    "tipoUsuario", cliente.getTipoUsuario(),
                    "telefone", cliente.getTelefone(),
                    "message", "Login bem-sucedido!"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }
    }

    @PostMapping()
    public ResponseEntity<Object> cadastrar(@RequestBody @Valid CadastroCliente cliente){
        return ResponseEntity.status(HttpStatus.OK).body(service.cadastarCliente(cliente));
    }
    @PostMapping("/salvar-token")
    public ResponseEntity<Object> salvarTokenNotificacao(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        // Salvar o token no banco de dados ou em algum armazenamento
        // Retorne uma resposta adequada, por exemplo:
        return ResponseEntity.status(HttpStatus.OK).body("Token de notificação salvo com sucesso");
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarCadastro(
            @PathVariable(value = "id") Integer id,
            @RequestBody CadastroCliente cliente) {
        Optional<CadastroCliente> c = service.buscarClienteId(id);
        if (c.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cadastro não Encontrado!!!");
        }
        CadastroCliente clienteExistente = c.get();
        service.atualizarCliente(clienteExistente, cliente);

        return ResponseEntity.status(HttpStatus.OK).body("Cadastro atualizado com sucesso!");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarCadastro(@PathVariable(value = "id") Integer id) {
        Optional<CadastroCliente> c = service.buscarClienteId(id);
        if (c.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cadastro não Encontrado!!!");
        }
        service.deletaClienteComRegistrosAssociados(id);
        return ResponseEntity.status(HttpStatus.OK).body("Cadastro foi Deletado!!!");
    }
}
