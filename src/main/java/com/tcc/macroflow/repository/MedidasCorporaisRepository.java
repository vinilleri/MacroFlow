package com.tcc.macroflow.repository;


import com.tcc.macroflow.model.MedidasCorporais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedidasCorporaisRepository  extends JpaRepository<MedidasCorporais,Long> {
    Optional<MedidasCorporais> findTopByUsuarioIdOrderByDataDesc(Long id);

    List<MedidasCorporais> findAllByUsuarioIdAndDataHoraBetween(Long id, LocalDateTime inicio, LocalDateTime fim);
}
