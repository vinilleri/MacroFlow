package com.tcc.macroflow.service;

import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.dto.*;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ReceitaItemRepository itemRepository;
    private final ComidaRepository comidaRepository;
    private final ComidaUsuarioRepository comidaUsuarioRepository;
    private final ReceitaItemUsuarioRepository receitaItemUsuarioRepository;
    private final AuthService authService;
    public ReceitaService(ReceitaRepository receitaRepository, ReceitaItemRepository itemRepository,
                          ComidaRepository comidaRepository, ComidaUsuarioRepository comidaUsuarioRepository,
                          ReceitaItemUsuarioRepository receitaItemUsuarioRepository, AuthService authService) {
        this.receitaRepository = receitaRepository;
        this.itemRepository = itemRepository;
        this.comidaRepository = comidaRepository;

        this.comidaUsuarioRepository = comidaUsuarioRepository;

        this.receitaItemUsuarioRepository = receitaItemUsuarioRepository;
        this.authService = authService;
    }

    @Transactional
    public ReceitaResponseDTO salvar(ReceitaDTO dto){
        Usuario usuario = authService.getUsuario();
        Receita receita = new Receita();
        receita.setNome(dto.getNome());
        receita.setUsuario(usuario);

        receitaRepository.save(receita);

        return new ReceitaResponseDTO(receita.getId(), receita.getNome());

    }
    private Receita buscarReceitaUsuario(Long receitaId){
        Usuario usuario = authService.getUsuario();
        Receita receita = receitaRepository.findById(receitaId).orElseThrow(
                () -> new RuntimeException("Receita não existente")
        );
        if(!receita.getUsuario().getId().equals(usuario.getId())){
            throw new RuntimeException("receita não pertence a usuário");
        }
        return receita;
    }


    private ReceitaItem buscarReceitaItem(Long id,Long receitaId){

        ReceitaItem item = itemRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Item não encontrado")
        );

        Receita receita = buscarReceitaUsuario(receitaId);
        if(!item.getReceita().equals(receita)){
            throw  new RuntimeException("Item não pertence a essa receita ");
        }
        return item;
    }
    private ReceitaItemUsuario buscarReceitaItemUsuario(Long id,Long receitaId){

        ReceitaItemUsuario item = receitaItemUsuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Item não encontrado")
        );

        Receita receita = buscarReceitaUsuario(receitaId);
        if(!item.getReceita().equals(receita)){
            throw  new RuntimeException("Item não pertence a essa receita ");
        }
        return item;
    }

    public ReceitaItemDTO adicionarItem(ReceitaItemRequestDTO dto, Long receitaId){
       if(dto.getOrigem() == null){throw  new RuntimeException("Origem não pode ser nula");}

           Receita receita = buscarReceitaUsuario(receitaId);

           if (Origem.SISTEMA.equals(dto.getOrigem())) {
               ReceitaItem receitaItem = adicionarItemSistema(dto.getComidaId(), receita, dto.getQuantidade(), dto.getValor());
               return new ReceitaItemDTO(receitaItem.getComida().getCalorias(),
                       receitaItem.getComida().getNome(),
                       receitaItem.getQuantidade(),
                       receitaItem.getValor(),
                       Origem.SISTEMA);
           } else {
               ReceitaItemUsuario receitaItem = adicionarItemUsuario(dto.getComidaId(), receita, dto.getQuantidade());
               return new ReceitaItemDTO(receitaItem.getComida().getCalorias(),
                       receitaItem.getComida().getNome(),
                       receitaItem.getQuantidade(),
                       receitaItem.getValor(),
                       Origem.USUARIO);
           }


    }

    @Transactional
    private ReceitaItem adicionarItemSistema(Long comidaId,Receita receita, BigDecimal quantidade, BigDecimal valor){
        if(quantidade.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }

        Comida comida = comidaRepository.findById(comidaId).orElseThrow(
                () -> new RuntimeException("Comida não existente")
        );

        Optional<ReceitaItem> buscaItem = itemRepository.findByComidaAndReceita(comida,receita);

        if(buscaItem.isPresent()){
            ReceitaItem atualizado = buscaItem.get();
            BigDecimal quantidadeNova = atualizado.getQuantidade().add(quantidade);
            atualizado.setQuantidade(quantidadeNova);
            atualizado.setValor(valor);
            return itemRepository.save(atualizado);
        }

        ReceitaItem item = new ReceitaItem();

        item.setReceita(receita);
        item.setQuantidade(quantidade);
        item.setValor(valor);
        item.setComida(comida);


        return itemRepository.save(item);

    }



    @Transactional
    private ReceitaItemUsuario adicionarItemUsuario(Long comidaUsuarioId,Receita receita, BigDecimal quantidade ){
        if(quantidade.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }


        Usuario usuario = authService.getUsuario();
        ComidaUsuario comida = comidaUsuarioRepository.findById(comidaUsuarioId).orElseThrow(
                () -> new RuntimeException("Comida não existente")
        );
        if(!comida.getUsuario().getId().equals(usuario.getId())){
            throw new RuntimeException("comida não pertence a usuário");
        }

        Optional<ReceitaItemUsuario> buscaItem = receitaItemUsuarioRepository.findByComidaAndReceita(comida,receita);

        if(buscaItem.isPresent()){
            ReceitaItemUsuario atualizado = buscaItem.get();
            BigDecimal quantidadeNova = atualizado.getQuantidade().add(quantidade);
            atualizado.setQuantidade(quantidadeNova);
            return receitaItemUsuarioRepository.save(atualizado);
        }

        ReceitaItemUsuario item = new ReceitaItemUsuario();

        item.setReceita(receita);
        item.setQuantidade(quantidade);
        item.setComida(comida);


        return receitaItemUsuarioRepository.save(item);

    }
    public ReceitaItemDTO atualizarItem(ReceitaItemRequestDTO dto, Long receitaId,Long id){
        if(dto.getOrigem() == null){throw  new RuntimeException("Origem não pode ser nula");}

        if (Origem.SISTEMA.equals(dto.getOrigem())) {
            ReceitaItem receitaItem =   atualizarItemSistema(id,dto.getQuantidade(),receitaId,dto.getValor());
            return new ReceitaItemDTO(receitaItem.getComida().getCalorias(),
                    receitaItem.getComida().getNome(),
                    receitaItem.getQuantidade(),
                    receitaItem.getValor(),
                    Origem.SISTEMA);
        }
        else{
           ReceitaItemUsuario receitaItem= atualizarItemUsuario(id,dto.getQuantidade(),receitaId,dto.getValor());
            return new ReceitaItemDTO(receitaItem.getComida().getCalorias(),
                    receitaItem.getComida().getNome(),
                    receitaItem.getQuantidade(),
                    receitaItem.getValor(),
                    Origem.USUARIO);
        }

    }

    @Transactional
    private ReceitaItem atualizarItemSistema(Long id, BigDecimal quantidadeNova,
                                             Long receitaId,BigDecimal valorNovo){

        ReceitaItem item = buscarReceitaItem(id,receitaId);

        if(quantidadeNova.compareTo(BigDecimal.ZERO) <= 0){
           itemRepository.delete(item);
            return null;
        }
        item.setQuantidade(quantidadeNova);
        item.setValor(valorNovo);
        return itemRepository.save(item);

    }
    @Transactional
    private ReceitaItemUsuario atualizarItemUsuario(Long id, BigDecimal quantidadeNova,
                                                    Long receitaId,BigDecimal valorNovo){

        ReceitaItemUsuario item = buscarReceitaItemUsuario(id,receitaId);
        if(quantidadeNova.compareTo(BigDecimal.ZERO) <= 0){
            receitaItemUsuarioRepository.delete(item);
            return null;
        }
        item.setQuantidade(quantidadeNova);
        item.setValor(valorNovo);
        return receitaItemUsuarioRepository.save(item);

    }


    public void deletarItem( Origem origem,Long receitaId,Long id){
        if(origem == null){throw  new RuntimeException("Origem não pode ser nula");}


        if (Origem.SISTEMA.equals(origem)) {
            deletarItemSistema(id,receitaId);
        }
        if (Origem.USUARIO.equals(origem)) {
            deletarItemUsuario(id,receitaId);
        }

    }


    @Transactional
    private void deletarItemSistema(Long id,Long receitaId){
        ReceitaItem item = buscarReceitaItem(id,receitaId);
         itemRepository.delete(item);
    }
    @Transactional
    private void deletarItemUsuario(Long id,Long receitaId){
        ReceitaItemUsuario item = buscarReceitaItemUsuario(id,receitaId);
        receitaItemUsuarioRepository.delete(item);
    }

    private List<ReceitaItemDTO> TransformarItemSistemaEmDTO(List<ReceitaItem> itens){

      return  itens.stream()
                .map((receitaItem) -> new ReceitaItemDTO(
                        receitaItem.getComida().getCalorias(),
                        receitaItem.getComida().getNome(),
                        receitaItem.getQuantidade(),
                        receitaItem.getValor(),
                        Origem.SISTEMA
                ))
                .toList();
    }
    private List<ReceitaItemDTO> TransformarItemUsuarioEmDTO(List<ReceitaItemUsuario> itens){

        return  itens.stream()
                .map((receitaItemUsuario) -> new ReceitaItemDTO(
                        receitaItemUsuario.getComida().getCalorias(),
                        receitaItemUsuario.getComida().getNome(),
                        receitaItemUsuario.getQuantidade(),
                        receitaItemUsuario.getValor(),
                        Origem.USUARIO
                ))
                .toList();
    }

    public List<ReceitaItemDTO> listarReceitaItem(Long receitaId){
        buscarReceitaUsuario(receitaId);


        List<ReceitaItemDTO> listaResultado = new ArrayList<>();
        List<ReceitaItem> lista = itemRepository.findAllByReceitaId(receitaId);
        List<ReceitaItemUsuario> listaUsuario = receitaItemUsuarioRepository.findAllByReceitaId(receitaId);

        listaResultado.addAll(
        TransformarItemSistemaEmDTO(lista));

        listaResultado.addAll(
        TransformarItemUsuarioEmDTO(listaUsuario)
        );

        return listaResultado;

    }

    public List<ReceitaDTO> listarReceitaUsuario(){
        Usuario usuario = authService.getUsuario();
        List<Receita> lista = receitaRepository.findAllByUsuarioId(usuario.getId());

        return lista.stream()
                .map((receita) -> new ReceitaDTO(
                        receita.getId(),
                        receita.getNome()
                ))
                .toList();
    }
    @Transactional
    public void deletarReceita(Long receitaId){
        buscarReceitaUsuario(receitaId);
        itemRepository.deleteAllByReceitaId(receitaId);
        receitaItemUsuarioRepository.deleteAllByReceitaId(receitaId);
        receitaRepository.deleteById(receitaId);
    }
    }