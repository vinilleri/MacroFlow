# Regras de Alimentação

## Comida

Uma Comida possui os seguintes dados relevantes:

- `nome`: nome da comida.
- `calorias`: quantidade de calorias.
- `proteinas`: quantidade de proteínas.
- `carboidrato`: quantidade de carboidratos.
- `gordura`: quantidade de gorduras.
- `icone`: ícone associado à comida.
- `origem`: origem da comida.
- `unidadeId`: ID da unidade base da comida.
- `valor`: peso ou medida correspondente a uma unidade.

### Quantidade e valor

`valor` representa o peso ou medida correspondente a uma unidade da comida.

Exemplo:

Arroz Branco Cozido:
- valor: 150
- unidade: g

Isso significa que uma unidade do alimento corresponde a 150 g.

Ao utilizar 300 g dessa comida:

- quantidade: 2
- valor: 150
- unidade: g

Ou:

- quantidade: 1
- valor: 300
- unidade: g

A unidade base da comida permanece a mesma.

---

## Receitas

Ao adicionar uma Comida a uma Receita:

- `quantidade` representa o número de unidades utilizadas.
- `valor` representa o peso ou medida correspondente a uma unidade.

### Unidade

Não é necessário informar a unidade manualmente ao adicionar uma Comida a uma Receita.

A unidade do item é determinada pela unidade cadastrada na própria Comida.

Exemplo:

Arroz Branco Cozido:
- quantidade: 1
- valor: 150

Se a comida possui:
- valor: 150
- unidade: g

Então o item representa 150 g de arroz.

Para utilizar 300 g:

- quantidade: 2
- valor: 150

Ou:

- quantidade: 1
- valor: 300

A IA **não deve enviar `unidadeId` ao adicionar uma Comida a uma Receita**.

---

## IDs das unidades

As unidades do MacroFlow possuem IDs fixos e são utilizadas ao criar ou cadastrar uma Comida.

| ID | Nome | Sigla |
|---:|---|---|
| 1 | Grama | g |
| 2 | Mililitro | ml |
| 3 | Unidade | un |
| 4 | Quilograma | kg |
| 5 | Litro | l |
| 6 | Colher de sopa | cs |
| 7 | Colher de chá | cc |
| 8 | Xícara | xic |
| 9 | Copo | cp |
| 10 | Fatia | fat |
| 11 | Porção | porc |
| 12 | Pedaço | ped |

Ao criar uma Comida, o `unidadeId` deve corresponder à unidade escolhida.

Exemplos:

- 150 g → `unidadeId = 1`
- 200 ml → `unidadeId = 2`
- 2 unidades → `unidadeId = 3`
- 1 colher de sopa → `unidadeId = 6`

### Importante

O `unidadeId` é uma propriedade da **Comida**, não do item da Receita.

Ao adicionar uma Comida existente a uma Receita, a IA não deve tentar escolher ou informar novamente a unidade.