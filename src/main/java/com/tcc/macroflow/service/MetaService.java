package com.tcc.macroflow.service;

import com.tcc.macroflow.enums.Sexo;
import com.tcc.macroflow.enums.TipoMeta;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.dto.MetaDTO;
import com.tcc.macroflow.dto.MetaResponseDTO;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.MedidasCorporaisRepository;
import com.tcc.macroflow.repository.MetaRepository;
import com.tcc.macroflow.repository.ObjetivoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class MetaService {

    private final MetaRepository metaRepository;
    private final AuthService service;
    private final MedidasCorporaisRepository medidasCorporaisRepository;
    private final ObjetivoRepository objetivoRepository;
    public MetaService(MetaRepository metaRepository, AuthService service, MedidasCorporaisRepository medidasCorporaisRepository, ObjetivoRepository objetivoRepository) {
        this.metaRepository = metaRepository;
        this.service = service;
        this.medidasCorporaisRepository = medidasCorporaisRepository;
        this.objetivoRepository = objetivoRepository;
    }


    private MetaResponseDTO converterMetaEmDTO(Meta meta){
        return new MetaResponseDTO(meta.getCalorias(),meta.getProteinas(),meta.getCarboidrato(),meta.getGordura()
                , meta.getDataInicio(), meta.getDataFim(), meta.getObjetivo().getId(), meta.getTipoMeta());
    }

    private MacroDTO calcularMeta(Usuario usuario, TipoObjetivo objetivo) {
        MedidasCorporais medidasCorporais = medidasCorporaisRepository.findTopByUsuarioIdOrderByDataDesc(usuario.getId()).orElseThrow(
                () -> new RuntimeException("Usuário sem medidas corporais")
        );
        BigDecimal peso = medidasCorporais.getPeso();
        BigDecimal altura = BigDecimal.valueOf(medidasCorporais.getAltura());
        BigDecimal idade = BigDecimal.valueOf(Period.between(medidasCorporais.getDataNascimento(), LocalDate.now()).getYears());
        Sexo sexo = medidasCorporais.getSexo();
        BigDecimal tmb = peso.multiply(BigDecimal.TEN)
                .add(altura.multiply(BigDecimal.valueOf(6.25)))
                .subtract(idade.multiply(BigDecimal.valueOf(5)));


        if (sexo == Sexo.MASCULINO) {
            tmb = tmb.add(BigDecimal.valueOf(5));
        } else {
            tmb = tmb.subtract(BigDecimal.valueOf(161));
        }
        BigDecimal tdee = tmb.multiply(usuario.getAtividadeFisica()
                .getFatorMultiplicador());

        BigDecimal totalCaloria = tdee.multiply(objetivo.getMultiplicadorCalorico()).setScale(2, RoundingMode.HALF_UP);;

        BigDecimal totalProteina = peso.multiply(objetivo.getMultiplicadorProteina()).setScale(2, RoundingMode.HALF_UP);;

        BigDecimal totalGordura = peso.multiply(objetivo.getMultiplicadorGordura()).setScale(2, RoundingMode.HALF_UP);;

        BigDecimal caloriasUsadas = totalProteina.multiply(BigDecimal.valueOf(4))
                                        .add(totalGordura.multiply(BigDecimal.valueOf(9)))
                                        .setScale(2, RoundingMode.HALF_UP);;

        if(totalCaloria.compareTo(caloriasUsadas) <=0){
            throw  new RuntimeException("Meta inválida para uso");
        }
        BigDecimal totalCarboidrato = totalCaloria.subtract(caloriasUsadas)
                .divide(BigDecimal.valueOf(4),2, RoundingMode.HALF_UP);

        return new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);

    }

    public MetaResponseDTO criarMeta(MetaDTO dto){
        Meta meta = new Meta();
        MacroDTO macroDTO;
        Usuario usuario = service.getUsuario();
        meta.setUsuario(usuario);

        Optional<Meta> metaAnterior = metaRepository.findByUsuarioIdAndAtiva(usuario.getId(),true);

        if(metaAnterior.isPresent()){
            Meta antigaMeta = metaAnterior.get();

            if(antigaMeta.getDataFim() == null || antigaMeta.getDataFim().isAfter(LocalDate.now())){
                antigaMeta.setDataFim(LocalDate.now());
                antigaMeta.setAtiva(false);
                metaRepository.save(antigaMeta);
            }
        }


        Objetivo objetivo = objetivoRepository
                .findByUsuarioIdAndAtivoAndId(usuario.getId(), true, dto.getObjetivoId()).orElseThrow(
                () -> new RuntimeException("Objetivo não encontrado ou não pertence ao usuário")
        );

        meta.setObjetivo(objetivo);
        meta.setAtiva(true);
        meta.setDataInicio(LocalDate.now());
        if(dto.getDataFim() != null){
            if(dto.getDataFim().isBefore(LocalDate.now())){
                throw new RuntimeException("Data final não pode ser antes do inicio");
            }
            meta.setDataFim(dto.getDataFim());
        }
        meta.setTipoMeta(dto.getTipoMeta());

        if(dto.getTipoMeta() == TipoMeta.CALCULADA){
            macroDTO = calcularMeta(usuario,objetivo.getTipoObjetivo());
            meta.setCalorias(macroDTO.getCalorias());
            meta.setCarboidrato(macroDTO.getCarboidrato());
            meta.setProteinas(macroDTO.getProteinas());
            meta.setGordura(macroDTO.getGordura());
        }
        else {
            throw new RuntimeException("Tipo de meta ainda não implementado");
        }
        metaRepository.save(meta);
        return converterMetaEmDTO(meta);
    }


}
