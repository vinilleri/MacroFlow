package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObjetivoRepository   extends JpaRepository<Objetivo,Long> {

    Optional<Objetivo> findByUsuarioIdAndAtivo(Long usuarioId,Boolean ativo);
    Optional<Objetivo> findByUsuarioIdAndAtivoAndId(Long usuarioId,Boolean ativo,Long Id);
    List<Objetivo> findAllByUsuarioIdAndAtivo(Long usuarioId,Boolean ativo);

    boolean existsByUsuarioId(Long id);
}
