package com.tcc.macroflow.repository;


import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.ComidaUsuario;
import com.tcc.macroflow.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComidaUsuarioRepository extends JpaRepository<ComidaUsuario, Long> {
    Optional<ComidaUsuario> findByNomeIgnoreCaseAndUsuario(String nome, Usuario usuario);
    List<ComidaUsuario> findAllByUsuarioId(Long usuarioId);

}
