# Regras de Consumo

## Conceito

Um `Consumo` representa uma comida ou receita consumida pelo usuário em determinado momento.

O consumo pertence ao usuário autenticado e registra:

* nome do item consumido;
* quantidade;
* data e hora;
* calorias;
* proteínas;
* carboidratos;
* gorduras.

Os valores de macronutrientes armazenados no consumo representam os valores calculados para a quantidade efetivamente consumida.

---

## Tipos de Consumo

O MacroFlow possui três tipos de consumo:

* `RECEITA`
* `COMIDA`
* `COMIDA_USUARIO`

### Receita

Quando o tipo é `RECEITA`, o sistema utiliza o ID da receita e a quantidade consumida.

Os valores nutricionais são calculados somando os valores de todos os itens pertencentes à receita.

Uma receita pode possuir tanto comidas do sistema quanto comidas cadastradas pelo usuário.

### Comida

Quando o tipo é `COMIDA`, o sistema utiliza uma comida pertencente ao catálogo do sistema.

Os valores nutricionais são calculados com base nos dados nutricionais cadastrados nessa comida.

### Comida do usuário

Quando o tipo é `COMIDA_USUARIO`, o sistema utiliza uma comida cadastrada pelo próprio usuário.

Os valores nutricionais são calculados com base nos dados nutricionais cadastrados nessa comida.

---

## Quantidade e valor

Ao consumir uma comida, `quantidade` e `valor` são utilizados em conjunto para determinar a quantidade efetivamente consumida.

A `quantidade` representa quantas unidades da referência foram consumidas.

O `valor` representa o peso ou medida correspondente à unidade base da comida.

Exemplo:

Uma comida possui:

* valor: 150
* unidade: g

Ao consumir:

* quantidade: 1
* valor: 150

O consumo corresponde a 150 g.

Ao consumir:

* quantidade: 2
* valor: 150

O consumo corresponde a 300 g.

A unidade utilizada pertence à própria comida e não precisa ser informada novamente ao registrar o consumo.

---

## Cálculo dos macronutrientes

Os valores nutricionais cadastrados em uma comida são multiplicados pela quantidade final calculada para o consumo.

São calculados:

* calorias;
* proteínas;
* carboidratos;
* gorduras.

O mesmo princípio é utilizado para comidas do sistema e comidas cadastradas pelo usuário.

---

## Cálculo de receitas

Ao consumir uma receita, o MacroFlow calcula os macronutrientes de cada item da receita.

Para cada item:

1. A quantidade do item na receita é considerada.
2. Essa quantidade é multiplicada pela quantidade da receita consumida.
3. O `valor` do item é utilizado para determinar a quantidade final da comida.
4. Os valores nutricionais da comida são calculados para essa quantidade.
5. Os resultados de todos os itens são somados.

A receita pode conter:

* `ReceitaItem`, utilizando comidas do sistema;
* `ReceitaItemUsuario`, utilizando comidas cadastradas pelo usuário.

Os dois tipos são incluídos no cálculo final da receita.

---

## Validação da quantidade

A quantidade de um novo consumo deve ser maior que zero.

Valores menores ou iguais a zero são inválidos.

---

## Data do consumo

Ao registrar um novo consumo, o MacroFlow utiliza a data e hora atuais do sistema.

O consumo é associado ao usuário autenticado.

---

## Consulta de consumo

O MacroFlow permite consultar:

### Consumo do dia

O consumo do dia considera os registros do usuário entre:

* início do dia atual;
* final do dia atual.

### Consumo por período

É possível consultar os consumos entre uma data e hora inicial e uma data e hora final.

O início do período não pode ser posterior ao fim do período.

Caso isso aconteça, o período é considerado inválido.

---

## Soma dos macronutrientes

O MacroFlow pode somar os valores nutricionais de todos os consumos de um determinado período.

Para cada consumo são considerados:

* calorias;
* proteínas;
* carboidratos;
* gorduras.

Os valores são somados para produzir o total nutricional do período.

Isso permite comparar posteriormente o consumo do usuário com suas metas nutricionais.

---

## Edição de consumo

Ao editar um consumo, o sistema localiza o registro pelo ID e verifica se ele pertence ao usuário autenticado.

Os valores nutricionais são recalculados utilizando os novos dados informados.

---

## Exclusão de consumo

Um consumo só pode ser excluído pelo usuário ao qual ele pertence.

O sistema verifica a propriedade do consumo antes de realizar a exclusão.
