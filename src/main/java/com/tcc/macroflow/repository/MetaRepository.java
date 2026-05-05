package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaRepository  extends JpaRepository<Meta,Long> {

    Optional<Meta> findByUsuarioIdAndAtiva(Long id, boolean b);


    List<Meta> findAllByUsuarioIdAndAtiva(Long id, boolean b);

    Optional<Meta> findByUsuarioIdAndAtivaAndId(Long id, boolean b, Long objetivoId);
}
