package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.ReceitaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaItemRepository  extends JpaRepository<ReceitaItem,Long> {
}
