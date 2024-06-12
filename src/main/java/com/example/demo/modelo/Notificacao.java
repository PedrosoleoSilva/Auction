package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private CadastroCliente cliente;

    @Column(name = "mensagem", length = 255)
    private String mensagem;

    @Column(name = "lida", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean lida;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "lance_id")
    private LancesEntregador lance;


    @Column(name = "tipo", length = 50)
    private String tipo;

    public Notificacao() {
    }
}
