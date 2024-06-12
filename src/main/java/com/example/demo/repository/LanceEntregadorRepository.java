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



    @Query("SELECT l FROM LancesEntregador l WHERE l.cliente.id = :clienteId")
    List<LancesEntregador> listarProdutosComOfertas(@Param("clienteId") Integer clienteId);

    @Query("SELECT l FROM LancesEntregador l JOIN FETCH l.produtoCliente p JOIN FETCH p.cliente c WHERE p.cliente.id = :clienteId")
    List<LancesEntregador> findLancesWithProductDetailsByClienteId(@Param("clienteId") Integer clienteId);
    @Query("SELECT l FROM LancesEntregador l JOIN FETCH l.produtoCliente WHERE l.cliente.id = :clienteId")
    List<LancesEntregador> findByProdutoCliente_Cliente_Id(@Param("clienteId") Integer clienteId);

    @Query("SELECT DISTINCT l.produtoCliente FROM LancesEntregador l WHERE l.produtoCliente.cliente.id = :clienteId")
    List<ProdutoCliente> findProdutosComOfertasRecebidasPorCliente(@Param("clienteId") Integer clienteId);

    @Modifying
    @Query("DELETE FROM LancesEntregador l WHERE l.produtoCliente.id = :produtoClienteId")
    void deleteByProdutoClienteId(@Param("produtoClienteId") Integer produtoClienteId);

    @Query("SELECT l FROM LancesEntregador l WHERE l.cliente.id = :clienteId AND l.status = 'Aceito'")
    List<LancesEntregador> findByClienteIdAndStatus(@Param("clienteId") Integer clienteId);


    List<LancesEntregador> findByClienteId(Integer clienteId);
}