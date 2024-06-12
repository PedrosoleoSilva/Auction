package com.example.demo.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "cadastroCliente")
public class CadastroCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome não pode ser vazio!!!")
    @Column(length = 50, nullable = false)
    private String nome;

    @NotBlank(message = "O campo Email não pode ser vazio")
    @Email(message = "Forneça um email valido")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 6, message = "A senha deve ter 6 caracteres")
    @NotBlank
    @Column(nullable = false)
    private String senha;

    @NotBlank
    @Column(nullable = false)
    private String tipoUsuario;

    private String telefone;

    private String sexo;

    @Getter
    @Column(name = "expo_push_token")
    private String expoPushToken;


    public CadastroCliente() {
    }

}
