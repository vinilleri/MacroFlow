package com.tcc.macroflow.service;

import com.tcc.macroflow.component.TipoConsumo;
import com.tcc.macroflow.dto.ConsumoItemDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ConsumoService {
    private final ConsumoReceitaRepository consumoReceitaRepository;
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ConsumoComidaRepository consumoComidaRepository;
    private final ConsumoComidaUsuarioRepository consumoComidaUsuarioRepository;
    private final ReceitaItemRepository itemRepository;
    private final ReceitaItemUsuarioRepository itemUsuarioRepository;
    public ConsumoService(ConsumoReceitaRepository consumoReceitaRepository, ConsumoRepository consumoRepository, AuthService authService, ConsumoComidaRepository consumoComidaRepository, ConsumoComidaUsuarioRepository consumoComidaUsuarioRepository, ReceitaItemRepository itemRepository, ReceitaItemUsuarioRepository itemUsuarioRepository) {
        this.consumoReceitaRepository = consumoReceitaRepository;
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.consumoComidaRepository = consumoComidaRepository;
        this.consumoComidaUsuarioRepository = consumoComidaUsuarioRepository;
        this.itemRepository = itemRepository;
        this.itemUsuarioRepository = itemUsuarioRepository;
    }

    private List<Consumo> buscarConsumoDia() {
        Usuario usuario = authService.getUsuario();
        LocalDate hoje = LocalDate.now();
        return consumoRepository.findAllByUsuarioIdAndDataHoraBetween(usuario.getId(),
                hoje.atStartOfDay(), hoje.atTime(23, 59, 59));
    }

    private ConsumoItemDTO converterConsumoEmDTO(Consumo consumo){
        Optional<ConsumoReceita> possivelConsumoReceita = consumoReceitaRepository.findByConsumoId(consumo.getId());
        ConsumoItemDTO consumoItemDTO = new ConsumoItemDTO();
        consumoItemDTO.setDataHora(consumo.getDataHora());
        consumoItemDTO.setConsumoId(consumo.getId());
        if(possivelConsumoReceita.isPresent()){
            ConsumoReceita consumoReceita = possivelConsumoReceita.get();
            Receita receita = consumoReceita.getReceita();
            consumoItemDTO.setNome(receita.getNome());
            consumoItemDTO.setTipoConsumo(TipoConsumo.RECEITA);
            consumoItemDTO.setQuantidade(consumoReceita.getQuantidade());

            MacroDTO macroDTO = calcularReceita(receita.getId(), consumoReceita.getQuantidade());
            consumoItemDTO.setCalorias(macroDTO.getCalorias());
            consumoItemDTO.setGordura(macroDTO.getGordura());
            consumoItemDTO.setProteinas(macroDTO.getProteinas());
            consumoItemDTO.setCarboidrato(macroDTO.getCarboidrato());

            return consumoItemDTO;
        }
        Optional<ConsumoComida> possivelConsumoComida = consumoComidaRepository.findByConsumoId(consumo.getId());
        if(possivelConsumoComida.isPresent()){
            ConsumoComida consumoComida = possivelConsumoComida.get();
            Comida comida = consumoComida.getComida();

            consumoItemDTO.setNome(comida.getNome());
            consumoItemDTO.setTipoConsumo(TipoConsumo.COMIDA);
            consumoItemDTO.setCarboidrato(comida.getCarboidrato().multiply(consumoComida.getQuantidade()));
            consumoItemDTO.setGordura(comida.getGordura().multiply(consumoComida.getQuantidade()));
            consumoItemDTO.setProteinas(comida.getProteinas().multiply(consumoComida.getQuantidade()));
            consumoItemDTO.setCalorias(comida.getCalorias().multiply(consumoComida.getQuantidade()));
            consumoItemDTO.setQuantidade(consumoComida.getQuantidade());

            return consumoItemDTO;
        }
        Optional<ConsumoComidaUsuario> possivelConsumoComidaUsuario = consumoComidaUsuarioRepository.findByConsumoId(consumo.getId());
        if(possivelConsumoComidaUsuario.isPresent()){
            ConsumoComidaUsuario consumoComidaUsuario = possivelConsumoComidaUsuario.get();
            ComidaUsuario comida = consumoComidaUsuario.getComidaUsuario();

            consumoItemDTO.setNome(comida.getNome());
            consumoItemDTO.setTipoConsumo(TipoConsumo.COMIDA_USUARIO);
            consumoItemDTO.setCarboidrato(comida.getCarboidrato().multiply(consumoComidaUsuario.getQuantidade()));
            consumoItemDTO.setGordura(comida.getGordura().multiply(consumoComidaUsuario.getQuantidade()));
            consumoItemDTO.setProteinas(comida.getProteinas().multiply(consumoComidaUsuario.getQuantidade()));
            consumoItemDTO.setCalorias(comida.getCalorias().multiply(consumoComidaUsuario.getQuantidade()));
            consumoItemDTO.setQuantidade(consumoComidaUsuario.getQuantidade());

            return consumoItemDTO;
        }
       throw new RuntimeException("Consumo está sem tipo");
    }

    private MacroDTO somarMacrosSistema(ReceitaItem i,BigDecimal quantidade){
        Comida comida = i.getComida();
        BigDecimal totalCaloria = BigDecimal.ZERO;
        BigDecimal totalProteina = BigDecimal.ZERO;
        BigDecimal totalCarboidrato = BigDecimal.ZERO;
        BigDecimal totalGordura = BigDecimal.ZERO;

            totalCaloria = totalCaloria.add(comida.getCalorias().multiply(quantidade).multiply(i.getQuantidade()));
            totalGordura = totalGordura.add(comida.getGordura().multiply(quantidade).multiply(i.getQuantidade()));
            totalProteina = totalProteina.add(comida.getProteinas().multiply(quantidade).multiply(i.getQuantidade()));
            totalCarboidrato = totalCarboidrato.add(comida.getCarboidrato().multiply(quantidade).multiply(i.getQuantidade()));
        return new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);

    }
    private MacroDTO somarMacrosUsuario(ReceitaItemUsuario i,BigDecimal quantidade){
        ComidaUsuario comida = i.getComida();
        BigDecimal totalCaloria = BigDecimal.ZERO;
        BigDecimal totalProteina = BigDecimal.ZERO;
        BigDecimal totalCarboidrato = BigDecimal.ZERO;
        BigDecimal totalGordura = BigDecimal.ZERO;

        totalCaloria = totalCaloria.add(comida.getCalorias().multiply(quantidade).multiply(i.getQuantidade()));
        totalGordura = totalGordura.add(comida.getGordura().multiply(quantidade).multiply(i.getQuantidade()));
        totalProteina = totalProteina.add(comida.getProteinas().multiply(quantidade).multiply(i.getQuantidade()));
        totalCarboidrato = totalCarboidrato.add(comida.getCarboidrato().multiply(quantidade).multiply(i.getQuantidade()));
        return new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);

    }

    private MacroDTO calcularReceita(Long receitaId, BigDecimal quantidade){
        List<ReceitaItem> itemSistema = itemRepository.findAllByReceitaId(receitaId);
        MacroDTO dto = new MacroDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        if(!itemSistema.isEmpty()){
            for(ReceitaItem i: itemSistema){
               dto= MacroDTO.somarDTO(dto,somarMacrosSistema(i,quantidade));
            }
        }
        List<ReceitaItemUsuario> itemUsuario = itemUsuarioRepository.findAllByReceitaId(receitaId);

        if(!itemUsuario.isEmpty()){
            for(ReceitaItemUsuario i: itemUsuario){
             dto = MacroDTO.somarDTO(dto,somarMacrosUsuario(i,quantidade));
            }

        }
        return dto;
    }



}
