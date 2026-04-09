package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.CodigoEmail;


import com.tcc.macroflow.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<CodigoEmail,Long> {

    Optional<CodigoEmail> findByUsuario(Usuario usuario);
}
