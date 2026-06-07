package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Consumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsumoRepository  extends JpaRepository<Consumo,Long> {

    List<Consumo> findAllByUsuarioIdAndDataHoraBetween(Long Id, LocalDateTime inicio, LocalDateTime fim);
    Optional<Consumo> findByIdAndUsuarioId(Long id, Long usuarioId);

}
