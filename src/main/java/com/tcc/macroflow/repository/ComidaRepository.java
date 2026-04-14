package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComidaRepository  extends JpaRepository<Comida,Long> {


}
