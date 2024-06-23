package com.example.demo.repository;

import com.example.demo.modelo.ProdutoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoClienteRepository extends JpaRepository<ProdutoCliente, Integer> {
    List<ProdutoCliente> findByClienteId(Integer clienteId);



    // Novo método para buscar todos os produtos de um cliente específico
    List<ProdutoCliente> findAllByClienteId(@Param("clienteId") Integer clienteId);

    @Query("SELECT p FROM ProdutoCliente p LEFT JOIN FETCH p.lances WHERE p.cliente.id = :clienteId AND p.lances IS NOT EMPTY")
    List<ProdutoCliente> findProdutosPorClienteComOfertas(@Param("clienteId") Integer clienteId);

    @Query("SELECT p FROM ProdutoCliente p LEFT JOIN FETCH p.lances l WHERE p.cliente.id = :clienteId")
    List<ProdutoCliente> findByClienteIdWithLances(Integer clienteId);


    List<ProdutoCliente> findByOfertaAceitaFalse();


}