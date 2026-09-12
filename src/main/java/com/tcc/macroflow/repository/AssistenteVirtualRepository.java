package com.tcc.macroflow.repository;

import com.tcc.macroflow.model.AssistenteVirtual;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssistenteVirtualRepository extends JpaRepository<AssistenteVirtual,Long> {

    List<AssistenteVirtual> findTop5ByUsuarioIdOrderByDataHoraDesc(Long usuarioId);

    List<AssistenteVirtual> findAllByUsuarioIdOrderByDataHoraAsc(Long usuarioId);


    void deleteByUsuarioId(Long usuario);

    void deleteAllByUsuarioId(Long usuarioId);

    @Query("SELECT DISTINCT a.usuario.id FROM AssistenteVirtual a")
    List<Long> findDistincUsuarioIds();
}
