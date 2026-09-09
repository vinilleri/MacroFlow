package com.tcc.macroflow.repository;


import com.tcc.macroflow.model.MedidasCorporais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedidasCorporaisRepository  extends JpaRepository<MedidasCorporais,Long> {
    List<MedidasCorporais> findAllByUsuarioIdAndDataBetween(Long id, LocalDate inicio, LocalDate fim);
    Optional<MedidasCorporais> findTopByUsuarioIdOrderByDataDescIdDesc(Long id);

    boolean existsByUsuarioId(Long id);
}
