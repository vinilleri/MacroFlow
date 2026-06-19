package com.tcc.macroflow.component;

import com.tcc.macroflow.enums.Icone;
import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.Unidade;
import com.tcc.macroflow.model.TipoObjetivo;
import com.tcc.macroflow.repository.AtividadeFisicaRepository;
import com.tcc.macroflow.repository.ComidaRepository;
import com.tcc.macroflow.repository.UnidadeRepository;
import com.tcc.macroflow.repository.TipoObjetivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AtividadeFisicaRepository atividadeFisicaRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private TipoObjetivoRepository tipoObjetivoRepository;

    @Override
    public void run(String... args) throws Exception {
        
        if (atividadeFisicaRepository.count() == 0) {
            atividadeFisicaRepository.saveAll(Arrays.asList(
                    new AtividadeFisica(1L, "Sedentario", BigDecimal.valueOf(1.2)),
                    new AtividadeFisica(2L, "Levemente Ativo", BigDecimal.valueOf(1.375)),
                    new AtividadeFisica(3L, "Moderadamente Ativo", BigDecimal.valueOf(1.55)),
                    new AtividadeFisica(4L, "Muito Ativo", BigDecimal.valueOf(1.725)),
                    new AtividadeFisica(5L, "Extremamente Ativo", BigDecimal.valueOf(1.9))
            ));
        }


        Map<Long, Unidade> unidadesMap = new HashMap<>();

        if (unidadeRepository.count() == 0) {
            unidadesMap.put(1L, unidadeRepository.save(new Unidade(1L, "Grama", "g")));
            unidadesMap.put(2L, unidadeRepository.save(new Unidade(2L, "Mililitro", "ml")));
            unidadesMap.put(3L, unidadeRepository.save(new Unidade(3L, "Unidade", "un")));
            unidadesMap.put(4L, unidadeRepository.save(new Unidade(4L, "Quilograma", "kg")));
            unidadesMap.put(5L, unidadeRepository.save(new Unidade(5L, "Litro", "l")));
            unidadesMap.put(6L, unidadeRepository.save(new Unidade(6L, "Colher de sopa", "cs")));
            unidadesMap.put(7L, unidadeRepository.save(new Unidade(7L, "Colher de chá", "cc")));
            unidadesMap.put(8L, unidadeRepository.save(new Unidade(8L, "Xícara", "xic")));
            unidadesMap.put(9L, unidadeRepository.save(new Unidade(9L, "Copo", "cp")));
            unidadesMap.put(10L, unidadeRepository.save(new Unidade(10L, "Fatia", "fat")));
            unidadesMap.put(11L, unidadeRepository.save(new Unidade(11L, "Porção", "porc")));
            unidadesMap.put(12L, unidadeRepository.save(new Unidade(12L, "Pedaço", "ped")));
        } else {
            unidadeRepository.findAll().forEach(u -> unidadesMap.put(u.getId(), u));
        }


        if (comidaRepository.count() == 0) {
            comidaRepository.saveAll(Arrays.asList(

                    new Comida(1L, "Arroz Branco Cozido", BigDecimal.valueOf(128.0), BigDecimal.valueOf(2.5), BigDecimal.valueOf(28.1), BigDecimal.valueOf(0.2), Icone.ARROZ, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(2L, "Arroz Integral Cozido", BigDecimal.valueOf(124.0), BigDecimal.valueOf(2.6), BigDecimal.valueOf(25.8), BigDecimal.valueOf(1.0), Icone.ARROZ, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(3L, "Macarrão Espaguete Cozido", BigDecimal.valueOf(131.0), BigDecimal.valueOf(4.4), BigDecimal.valueOf(28.2), BigDecimal.valueOf(0.3), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(4L, "Quinoa Cozida", BigDecimal.valueOf(120.0), BigDecimal.valueOf(4.4), BigDecimal.valueOf(21.3), BigDecimal.valueOf(1.9), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(5L, "Cuscuz de Milho Cozido", BigDecimal.valueOf(113.0), BigDecimal.valueOf(2.2), BigDecimal.valueOf(25.4), BigDecimal.valueOf(0.6), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),


                    new Comida(6L, "Peito de Frango Grelhado", BigDecimal.valueOf(159.0), BigDecimal.valueOf(32.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(2.5), Icone.CARNE, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(7L, "Bife de Patinho Grelhado", BigDecimal.valueOf(219.0), BigDecimal.valueOf(35.9), BigDecimal.valueOf(0.0), BigDecimal.valueOf(7.3), Icone.CARNE, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(8L, "Carne Moída (Acém) Cozida", BigDecimal.valueOf(212.0), BigDecimal.valueOf(26.7), BigDecimal.valueOf(0.0), BigDecimal.valueOf(10.9), Icone.CARNE, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(9L, "Sobrecoxa de Frango Assada", BigDecimal.valueOf(233.0), BigDecimal.valueOf(25.5), BigDecimal.valueOf(0.0), BigDecimal.valueOf(13.8), Icone.CARNE, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(10L, "Filé de Tilápia Grelhado", BigDecimal.valueOf(128.0), BigDecimal.valueOf(26.6), BigDecimal.valueOf(0.0), BigDecimal.valueOf(2.3), Icone.CARNE, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(11L, "Lombo de Porco Assado", BigDecimal.valueOf(210.0), BigDecimal.valueOf(31.1), BigDecimal.valueOf(0.0), BigDecimal.valueOf(8.4), Icone.CARNE, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(12L, "Ovo Cozido Inteiro", BigDecimal.valueOf(78.0), BigDecimal.valueOf(6.3), BigDecimal.valueOf(0.6), BigDecimal.valueOf(5.3), Icone.GENERICO, unidadesMap.get(3L), BigDecimal.valueOf(1.0)),
                    new Comida(13L, "Ovo Frito (com óleo)", BigDecimal.valueOf(107.0), BigDecimal.valueOf(6.3), BigDecimal.valueOf(0.6), BigDecimal.valueOf(8.2), Icone.GENERICO, unidadesMap.get(3L), BigDecimal.valueOf(1.0)),

                    new Comida(14L, "Feijão Carioca Cozido", BigDecimal.valueOf(76.0), BigDecimal.valueOf(4.8), BigDecimal.valueOf(13.6), BigDecimal.valueOf(0.5), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(15L, "Feijão Preto Cozido", BigDecimal.valueOf(77.0), BigDecimal.valueOf(4.5), BigDecimal.valueOf(14.0), BigDecimal.valueOf(0.5), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(16L, "Grão de Bico Cozido", BigDecimal.valueOf(164.0), BigDecimal.valueOf(8.9), BigDecimal.valueOf(27.4), BigDecimal.valueOf(2.6), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(17L, "Lentilha Cozida", BigDecimal.valueOf(116.0), BigDecimal.valueOf(9.0), BigDecimal.valueOf(20.1), BigDecimal.valueOf(0.4), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(18L, "Alface Crespa", BigDecimal.valueOf(11.0), BigDecimal.valueOf(1.3), BigDecimal.valueOf(1.7), BigDecimal.valueOf(0.2), Icone.SALADA, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(19L, "Tomate Salada Cru", BigDecimal.valueOf(15.0), BigDecimal.valueOf(0.8), BigDecimal.valueOf(3.1), BigDecimal.valueOf(0.2), Icone.SALADA, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(20L, "Brócolis Cozido", BigDecimal.valueOf(25.0), BigDecimal.valueOf(2.1), BigDecimal.valueOf(4.4), BigDecimal.valueOf(0.5), Icone.SALADA, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(21L, "Cenoura Crua", BigDecimal.valueOf(34.0), BigDecimal.valueOf(1.3), BigDecimal.valueOf(7.7), BigDecimal.valueOf(0.2), Icone.SALADA, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(22L, "Espinafre Cozido", BigDecimal.valueOf(38.0), BigDecimal.valueOf(2.7), BigDecimal.valueOf(4.2), BigDecimal.valueOf(0.5), Icone.SALADA, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),


                    new Comida(23L, "Banana Prata Crua", BigDecimal.valueOf(89.0), BigDecimal.valueOf(1.3), BigDecimal.valueOf(23.0), BigDecimal.valueOf(0.3), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(24L, "Maçã Fuji com Casca", BigDecimal.valueOf(56.0), BigDecimal.valueOf(0.3), BigDecimal.valueOf(15.2), BigDecimal.valueOf(0.0), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(25L, "Abacate Cru", BigDecimal.valueOf(96.0), BigDecimal.valueOf(1.2), BigDecimal.valueOf(6.0), BigDecimal.valueOf(8.4), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(26L, "Mamão Papaia Cru", BigDecimal.valueOf(40.0), BigDecimal.valueOf(0.5), BigDecimal.valueOf(10.4), BigDecimal.valueOf(0.1), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),

                    new Comida(27L, "Leite Integral", BigDecimal.valueOf(58.0), BigDecimal.valueOf(2.9), BigDecimal.valueOf(4.7), BigDecimal.valueOf(3.0), Icone.BEBIDA, unidadesMap.get(2L), BigDecimal.valueOf(100.0)),
                    new Comida(28L, "Leite Desnatado", BigDecimal.valueOf(33.0), BigDecimal.valueOf(3.0), BigDecimal.valueOf(4.7), BigDecimal.valueOf(0.1), Icone.BEBIDA, unidadesMap.get(2L), BigDecimal.valueOf(100.0)),
                    new Comida(29L, "Suco de Laranja Natural", BigDecimal.valueOf(45.0), BigDecimal.valueOf(0.7), BigDecimal.valueOf(10.4), BigDecimal.valueOf(0.2), Icone.BEBIDA, unidadesMap.get(2L), BigDecimal.valueOf(100.0)),
                    new Comida(30L, "Café sem Açúcar", BigDecimal.valueOf(1.0), BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), Icone.BEBIDA, unidadesMap.get(2L), BigDecimal.valueOf(100.0)),


                    new Comida(31L, "Pão Francês", BigDecimal.valueOf(150.0), BigDecimal.valueOf(4.0), BigDecimal.valueOf(29.0), BigDecimal.valueOf(1.5), Icone.GENERICO, unidadesMap.get(3L), BigDecimal.valueOf(1.0)),
                    new Comida(32L, "Pão de Forma Integral", BigDecimal.valueOf(62.0), BigDecimal.valueOf(2.5), BigDecimal.valueOf(11.0), BigDecimal.valueOf(0.8), Icone.GENERICO, unidadesMap.get(10L), BigDecimal.valueOf(1.0)),
                    new Comida(33L, "Queijo Mussarela", BigDecimal.valueOf(84.0), BigDecimal.valueOf(6.0), BigDecimal.valueOf(0.1), BigDecimal.valueOf(6.7), Icone.GENERICO, unidadesMap.get(10L), BigDecimal.valueOf(1.0)),
                    new Comida(34L, "Queijo Minas Frescal", BigDecimal.valueOf(60.0), BigDecimal.valueOf(4.0), BigDecimal.valueOf(0.8), BigDecimal.valueOf(4.5), Icone.GENERICO, unidadesMap.get(10L), BigDecimal.valueOf(1.0)),
                    new Comida(35L, "Manteiga com Sal", BigDecimal.valueOf(36.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(4.0), Icone.GENERICO, unidadesMap.get(6L), BigDecimal.valueOf(1.0)),
                    new Comida(36L, "Azeite de Oliva", BigDecimal.valueOf(119.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(13.5), Icone.GENERICO, unidadesMap.get(6L), BigDecimal.valueOf(1.0)),
                    new Comida(37L, "Pasta de Amendoim", BigDecimal.valueOf(588.0), BigDecimal.valueOf(25.0), BigDecimal.valueOf(20.0), BigDecimal.valueOf(50.0), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(38L, "Aveia em Flocos", BigDecimal.valueOf(394.0), BigDecimal.valueOf(13.9), BigDecimal.valueOf(66.6), BigDecimal.valueOf(8.5), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(39L, "Tapioca Pronta", BigDecimal.valueOf(215.0), BigDecimal.valueOf(0.2), BigDecimal.valueOf(53.6), BigDecimal.valueOf(0.0), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0)),
                    new Comida(40L, "Chocolate Amargo 70%", BigDecimal.valueOf(540.0), BigDecimal.valueOf(7.0), BigDecimal.valueOf(35.0), BigDecimal.valueOf(42.0), Icone.GENERICO, unidadesMap.get(1L), BigDecimal.valueOf(100.0))
            ));
        }


        if (tipoObjetivoRepository.count() == 0) {
            tipoObjetivoRepository.saveAll(Arrays.asList(
                    new TipoObjetivo(1L, "Emagrecimento", "Foco em perda de gordura com déficit calórico moderado, preservando massa muscular com alta ingestão de proteínas.", BigDecimal.valueOf(0.80), BigDecimal.valueOf(2.0), BigDecimal.valueOf(0.8)),
                    new TipoObjetivo(2L, "Emagrecimento Agressivo", "Déficit calórico elevado para perda de peso mais rápida, exige maior controle e alta ingestão de proteínas.", BigDecimal.valueOf(0.70), BigDecimal.valueOf(2.2), BigDecimal.valueOf(0.7)),
                    new TipoObjetivo(3L, "Manutenção", "Objetivo de manter o peso atual, equilibrando ingestão calórica com gasto energético.", BigDecimal.valueOf(1.00), BigDecimal.valueOf(1.6), BigDecimal.valueOf(1.0)),
                    new TipoObjetivo(4L, "Ganho de Massa Magra", "Superávit calórico leve com foco em gain de massa muscular e mínimo acúmulo de gordura.", BigDecimal.valueOf(1.10), BigDecimal.valueOf(1.8), BigDecimal.valueOf(1.0)),
                    new TipoObjetivo(5L, "Ganho de Massa Agressivo", "Superávit calórico alto visando máximo ganho de peso e massa muscular, com maior risco de acúmulo de gordura.", BigDecimal.valueOf(1.20), BigDecimal.valueOf(1.8), BigDecimal.valueOf(1.2)),
                    new TipoObjetivo(6L, "Recomposição Corporal", "Busca simultânea de perda de gordura e ganho de massa muscular, comum em iniciantes.", BigDecimal.valueOf(0.95), BigDecimal.valueOf(2.0), BigDecimal.valueOf(0.9))
            ));
        }
    }
}