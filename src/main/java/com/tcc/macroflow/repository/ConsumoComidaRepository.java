package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.ConsumoComida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumoComidaRepository  extends JpaRepository<ConsumoComida,Long> {

       int countByConsumoId(Long consumoId);
}
