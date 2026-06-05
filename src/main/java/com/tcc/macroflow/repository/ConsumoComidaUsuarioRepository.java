package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.ConsumoComida;
import com.tcc.macroflow.model.ConsumoComidaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumoComidaUsuarioRepository  extends JpaRepository<ConsumoComidaUsuario,Long> {
    int countByConsumoId(Long consumoId);

    Optional<ConsumoComidaUsuario> findByConsumoId(Long consumoId);

    Optional<ConsumoComidaUsuario> findByIdAndConsumoUsuarioId(Long id, Long usuarioId);
    List<ConsumoComidaUsuario> findAllByConsumoUsuarioId(Long usuarioId);
}
