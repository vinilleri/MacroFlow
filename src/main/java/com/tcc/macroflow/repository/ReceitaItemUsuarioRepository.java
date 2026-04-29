package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceitaItemUsuarioRepository extends JpaRepository<ReceitaItemUsuario,Long> {


    Optional<ReceitaItemUsuario> findByComidaAndReceita(ComidaUsuario comida, Receita receita);
    List<ReceitaItemUsuario> findAllByReceitaId(Long id);
    void deleteAllByReceitaId(Long receitaId);
}


