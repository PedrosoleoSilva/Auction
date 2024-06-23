package com.example.demo.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "ProdutoCliente")
public class ProdutoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CadastroCliente cliente;

    @OneToMany(mappedBy = "produtoCliente", fetch = FetchType.LAZY)
    private List<LancesEntregador> lances;

    @Column(length = 60)
    private String nomeProduto;

    @Column(name = "Valor_Sugerido")
    private double valorSugerido;

    @Column(length = 100)
    @NotBlank(message = "O campo descricao não pode ser vazio")
    private String descricao;

    @Column(length = 30, name = "Peso_Produto")
    private String peso;

    @Column(name = "largura_produto")
    private String largura;

    @Column(name = "comprimento_produto")
    private String comprimento;

    @NotBlank(message = "O campo localOrigemEstado não pode ser vazio")
    private String localOrigemEstado;

    @Column(length = 50)
    @NotBlank(message = "O campo localOrigemCidade não pode ser vazio")
    private String localOrigemCidade;

    @Column(length = 50)
    @NotBlank(message = "O campo destinoEstado não pode ser vazio")
    private String destinoEstado;

    @Column(length = 50)
    @NotBlank(message = "O campo destinoCidade não pode ser vazio")
    private String destinoCidade;

    @Column(name = "data_postagem")
    private LocalDateTime dataPostagem;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name= "FOTO", nullable = false, columnDefinition = "LONGTEXT")
    private String  foto;

    private boolean ofertaAceita = false;

    public ProdutoCliente() {
    }


}
