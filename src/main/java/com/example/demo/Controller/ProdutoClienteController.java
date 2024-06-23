package com.example.demo.Controller;

import com.example.demo.modelo.CadastroCliente;
import com.example.demo.modelo.ProdutoCliente;
import com.example.demo.service.ProdutoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postarProduto")
@CrossOrigin
public class ProdutoClienteController {

    @Autowired
    private ProdutoClienteService produtoClienteService;


    @GetMapping
    public ResponseEntity<List<ProdutoCliente>> produtoList(){
        List<ProdutoCliente> produtos = produtoClienteService.produtoClienteList();
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }

    @PostMapping
    public ResponseEntity<Object> postarProduto(@RequestBody ProdutoCliente produtoCliente){
        if(produtoCliente.getCliente() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O produto deve estar associado a um cliente.");
        }
        ProdutoCliente produtoCliente1 = produtoClienteService.postarProdutoCliente(produtoCliente);
        return ResponseEntity.status(HttpStatus.OK).body(produtoCliente1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> alterarProduto(@PathVariable(value = "id") Integer id, @RequestBody ProdutoCliente produtoCliente){
        Optional<ProdutoCliente> p = produtoClienteService.buscarProdutoid(id);
        if(p.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Produto não Encontrado!!!");
        }
        ProdutoCliente produtoCliente1 = p.get();
        produtoCliente1.setDescricao(produtoCliente.getDescricao());
        produtoCliente1.setFoto(produtoCliente.getFoto());
        produtoCliente1.setLocalOrigemEstado(produtoCliente1.getLocalOrigemEstado());
        produtoCliente1.setLocalOrigemCidade(produtoCliente1.getLocalOrigemCidade());
        produtoCliente1.setDestinoEstado(produtoCliente.getDestinoEstado());
        produtoCliente1.setDestinoCidade(produtoCliente.getDestinoCidade());
        produtoCliente1.setValorSugerido(produtoCliente.getValorSugerido());
        return ResponseEntity.status(HttpStatus.OK).body(produtoClienteService.postarProdutoCliente(produtoCliente1));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarCadastro(@PathVariable(value = "id") Integer id, @RequestBody CadastroCliente cliente){
        Optional<ProdutoCliente> p = produtoClienteService.buscarProdutoid(id);
        if(p.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Cadastro não Encontrado!!!");
        }
        produtoClienteService.deletarProduto(p);
        return ResponseEntity.status(HttpStatus.OK).body("Cadastro foi Deletado!!!");
    }
    @GetMapping("/porCliente/{clienteId}")
    public ResponseEntity<List<ProdutoCliente>> listarProdutosPorCliente(@PathVariable Integer clienteId) {
        List<ProdutoCliente> produtos = produtoClienteService.listarProdutosPorClienteIdWithLances(clienteId);
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/porClienteComOfertas/{clienteId}")
    public ResponseEntity<List<ProdutoCliente>> listarProdutosPorClienteComOfertas(@PathVariable Integer clienteId) {
        List<ProdutoCliente> produtos = produtoClienteService.listarProdutosPorClienteComOfertas(clienteId);
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(produtos);
    }
    // Novo endpoint para listar todos os produtos de um cliente específico
    @GetMapping("/todosPorCliente/{clienteId}")
    public ResponseEntity<List<ProdutoCliente>> listarTodosProdutosPorCliente(@PathVariable Integer clienteId) {
        List<ProdutoCliente> produtos = produtoClienteService.listarTodosProdutosPorClienteId(clienteId);
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Object> deletarProdutoPorId(@PathVariable(value = "id") Integer id) {
        produtoClienteService.deletarProdutoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto foi Deletado!!!");
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<ProdutoCliente>> listarProdutosComOfertasPendentes() {
        List<ProdutoCliente> produtos = produtoClienteService.listarProdutosComOfertasPendentes();
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(produtos);
    }





}