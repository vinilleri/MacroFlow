package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.*;
import com.tcc.macroflow.enums.FuncaoMeta;
import com.tcc.macroflow.enums.Sexo;
import com.tcc.macroflow.enums.TipoMeta;
import com.tcc.macroflow.helper.ValidacaoMacro;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.MedidasCorporaisRepository;
import com.tcc.macroflow.repository.MetaRepository;
import com.tcc.macroflow.repository.ObjetivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
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
        return new MetaResponseDTO(meta.getId(),meta.getCalorias(),meta.getProteinas(),meta.getCarboidrato(),meta.getGordura()
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

        if(usuario.getAtividadeFisica() == null) {
            throw new RuntimeException("Atividade física vazia");
        }

        if (sexo == Sexo.MASCULINO) {
            tmb = tmb.add(BigDecimal.valueOf(5));
        } else {
            tmb = tmb.subtract(BigDecimal.valueOf(161));
        }
        BigDecimal tdee = tmb.multiply(usuario.getAtividadeFisica()
                .getFatorMultiplicador());

        BigDecimal totalCaloria = tdee.multiply(objetivo.getMultiplicadorCalorico()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalProteina = peso.multiply(objetivo.getMultiplicadorProteina()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalGordura = peso.multiply(objetivo.getMultiplicadorGordura()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal caloriasUsadas = totalProteina.multiply(BigDecimal.valueOf(4))
                                        .add(totalGordura.multiply(BigDecimal.valueOf(9)))
                                        .setScale(2, RoundingMode.HALF_UP);

        if(totalCaloria.compareTo(caloriasUsadas) <=0){
            throw  new RuntimeException("Meta inválida para uso");
        }
        BigDecimal totalCarboidrato = totalCaloria.subtract(caloriasUsadas)
                .divide(BigDecimal.valueOf(4),2, RoundingMode.HALF_UP);

        return new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);
    }

    private Meta atribuirMeta(MetaDTO dto, Usuario usuario, FuncaoMeta funcao){
        Meta meta;
        if(funcao == FuncaoMeta.CRIAR){
            meta = new Meta();
        }
        else{
            meta = metaRepository.findByUsuarioIdAndAtiva(usuario.getId(), true).orElseThrow(
                    () -> new RuntimeException("Meta atual não encontrada")
            );
        }
        MacroDTO macroDTO;
        meta.setUsuario(usuario);
        Objetivo objetivo = objetivoRepository
                .findByUsuarioIdAndAtivoAndId(usuario.getId(), true, dto.getObjetivoId()).orElseThrow(
                        () -> new RuntimeException("Objetivo não encontrado ou não pertence ao usuário")
                );
        if(funcao == FuncaoMeta.CRIAR) {
            meta.setObjetivo(objetivo);
        }
        if(!objetivo.isAtivo()){
          throw  new RuntimeException("Nenhum objetivo ativo no momento, certifique-se de ter um objetivo criado");
        }
        meta.setAtiva(true);
        if(funcao == FuncaoMeta.CRIAR){
            meta.setDataInicio(LocalDate.now());
        }
        if(objetivo.getDataFim() != null){
            meta.setDataFim(objetivo.getDataFim());
        }
        meta.setTipoMeta(dto.getTipoMeta());

        if(dto.getTipoMeta() == TipoMeta.CALCULADA){
            macroDTO = calcularMeta(usuario,objetivo.getTipoObjetivo());
            meta.setCalorias(macroDTO.getCalorias());
            meta.setCarboidrato(macroDTO.getCarboidrato());
            meta.setProteinas(macroDTO.getProteinas());
            meta.setGordura(macroDTO.getGordura());
        }
        else if(dto.getTipoMeta() == TipoMeta.MANUAL) {
            ValidacaoMacro.validarMacro(dto);

            meta.setCalorias(dto.getCalorias());
            meta.setCarboidrato(dto.getCarboidrato());
            meta.setProteinas(dto.getProteinas());
            meta.setGordura(dto.getGordura());
        }
        else {
            throw new RuntimeException("Tipo de meta ainda não implementado");
        }

        return meta;
    }

    public MetaResponseDTO criarMeta(MetaDTO dto){
        Usuario usuario = service.getUsuario();
        Optional<Meta> metaAnterior = metaRepository.findByUsuarioIdAndAtiva(usuario.getId(),true);

        if(metaAnterior.isPresent()){
            Meta antigaMeta = metaAnterior.get();

            if(antigaMeta.getDataFim() == null || antigaMeta.getDataFim().isAfter(LocalDate.now())){
                antigaMeta.setDataFim(LocalDate.now());
                antigaMeta.setAtiva(false);
                metaRepository.save(antigaMeta);
            }
        }
        Meta meta = atribuirMeta(dto,usuario, FuncaoMeta.CRIAR);
        metaRepository.save(meta);
        return converterMetaEmDTO(meta);
    }

    public MetaResponseDTO buscarMetaAtual(){
        Usuario usuario = service.getUsuario();

        Meta meta = metaRepository.findByUsuarioIdAndAtiva(usuario.getId(), true).orElseThrow(
                () -> new RuntimeException("Meta atual não encontrada")
        );

        return converterMetaEmDTO(meta);
    }

    public List<MetaResponseDTO> listarMetasAntigas(){
        Usuario usuario = service.getUsuario();
            List<Meta> lista = metaRepository.findAllByUsuarioIdAndAtiva(usuario.getId(), false);

           return lista.stream()
                    .map(this:: converterMetaEmDTO)
                    .toList();
    }

    public void desativarMeta( ){
        Usuario usuario = service.getUsuario();
        Meta meta = metaRepository.findByUsuarioIdAndAtiva(usuario.getId(), true).orElseThrow(
                () -> new RuntimeException("Meta atual não encontrada")
        );

        meta.setAtiva(false);
        meta.setDataFim(LocalDate.now());
        metaRepository.save(meta);
    }

    @Transactional
    public void deletarMetaAntiga(Long metaId){
        Usuario usuario = service.getUsuario();
        Meta meta = metaRepository.findByUsuarioIdAndAtivaAndId(usuario.getId(), false, metaId).orElseThrow(
                () -> new RuntimeException("Meta ativa ou não encontrada")
        );

        metaRepository.delete(meta);

    }

    @Transactional
    public MetaResponseDTO atualizarMeta(MetaDTO dto) {
        Usuario usuario = service.getUsuario();
        Meta atualizado = atribuirMeta(dto,usuario,FuncaoMeta.ATUALIZAR);
            metaRepository.save(atualizado);
            return converterMetaEmDTO(atualizado);

    }
}
