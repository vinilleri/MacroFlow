package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario,Long> {
}
