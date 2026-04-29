package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.Receita;
import com.tcc.macroflow.model.ReceitaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceitaItemRepository  extends JpaRepository<ReceitaItem,Long> {


    Optional<ReceitaItem> findByComidaAndReceita(Comida comida, Receita receita);
    List<ReceitaItem> findAllByReceitaId(Long id);
    void deleteAllByReceitaId(Long receitaId);
}
