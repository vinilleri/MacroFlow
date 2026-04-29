package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.ConsumoDiaResponseDTO;
import com.tcc.macroflow.dto.ConsumoItemDTO;
import com.tcc.macroflow.model.Consumo;
import com.tcc.macroflow.model.ConsumoReceita;
import com.tcc.macroflow.model.Receita;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.ConsumoComidaRepository;
import com.tcc.macroflow.repository.ConsumoComidaUsuarioRepository;
import com.tcc.macroflow.repository.ConsumoReceitaRepository;
import com.tcc.macroflow.repository.ConsumoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ConsumoService {
    private final ConsumoReceitaRepository consumoReceitaRepository;
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ConsumoComidaRepository consumoComidaRepository;
    private final ConsumoComidaUsuarioRepository consumoComidaUsuarioRepository;
    public ConsumoService(ConsumoReceitaRepository consumoReceitaRepository, ConsumoRepository consumoRepository, AuthService authService, ConsumoComidaRepository consumoComidaRepository, ConsumoComidaUsuarioRepository consumoComidaUsuarioRepository) {
        this.consumoReceitaRepository = consumoReceitaRepository;
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.consumoComidaRepository = consumoComidaRepository;
        this.consumoComidaUsuarioRepository = consumoComidaUsuarioRepository;
    }

    private List<Consumo> buscarConsumoDia() {
        Usuario usuario = authService.getUsuario();
        LocalDate hoje = LocalDate.now();
        return consumoRepository.findAllByUsuarioIdAndDataHoraBetween(usuario.getId(),
                hoje.atStartOfDay(), hoje.atTime(23, 59, 59));
    }

    private ConsumoItemDTO converterConsumoEmDTO(Consumo consumo){
        Optional<ConsumoReceita> consumoReceita = consumoReceitaRepository.findByConsumoId(consumo.getId());

        if(consumoReceita.isPresent()){
            ConsumoItemDTO consumoItemDTO = new ConsumoItemDTO();
            Receita receita = consumoReceita.get().getReceita();
            consumoItemDTO.setNome(receita.getNome());
        }
        return null;
    }
}
