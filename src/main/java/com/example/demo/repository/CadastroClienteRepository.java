package com.example.demo.repository;

import com.example.demo.modelo.CadastroCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroClienteRepository extends JpaRepository<CadastroCliente, Integer> {

    Optional<CadastroCliente> findByEmail(String email);

}