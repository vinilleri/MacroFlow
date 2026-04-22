package com.tcc.macroflow.repository;


import com.tcc.macroflow.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceitaRepository  extends JpaRepository<Receita,Long> {
    List<Receita> findAllByUsuarioId(Long usuarioId);
}
