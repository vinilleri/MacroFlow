package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaRepository  extends JpaRepository<Meta,Long> {

    Optional<Meta> findByUsuarioIdAndAtiva(Long id, boolean b);
}
