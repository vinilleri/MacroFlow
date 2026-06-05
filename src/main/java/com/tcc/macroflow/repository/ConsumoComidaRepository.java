package com.tcc.macroflow.repository;
import com.tcc.macroflow.model.ConsumoComida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumoComidaRepository  extends JpaRepository<ConsumoComida,Long> {

       int countByConsumoId(Long consumoId);
       Optional<ConsumoComida> findByConsumoId(Long consumoId);

       Optional<ConsumoComida> findByIdAndConsumoUsuarioId(  Long id,
                                                             Long usuarioId);

       List<ConsumoComida> findAllByConsumoUsuarioId(Long usuarioId);
}
