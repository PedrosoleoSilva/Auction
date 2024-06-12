package com.example.demo.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class LancesEntregador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double valorLance;

    private LocalDate data;

    private String tempoEntregaEstimado;

    private String status;

    private String entregaFeita;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private CadastroCliente cliente;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "produto_cliente_id")
    private ProdutoCliente produtoCliente;

    @Column(length = 500)
    private String mensagem;


    public LancesEntregador() {
    }

    public String getNomeProduto() {
        return produtoCliente != null ? produtoCliente.getNomeProduto() : null;

    }

    public String getFoto() {
        return produtoCliente != null ? produtoCliente.getFoto() : null;
    }

    public Double getValorSugerido() {
        return produtoCliente != null ? produtoCliente.getValorSugerido() : null;
    }

    public void setNomeProduto(String nomeProduto) {
        if (produtoCliente == null) {
            produtoCliente = new ProdutoCliente();
        }
        produtoCliente.setNomeProduto(nomeProduto);
    }

    public void setFoto(String foto) {
        if (produtoCliente == null) {
            produtoCliente = new ProdutoCliente();
        }
        produtoCliente.setFoto(foto);
    }

    public void setValorSugerido(Double valorSugerido) {
        if (produtoCliente == null) {
            produtoCliente = new ProdutoCliente();
        }
        produtoCliente.setValorSugerido(valorSugerido);
    }

}
