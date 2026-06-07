package com.tcc.macroflow.service;


import com.tcc.macroflow.dto.ConsumoResultanteDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.enums.TipoConsumo;
import com.tcc.macroflow.dto.ConsumoItemDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.helper.CalcularQuantidade;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsumoService {
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ReceitaItemRepository itemRepository;
    private final ReceitaItemUsuarioRepository itemUsuarioRepository;
    private final ComidaRepository comidaRepository;
    private final ComidaUsuarioRepository comidaUsuarioRepository;
    public ConsumoService(ConsumoRepository consumoRepository, AuthService authService, ReceitaItemRepository itemRepository,
                          ReceitaItemUsuarioRepository itemUsuarioRepository, ComidaRepository comidaRepository, ComidaUsuarioRepository comidaUsuarioRepository) {
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.itemRepository = itemRepository;
        this.itemUsuarioRepository = itemUsuarioRepository;
        this.comidaRepository = comidaRepository;
        this.comidaUsuarioRepository = comidaUsuarioRepository;
    }

    private List<Consumo> buscarConsumoDia() {
        Usuario usuario = authService.getUsuario();
        LocalDate hoje = LocalDate.now();
        return consumoRepository.findAllByUsuarioIdAndDataHoraBetween(usuario.getId(),
                hoje.atStartOfDay(), hoje.atTime(23, 59, 59));
    }

    private List<Consumo> buscarConsumoPorPeriodo(LocalDateTime inicio,LocalDateTime fim){
        Usuario usuario = authService.getUsuario();

        if(inicio.isAfter(fim)){
        throw new RuntimeException("Data invalida! O começo do período não pode vir depois do fim");
        }
        return consumoRepository.findAllByUsuarioIdAndDataHoraBetween(usuario.getId(), inicio,fim);
    }

    public Consumo consumirItem(ConsumoItemDTO consumoItem){
        if(consumoItem.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }
        Usuario usuario = authService.getUsuario();
        Consumo consumo = new Consumo();
        consumo.setDataHora(LocalDateTime.now());
        consumo.setUsuario(usuario);
        consumo.setNome(consumoItem.getNome());
        return getConsumo(consumoItem, consumo, consumoItem.getQuantidade());
    }

    private Consumo getConsumo(ConsumoItemDTO consumoItem, Consumo consumo, BigDecimal quantidade) {
        MacroDTO macros;
        ConsumoResultanteDTO consumoResultanteDTO;
        if(TipoConsumo.RECEITA.equals(consumoItem.getTipoConsumo())){
            consumo.setQuantidade(quantidade);
            macros = calcularReceita(consumoItem.getIdReceitaOuComida(),consumoItem.getQuantidade());

        }
        else if(TipoConsumo.COMIDA.equals(consumoItem.getTipoConsumo())) {
            consumoResultanteDTO = calcularComida(consumoItem.getIdReceitaOuComida(),consumoItem.getQuantidade(),consumoItem.getValor(), Origem.SISTEMA);
            macros = consumoResultanteDTO.getMacroDTO();
            consumo.setQuantidade(consumoResultanteDTO.getQuantidade());
        }
        else{
            consumoResultanteDTO = calcularComida(consumoItem.getIdReceitaOuComida(),consumoItem.getQuantidade(),consumoItem.getValor(), Origem.USUARIO);
            macros = consumoResultanteDTO.getMacroDTO();
            consumo.setQuantidade(consumoResultanteDTO.getQuantidade());
        }

        consumo.setCalorias(macros.getCalorias());
        consumo.setGordura(macros.getGordura());
        consumo.setCarboidrato(macros.getCarboidrato());
        consumo.setProteinas(macros.getProteinas());

        return consumoRepository.save(consumo);
    }

    @Transactional
    public Consumo editarConsumo(ConsumoItemDTO consumoItem, Long id){
        Usuario usuario = authService.getUsuario();
        Consumo atualizado = consumoRepository.findByIdAndUsuarioId(id, usuario.getId()).orElseThrow(
                () -> new RuntimeException("Consumo não encontrado, ou não pertence ao usuário")
        );
        return getConsumo(consumoItem, atualizado, consumoItem.getQuantidade());
    }

        @Transactional
    public void deletarConsumo(Long id){
        Usuario usuario = authService.getUsuario();
        Consumo consumo = consumoRepository.findByIdAndUsuarioId(id, usuario.getId()).orElseThrow(
                () -> new RuntimeException("Consumo não encontrado, ou não pertence ao usuário")
        );


        consumoRepository.delete(consumo);

    }

    private ConsumoResultanteDTO calcularComida(Long comidaId, BigDecimal quantidade, BigDecimal valor, Origem origem){

        BigDecimal totalCaloria = BigDecimal.ZERO;
        BigDecimal totalProteina = BigDecimal.ZERO;
        BigDecimal totalCarboidrato = BigDecimal.ZERO;
        BigDecimal totalGordura = BigDecimal.ZERO;
        BigDecimal quantidadeFinal;


        if(Origem.SISTEMA.equals(origem)) {
            Comida comida = comidaRepository.findById(comidaId).orElseThrow(
                    () -> new RuntimeException("Comida não encontrada")
                    );
             quantidadeFinal = CalcularQuantidade.calcularQuantidade(quantidade, valor
                    , CalcularQuantidade.converterComidaEmDto(comida));
            return getMacroDTO(comida, totalCaloria, totalProteina, totalCarboidrato, totalGordura, quantidadeFinal);

        }
        else{
            ComidaUsuario comida = comidaUsuarioRepository.findById(comidaId).orElseThrow(
                    () -> new RuntimeException("Comida não encontrada")
            );
            quantidadeFinal = CalcularQuantidade.calcularQuantidade(quantidade, valor
                    , CalcularQuantidade.converterComidaUsuarioEmDto(comida));
            return getMacroDTOUsuario(comida, totalCaloria, totalProteina, totalCarboidrato, totalGordura, quantidadeFinal);
        }

    }


    private ConsumoResultanteDTO getMacroDTO(Comida comida, BigDecimal totalCaloria, BigDecimal totalProteina, BigDecimal totalCarboidrato, BigDecimal totalGordura, BigDecimal quantidadeFinal) {
        return getConsumoResultante(totalCaloria, totalProteina, totalCarboidrato, totalGordura, quantidadeFinal, comida.getCalorias(), comida.getGordura(), comida.getProteinas(), comida.getCarboidrato());
    }

    private ConsumoResultanteDTO getMacroDTOUsuario(ComidaUsuario comida, BigDecimal totalCaloria, BigDecimal totalProteina, BigDecimal totalCarboidrato, BigDecimal totalGordura, BigDecimal quantidadeFinal) {
        return getConsumoResultante(totalCaloria, totalProteina, totalCarboidrato, totalGordura, quantidadeFinal, comida.getCalorias(), comida.getGordura(), comida.getProteinas(), comida.getCarboidrato());
    }


    private ConsumoResultanteDTO getConsumoResultante(BigDecimal totalCaloria, BigDecimal totalProteina, BigDecimal totalCarboidrato,
                                             BigDecimal totalGordura, BigDecimal quantidadeFinal, BigDecimal calorias,
                                             BigDecimal gordura, BigDecimal proteinas, BigDecimal carboidrato) {
        totalCaloria = totalCaloria.add(calorias.multiply(quantidadeFinal));
        totalGordura = totalGordura.add(gordura.multiply(quantidadeFinal));
        totalProteina = totalProteina.add(proteinas.multiply(quantidadeFinal));
        totalCarboidrato = totalCarboidrato.add(carboidrato.multiply(quantidadeFinal));
        MacroDTO macroDTO = new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);

        return new ConsumoResultanteDTO(macroDTO, quantidadeFinal);
    }


    private MacroDTO somarMacrosSistema(ReceitaItem i, BigDecimal quantidade){
        Comida comida = i.getComida();
        BigDecimal totalCaloria = BigDecimal.ZERO;
        BigDecimal totalProteina = BigDecimal.ZERO;
        BigDecimal totalCarboidrato = BigDecimal.ZERO;
        BigDecimal totalGordura = BigDecimal.ZERO;

        BigDecimal quantidadeTotal = quantidade.multiply(i.getQuantidade());
        BigDecimal quantidadeFinal = CalcularQuantidade.calcularQuantidade(quantidadeTotal, i.getValor()
                ,CalcularQuantidade.converterComidaEmDto(comida));
        return getMacroDTO(totalCaloria, totalProteina, totalCarboidrato, totalGordura, quantidadeFinal, comida.getCalorias(), comida.getGordura(), comida.getProteinas(), comida.getCarboidrato());

    }


    private MacroDTO getMacroDTO(BigDecimal totalCaloria, BigDecimal totalProteina, BigDecimal totalCarboidrato, BigDecimal totalGordura, BigDecimal quantidadeFinal, BigDecimal calorias, BigDecimal gordura, BigDecimal proteinas, BigDecimal carboidrato) {
        totalCaloria = totalCaloria.add(calorias.multiply(quantidadeFinal));
        totalGordura = totalGordura.add(gordura.multiply(quantidadeFinal));
        totalProteina = totalProteina.add(proteinas.multiply(quantidadeFinal));
        totalCarboidrato = totalCarboidrato.add(carboidrato.multiply(quantidadeFinal));
        return new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);
    }

    private MacroDTO somarMacrosUsuario(ReceitaItemUsuario i,
                                        BigDecimal quantidade){
        ComidaUsuario comida = i.getComida();
        BigDecimal totalCaloria = BigDecimal.ZERO;
        BigDecimal totalProteina = BigDecimal.ZERO;
        BigDecimal totalCarboidrato = BigDecimal.ZERO;
        BigDecimal totalGordura = BigDecimal.ZERO;

        BigDecimal quantidadeTotal = quantidade.multiply(i.getQuantidade());
        BigDecimal quantidadeFinal = CalcularQuantidade.calcularQuantidade(quantidadeTotal, i.getValor()
                ,CalcularQuantidade.converterComidaUsuarioEmDto(comida));
        return getMacroDTO(totalCaloria, totalProteina, totalCarboidrato, totalGordura, quantidadeFinal, comida.getCalorias(),
                comida.getGordura(), comida.getProteinas(), comida.getCarboidrato());

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
        if(itemSistema.isEmpty() && itemUsuario.isEmpty()){
            throw new RuntimeException("Receita vazia");
        }
        return dto;
    }



        public List<Consumo> listarConsumoDia(){
        return buscarConsumoDia();

        }

        public List<Consumo> listarConsumoPeriodo(LocalDateTime inicio, LocalDateTime fim){
        return buscarConsumoPorPeriodo(inicio,fim);
        }


        public MacroDTO somarConsumoDia(){
            MacroDTO total = new MacroDTO(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );

            for(Consumo consumo: buscarConsumoDia()){

                MacroDTO atual = new MacroDTO(consumo.getCalorias(),
                        consumo.getProteinas(),
                        consumo.getCarboidrato(),
                        consumo.getGordura()
                        );

                total = MacroDTO.somarDTO(total,atual);


            }
            return total;
        }
    public MacroDTO somarConsumoPeriodo(LocalDateTime inicio, LocalDateTime fim){
        MacroDTO total = new MacroDTO(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        for(Consumo consumo: buscarConsumoPorPeriodo(inicio,fim)){


            MacroDTO atual = new MacroDTO(consumo.getCalorias(),
                    consumo.getProteinas(),
                    consumo.getCarboidrato(),
                    consumo.getGordura()
            );

            total = MacroDTO.somarDTO(total,atual);


        }
        return total;
    }

}
