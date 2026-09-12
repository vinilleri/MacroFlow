# Regras de Metas

## Conceito

Uma meta nutricional define os valores diários que o usuário deve utilizar como referência para:

* calorias;
* proteínas;
* carboidratos;
* gorduras.

A meta pertence ao usuário e está associada a um objetivo.

O MacroFlow permite dois tipos de meta:

* `CALCULADA`
* `MANUAL`

---

## Meta calculada

Uma meta calculada é gerada automaticamente pelo MacroFlow utilizando os dados atuais do usuário.

Para calcular a meta, o sistema utiliza:

* peso;
* altura;
* idade;
* sexo;
* nível de atividade física;
* objetivo atual.

O sistema utiliza a medida corporal mais recente cadastrada pelo usuário.

Para calcular a idade, é utilizada a data de nascimento registrada nas medidas corporais.

O usuário precisa possuir um nível de atividade física cadastrado para que uma meta calculada possa ser criada.

---

## Cálculo da meta

A meta calculada utiliza a taxa metabólica basal (TMB) e o gasto energético diário total (TDEE).

A TMB é calculada a partir de peso, altura, idade e sexo.

Para usuários do sexo masculino:

* é aplicado o ajuste masculino no cálculo da TMB.

Para usuários do sexo feminino:

* é aplicado o ajuste feminino no cálculo da TMB.

Depois da TMB, o TDEE é calculado utilizando o fator multiplicador correspondente ao nível de atividade física do usuário.

O objetivo do usuário possui multiplicadores específicos para:

* calorias;
* proteínas;
* gorduras.

Esses multiplicadores são utilizados para determinar os valores da meta.

---

## Proteínas e gorduras

A quantidade de proteínas e gorduras da meta calculada é determinada utilizando o peso considerado pelo sistema e os multiplicadores definidos pelo objetivo.

O sistema calcula primeiro:

* total de proteínas;
* total de gorduras.

As proteínas fornecem 4 kcal por grama.

As gorduras fornecem 9 kcal por grama.

As calorias utilizadas por proteínas e gorduras são subtraídas da meta calórica total.

---

## Carboidratos

As calorias restantes depois do cálculo de proteínas e gorduras são destinadas aos carboidratos.

Os carboidratos fornecem 4 kcal por grama.

Portanto, a quantidade de carboidratos é determinada pelas calorias restantes divididas por 4.

A meta calculada é considerada inválida caso as calorias provenientes de proteínas e gorduras sejam maiores ou iguais à meta calórica total.

---

## Peso utilizado no cálculo

O sistema calcula um peso de referência a partir do peso atual e de um peso ideal calculado com base na altura.

O peso utilizado para calcular proteínas e gorduras é o menor entre:

* peso atual;
* peso ideal calculado.

Quando a altura está abaixo de 152,4 cm, o sistema utiliza o peso atual.

---

## Meta manual

Uma meta manual permite que o usuário informe diretamente seus valores nutricionais.

Os valores informados são:

* calorias;
* proteínas;
* carboidratos;
* gorduras.

Os valores passam pela validação de macros antes de serem armazenados.

A meta manual não utiliza o cálculo automático baseado em peso, altura, idade, sexo ou atividade física.

---

## Objetivo

Toda meta está associada a um objetivo do usuário.

O objetivo precisa:

* pertencer ao usuário;
* estar ativo.

Ao criar uma meta, o objetivo informado é utilizado como referência para a meta.

Uma meta calculada utiliza o tipo de objetivo para obter seus multiplicadores nutricionais.

---

## Meta ativa

O usuário possui uma meta atual ativa.

Quando uma nova meta é criada e já existe uma meta ativa:

1. A meta anterior é encerrada.
2. Sua data de fim é definida como a data atual.
3. A meta anterior deixa de ser ativa.
4. A nova meta passa a ser a meta ativa.

Somente uma meta deve representar a meta atual do usuário.

---

## Datas da meta

Ao criar uma meta, sua data de início é definida como a data atual.

Se o objetivo associado possuir uma data de fim, essa data também é utilizada como data de fim da meta.

Quando uma meta ativa é encerrada manualmente ou substituída por uma nova meta, sua data de fim é definida como a data atual.

---

## Consultar a meta atual

A meta atual é a meta que:

* pertence ao usuário autenticado;
* está marcada como ativa.

Se o usuário não possuir uma meta ativa, a meta atual não pode ser encontrada.

---

## Metas antigas

Quando uma meta deixa de ser ativa, ela permanece registrada como histórico.

As metas antigas podem ser consultadas posteriormente.

Uma meta antiga não pode ser considerada a meta atual.

---

## Atualização da meta

Ao atualizar a meta atual, o sistema mantém a meta existente e altera seus valores conforme os dados enviados.

A atualização pode utilizar:

* uma nova meta calculada;
* uma nova meta manual.

A meta atualizada continua associada ao objetivo informado.

---

## Desativação

A meta atual pode ser desativada.

Quando isso acontece:

* a meta deixa de ser ativa;
* sua data de fim passa a ser a data atual.

---

## Exclusão

Somente metas antigas podem ser excluídas.

Uma meta ativa não pode ser excluída por meio da operação destinada à exclusão de metas antigas.

---

## Relação entre meta e consumo

A meta representa o valor nutricional diário de referência.

O consumo representa aquilo que o usuário efetivamente ingeriu.

A comparação entre consumo e meta permite identificar quanto o usuário já consumiu e quanto ainda falta para atingir sua meta diária.
