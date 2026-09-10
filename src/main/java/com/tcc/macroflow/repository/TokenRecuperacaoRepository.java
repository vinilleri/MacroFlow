package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.TokenRecuperacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoRepository extends JpaRepository<TokenRecuperacao,Long> {

   Optional<TokenRecuperacao> findByCodigo(String codigo);

    Optional<TokenRecuperacao> findTopByUsuarioIdOrderByDataCriacaoDesc(Long id);
}
