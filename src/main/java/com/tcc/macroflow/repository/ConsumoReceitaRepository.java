package com.tcc.macroflow.repository;


import com.tcc.macroflow.model.ConsumoReceita;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumoReceitaRepository  extends JpaRepository<ConsumoReceita,Long> {
    int countByConsumoId(Long consumoId);
    Optional<ConsumoReceita> findByConsumoId(Long consumoId);
}
