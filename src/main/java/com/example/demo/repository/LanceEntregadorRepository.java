package com.example.demo.repository;

import com.example.demo.modelo.LancesEntregador;
import com.example.demo.modelo.ProdutoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanceEntregadorRepository extends JpaRepository<LancesEntregador, Integer> {

    List<LancesEntregador> findByProdutoClienteId(Integer idProduto);

    //query encontra todos os lances feitos para um determinado produto do cliente e os detalhes produto
    @Query("SELECT l FROM LancesEntregador l JOIN FETCH l.produtoCliente p JOIN FETCH p.cliente c WHERE p.cliente.id = :clienteId")
    List<LancesEntregador> findLancesWithProductDetailsByClienteId(@Param("clienteId") Integer clienteId);
    @Modifying  // query deleta todos os lances associados a um produto.
    @Query("DELETE FROM LancesEntregador l WHERE l.produtoCliente.id = :produtoClienteId")
    void deleteByProdutoClienteId(@Param("produtoClienteId") Integer produtoClienteId);

    //encontra todos os lances aceitos por um cliente para retornar ao entregador as ofertas aceitas
    @Query("SELECT l FROM LancesEntregador l WHERE l.cliente.id = :clienteId AND l.status = 'Aceito'")
    List<LancesEntregador> findByClienteIdAndStatus(@Param("clienteId") Integer clienteId);


    List<LancesEntregador> findByClienteId(Integer clienteId);
}