package com.example.demo.repository;

import com.example.demo.modelo.Notificacao;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    List<Notificacao> findByClienteId(Integer clienteId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notificacao n WHERE n.lance.id = :lanceId")
    void deleteByLanceId(@Param("lanceId") Integer lanceId);

}
