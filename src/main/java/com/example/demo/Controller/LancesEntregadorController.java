package com.example.demo.Controller;

import com.example.demo.modelo.CadastroCliente;
import com.example.demo.modelo.LancesEntregador;
import com.example.demo.modelo.ProdutoCliente;
import com.example.demo.service.LancesEntregadorService;
import com.example.demo.service.NotificacaoService;
import com.example.demo.service.ProdutoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lacesProduto")
@CrossOrigin
public class LancesEntregadorController {

    @Autowired
    private LancesEntregadorService service;

    @Autowired
    ProdutoClienteService clienteService;

    @Autowired
    private NotificacaoService notificacaoService;


    @PostMapping()
    public ResponseEntity<Object> fazerLance(@RequestBody LancesEntregador lances) {
        lances.setData(LocalDate.now());
        if (lances.getCliente() == null) { //verifique se a oferta esta associado ao clienteid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A oferta deve estar associada a um cliente.");
        }
        LancesEntregador lancesEntregador = service.lancesEntregadorSalvar(lances);

        // Recuperar informações do produto associado à oferta para poder enviar notificação
        Integer produtoId = lances.getProdutoCliente().getId();
        Optional<ProdutoCliente> produtoOptional = clienteService.buscarProdutoid(produtoId);
        if (produtoOptional.isPresent()) {
            ProdutoCliente produto = produtoOptional.get();
            String nomeProduto = produto.getNomeProduto();
            Double valorLance = lances.getValorLance();
            CadastroCliente donoProduto = produto.getCliente(); //pega de quem e o dono do produto para enviar notificação

            String tokenDispositivoDonoProduto = donoProduto.getExpoPushToken();

            // Construa a mensagem de notificação para enviar ao cliente
            String mensagemNotificacao = String.format("Nova oferta recebida para o produto %s: R$ %.2f", nomeProduto, valorLance);

            // Criar notificação no sistema e salva na tabela Notificação no banco
            notificacaoService.criarNotificacao(donoProduto, mensagemNotificacao, lancesEntregador, "nova_oferta");

            // Retornar a resposta da solicitação
            return ResponseEntity.status(HttpStatus.OK).body("Oferta registrada com sucesso e notificação enviada.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<LancesEntregador>> listarLances() {
        return ResponseEntity.status(HttpStatus.OK).body(service.lancesEntregadorList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarLance(@PathVariable(value = "id") Integer id, @RequestBody LancesEntregador lancesEntregador) {
        Optional<LancesEntregador> l = service.buscarLanceId(id);
        if (l.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Produto não foi Encontrado!");
        }
        service.deletarOferta(l);
        return ResponseEntity.status(HttpStatus.OK).body("A oferta foi deletada!");
    }

    @PutMapping("/{id}/statusAceito") //metodo para aceitar oferta entrega
    public ResponseEntity<Object> statusAceito(@PathVariable("id") Integer id) {
        Optional<LancesEntregador> ofertaOptional = service.buscarLanceId(id);
        if (ofertaOptional.isPresent()) {
            LancesEntregador oferta = ofertaOptional.get();
            oferta.setStatus("Aceito");
            service.lancesEntregadorSalvar(oferta);

            ProdutoCliente produto = oferta.getProdutoCliente();
            produto.setOfertaAceita(true); // Atualiza o estado para true na tabela produto, para poder listar somentes produtos que nao foram aceitos as oferta de entrega
            clienteService.postarProdutoCliente(produto); // Salva as alterações no produto

            return ResponseEntity.ok("Oferta aceita com sucesso e notificação enviada."); //esta sendo feita uma futura notificaçao
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta não encontrada!");
        }
    }
    @GetMapping("/ofertasRecebidasDetalhadas/{clienteId}") //exibe a oferta do cliente do produto recebeu
    public ResponseEntity<List<LancesEntregador>> listarOfertasRecebidasDetalhadas(@PathVariable Integer clienteId) {
        List<LancesEntregador> lances = service.listarOfertasRecebidasComDetalhes(clienteId);
        if (lances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(lances);
    }

    @PutMapping("/{id}/statusRejeitado")
    public ResponseEntity<Object> statusRejeitado(@PathVariable("id") Integer id) {
        Optional<LancesEntregador> ofertaOptional = service.buscarLanceId(id);
        if (ofertaOptional.isPresent()) {
            LancesEntregador oferta = ofertaOptional.get();
            oferta.setStatus("Rejeitado");
            service.lancesEntregadorSalvar(oferta);
            return ResponseEntity.status(HttpStatus.OK).body("Oferta rejeitada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta não encontrada!");
        }
    }

    @GetMapping("/ofertasAceitas/{clienteId}") //lista ao entregador as suas ofertas de entrega aceita
    public ResponseEntity<List<LancesEntregador>> listarOfertasAceitasPorCliente(@PathVariable Integer clienteId) {
        List<LancesEntregador> ofertasAceitas = service.listarOfertasAceitasPorCliente(clienteId);
        if (ofertasAceitas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(ofertasAceitas);
    }
    @PutMapping("/confirmarEntrega/{id}")
    public ResponseEntity<Object> confirmarEntrega(@PathVariable("id") Integer id) {
        Optional<LancesEntregador> ofertaOptional = service.buscarLanceId(id);
        if (ofertaOptional.isPresent()) {
            LancesEntregador oferta = ofertaOptional.get();
            oferta.setEntregaFeita("Sim"); // Atualize o status da entrega
            service.lancesEntregadorSalvar(oferta);

            ProdutoCliente produto = oferta.getProdutoCliente();
            CadastroCliente donoProduto = produto.getCliente();

            String mensagemNotificacao = String.format("A entrega do produto %s foi realizada com sucesso!", produto.getNomeProduto());

            // Enviar notificação
            notificacaoService.criarNotificacao(donoProduto, mensagemNotificacao, oferta, "entrega_confirmada");

            return ResponseEntity.status(HttpStatus.OK).body("Entrega confirmada com sucesso e notificação enviada.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta não encontrada!");
        }
    }

}
