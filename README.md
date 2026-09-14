# MacroFlow

<p align="center">
  <strong>Gestão nutricional baseada em dados para uma alimentação mais consciente.</strong>
</p>

<p align="center">
  <a href="https://github.com/vinilleri/MacroFlow">Backend</a>
  •
  <a href="https://github.com/vinilleri/macroflow-frontend">Frontend</a>
</p>

---

## Sobre o projeto

O **MacroFlow** é uma aplicação web de acompanhamento nutricional desenvolvida como projeto de **Trabalho de Conclusão de Curso (TCC)** no curso técnico de Desenvolvimento de Sistemas.

A plataforma foi desenvolvida com o objetivo de centralizar informações relacionadas à alimentação, composição corporal, objetivos e metas nutricionais em uma única aplicação.

Em vez de tratar a alimentação apenas como um registro de refeições, o MacroFlow busca relacionar diferentes informações do usuário para auxiliar no acompanhamento de sua evolução e na tomada de decisões relacionadas à sua rotina alimentar.

Entre os principais recursos estão:

* registro e acompanhamento do consumo alimentar;
* cálculo e acompanhamento de macronutrientes;
* definição de metas nutricionais;
* acompanhamento de medidas corporais;
* definição de objetivos;
* evolução do peso;
* criação e gerenciamento de receitas;
* recomendações personalizadas;
* assistente virtual baseado em inteligência artificial;
* recuperação de conhecimento por RAG;
* execução de operações através de Tool Calling;
* autenticação e controle de acesso;
* verificação e recuperação de conta por e-mail.

---

## Objetivo

O projeto surgiu a partir de um problema comum em aplicações de acompanhamento nutricional: a **fragmentação das informações**.

Dados como alimentação, metas, peso, medidas corporais e objetivos normalmente são registrados separadamente, dificultando uma visão integrada da evolução do usuário.

O MacroFlow procura integrar essas informações em uma única plataforma, permitindo que os dados registrados sejam utilizados não apenas para armazenamento, mas também como base para análises, recomendações e assistência personalizada.

> **Registrar. Acompanhar. Entender. Evoluir.**

---

## Principais funcionalidades

### Alimentação

O usuário pode registrar os alimentos consumidos ao longo do dia e acompanhar suas informações nutricionais.

O sistema trabalha com dados como:

* calorias;
* proteínas;
* carboidratos;
* gorduras;
* quantidade consumida;
* unidade de medida;
* data do consumo.

Também é possível trabalhar com alimentos cadastrados pelo próprio usuário e alimentos disponibilizados pelo sistema.

---

### Metas nutricionais

O MacroFlow permite definir metas diárias de:

* calorias;
* proteínas;
* carboidratos;
* gorduras.

Os dados consumidos podem ser comparados com as metas estabelecidas, permitindo acompanhar o progresso nutricional ao longo do dia.

O sistema também diferencia metas **calculadas** de metas **manuais**, permitindo que os valores sejam definidos de acordo com as regras nutricionais implementadas ou personalizados pelo usuário.

---

### Objetivos

O usuário pode definir seu objetivo dentro da aplicação, como:

* emagrecimento;
* ganho de massa;
* manutenção.

O objetivo é utilizado como parte das informações que orientam o acompanhamento nutricional e a definição das metas.

---

### Acompanhamento corporal

O sistema permite registrar informações relacionadas à composição corporal e acompanhar sua evolução.

Entre os dados armazenados estão:

* peso;
* medidas corporais;
* registros históricos;
* evolução ao longo do tempo.

Dessa forma, o usuário consegue acompanhar mudanças corporais em conjunto com sua alimentação.

---

### Receitas

O MacroFlow possui suporte para criação de receitas compostas por diferentes alimentos.

Uma receita pode possuir múltiplos itens e suas respectivas quantidades, permitindo calcular as informações nutricionais do conjunto.

Isso possibilita tratar uma preparação completa como uma unidade de consumo, além do registro individual de alimentos.

---

### Recomendações

O sistema possui uma camada dedicada à geração de recomendações nutricionais.

A lógica analisa os dados de consumo e a meta atual do usuário para identificar quais nutrientes ainda precisam ser atingidos e atribuir uma **pontuação de relevância** aos alimentos disponíveis.

Os alimentos podem então ser classificados de acordo com o quanto contribuem para o preenchimento das necessidades restantes, utilizando diferentes pesos para calorias, proteínas, carboidratos e gorduras.

A arquitetura foi desenvolvida de forma independente da camada de apresentação, permitindo a evolução dos critérios de recomendação sem modificar a estrutura principal da aplicação.

---

## Inteligência artificial

O MacroFlow possui um assistente virtual chamado **Magali**, integrado diretamente ao backend da aplicação.

A proposta não é utilizar a IA apenas como uma interface de conversação, mas integrá-la ao ecossistema do sistema para que ela possa utilizar conhecimento relacionado ao domínio e interagir com funcionalidades reais do MacroFlow.

A arquitetura utiliza **Spring AI** para realizar a integração com o modelo de linguagem e combina diferentes mecanismos:

* **LLM (Large Language Model)** para interpretação e geração de respostas;
* **RAG (Retrieval-Augmented Generation)** para recuperação de conhecimento;
* **Tool Calling** para execução de operações reais;
* **memória conversacional** para manutenção de contexto;
* **PGVector** para armazenamento e busca vetorial;
* **embeddings** para representação semântica dos documentos.

---

### Arquitetura da IA

O fluxo simplificado da arquitetura é:

```text
                         ┌─────────────────────┐
                         │       Usuário       │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │      Magali         │
                         │   Assistente IA     │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
                    ▼               ▼               ▼
              Chat Memory          RAG        Tool Calling
                    │               │               │
                    ▼               ▼               ▼
               Contexto       PGVector          Tools
              conversacional      │               │
                                  ▼               ▼
                             Conhecimento      Services
                                               do MacroFlow
                                                    │
                                                    ▼
                                               PostgreSQL
```

A IA, portanto, pode atuar tanto como uma interface de consulta quanto como uma camada capaz de interagir com funcionalidades existentes no backend.

---

### RAG

O MacroFlow utiliza **Retrieval-Augmented Generation (RAG)** para fornecer ao modelo informações específicas sobre o domínio da aplicação.

Documentos contendo conhecimentos nutricionais e regras de funcionamento do MacroFlow são processados e divididos em partes menores.

O fluxo de ingestão é:

```text
Arquivos Markdown
       │
       ▼
    Documents
       │
       ▼
      Chunks
       │
       ▼
    Embeddings
       │
       ▼
     PGVector
```

Quando uma pergunta é realizada, ela também é transformada em uma representação vetorial. O sistema então realiza uma busca por similaridade para recuperar os documentos mais relevantes.

```text
Pergunta
   │
   ▼
Embedding da pergunta
   │
   ▼
Busca por similaridade
   │
   ▼
Documentos relevantes
   │
   ▼
Contexto
   │
   ▼
Modelo de linguagem
   │
   ▼
Resposta
```

O armazenamento vetorial utiliza **PostgreSQL com PGVector**, enquanto os embeddings são gerados localmente através do **Ollama**.

---

### Tool Calling

O assistente possui ferramentas que permitem consultar e executar operações reais no sistema.

Entre as ferramentas disponíveis estão recursos relacionados a:

* consumo alimentar;
* metas nutricionais;
* alimentos;
* receitas;
* medidas corporais.

A arquitetura segue o fluxo:

```text
Usuário
   │
   ▼
Modelo de linguagem
   │
   ▼
Identificação da ferramenta
   │
   ▼
Tool
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
PostgreSQL
```

Isso permite que uma solicitação feita em linguagem natural seja transformada em operações reais da aplicação.

Por exemplo:

```text
"Cadastre feijão preto, linguiça e arroz."

                │
                ▼

          Modelo de IA
                │
                ▼

          Tool Calling
                │
       ┌────────┼────────┐
       ▼        ▼        ▼
    Comida    Comida   Comida
       │        │        │
       └────────┼────────┘
                ▼
            PostgreSQL
```

Uma única solicitação pode resultar em múltiplas chamadas de ferramentas, dependendo da tarefa solicitada pelo usuário.

As ferramentas também utilizam os Services existentes no backend, evitando duplicar regras de negócio dentro da camada de inteligência artificial.

---

### Memória conversacional

O assistente utiliza memória conversacional para manter o contexto das interações recentes.

A memória utilizada pelo modelo é diferente do histórico permanente de conversas armazenado pela aplicação.

```text
ChatMemory
   │
   └── Contexto utilizado pelo modelo

AssistenteVirtual
   │
   └── Histórico permanente da aplicação
```

Essa separação permite que o sistema mantenha o histórico completo para o usuário sem depender dele integralmente como contexto enviado ao modelo.

---

## Arquitetura do backend

O backend segue uma arquitetura organizada em camadas, buscando separar responsabilidades e reduzir o acoplamento entre as diferentes partes da aplicação.

```text
                         ┌──────────────────────┐
                         │      Frontend        │
                         │   HTML / CSS / JS    │
                         └──────────┬───────────┘
                                    │
                                    │ HTTP / REST
                                    ▼
                         ┌──────────────────────┐
                         │     Controllers      │
                         │      Endpoints       │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │       Services       │
                         │  Regras de negócio   │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │     Repositories     │
                         │   Acesso aos dados   │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │      PostgreSQL      │
                         │       Database       │
                         └──────────────────────┘
```

---

## Organização do backend

```text
src/main/java/com/tcc/macroflow
│
├── component/
│
├── configuration/
│   ├── CorsConfig
│   ├── SecurityConfig
│   └── SwaggerConfiguration
│
├── controller/
│   ├── AuthController
│   ├── UsuarioController
│   ├── ComidaController
│   ├── ConsumoController
│   ├── ReceitaController
│   ├── ReceitaItemController
│   ├── MetaController
│   ├── ObjetivoController
│   ├── MedidasCorporaisController
│   ├── RecomendadoController
│   └── AssistenteVirtualController
│
├── dto/
│
├── enums/
│
├── helper/
│
├── model/
│
├── repository/
│
├── service/
│
└── ia/
    ├── service/
    │   ├── MacroFlowIaService
    │   └── KnowledgeIngestionService
    │
    ├── tools/
    │   ├── ConsumoTools
    │   ├── MetaTools
    │   ├── ComidaTools
    │   ├── ReceitaTools
    │   └── MedidasCorporaisTools
    │
    └── configuration/
        └── MacroflowIaConfig
```

A camada `ia` concentra os componentes específicos de inteligência artificial, enquanto as regras de negócio continuam sendo executadas pelos Services tradicionais da aplicação.

---

## Tecnologias

### Backend

| Tecnologia            | Utilização                            |
| --------------------- | ------------------------------------- |
| **Java 21**           | Linguagem principal                   |
| **Spring Boot 4.0.5** | Framework principal                   |
| **Spring Web MVC**    | API REST                              |
| **Spring Data JPA**   | Persistência e ORM                    |
| **PostgreSQL**        | Banco de dados relacional             |
| **Spring Security**   | Autenticação e autorização            |
| **JWT**               | Autenticação baseada em tokens        |
| **Lombok**            | Redução de código repetitivo          |
| **Spring Mail**       | Comunicação por e-mail                |
| **Springdoc OpenAPI** | Documentação da API                   |
| **Maven**             | Gerenciamento de dependências e build |

### Inteligência artificial

| Tecnologia           | Utilização                                           |
| -------------------- | ---------------------------------------------------- |
| **Spring AI**        | Integração com modelos de linguagem e recursos de IA |
| **Groq**             | Inferência do modelo de linguagem                    |
| **Ollama**           | Execução local para geração de embeddings            |
| **nomic-embed-text** | Modelo utilizado para embeddings                     |
| **PGVector**         | Armazenamento e busca vetorial                       |
| **RAG**              | Recuperação de conhecimento contextual               |
| **Tool Calling**     | Execução de operações através da IA                  |

### Frontend

O frontend é mantido em um repositório separado:

**[MacroFlow Frontend](https://github.com/vinilleri/macroflow-frontend)**

A separação permite que a API REST e a camada de apresentação sejam desenvolvidas e evoluídas de forma independente.

---

## API REST

O backend disponibiliza uma API REST responsável por intermediar a comunicação entre a interface, as regras de negócio e os dados da aplicação.

Alguns dos principais recursos expostos pela API incluem:

```text
/auth
/usuario
/comida
/consumo
/receita
/receita-item
/meta
/objetivo
/medidas-corporais
/recomendado
/assistente-virtual
```

Os endpoints são organizados por responsabilidade e associados aos respectivos controllers.

A documentação da API utiliza **OpenAPI/Swagger**, permitindo visualizar e testar os endpoints disponíveis durante o desenvolvimento.

---

## Exemplo de utilização da API

### Criar uma comida

```http
POST /comida
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "nome": "Banana",
  "calorias": 89,
  "proteinas": 1.1,
  "carboidrato": 22.8,
  "gordura": 0.3,
  "unidadeId": 1,
  "valor": 100
}
```

A API valida os dados recebidos antes de realizar a persistência.

---

## Segurança

A aplicação utiliza autenticação baseada em **JSON Web Tokens (JWT)**.

O fluxo simplificado é:

```text
Usuário
   │
   │ login
   ▼
AuthController
   │
   ▼
AuthService
   │
   ├── valida credenciais
   │
   └── gera JWT
           │
           ▼
        Frontend
           │
           │ Authorization: Bearer <token>
           ▼
      API protegida
           │
           ▼
     Spring Security
```

O token é utilizado para identificar o usuário autenticado e restringir o acesso aos dados pertencentes à sua conta.

Também existem mecanismos relacionados a:

* criação de conta;
* login;
* verificação de e-mail;
* recuperação de senha;
* tokens de recuperação;
* proteção de endpoints;
* controle de acesso aos dados do usuário.

> **Importante:** chaves JWT, credenciais de banco de dados, senhas de serviços externos e outras informações sensíveis devem ser configuradas por variáveis de ambiente ou arquivos locais que não sejam versionados.

---

## Banco de dados

O MacroFlow utiliza **PostgreSQL** como banco de dados relacional.

A persistência é realizada utilizando **Spring Data JPA**, com as entidades do domínio representadas pelas classes localizadas em:

```text
src/main/java/com/tcc/macroflow/model
```

Entre as entidades existentes estão:

* `Usuario`
* `Comida`
* `ComidaUsuario`
* `Consumo`
* `Receita`
* `ReceitaItem`
* `ReceitaItemUsuario`
* `Meta`
* `Objetivo`
* `MedidasCorporais`
* `AtividadeFisica`
* `AssistenteVirtual`
* `TokenRecuperacao`
* `CodigoEmail`

A modelagem procura representar as relações entre o usuário, sua alimentação, suas metas e seus dados corporais.

---

## Requisitos

Para executar o backend localmente, recomenda-se ter instalado:

* **Java 21**
* **Maven** ou Maven Wrapper
* **PostgreSQL**
* uma IDE como IntelliJ IDEA, Eclipse ou VS Code

O projeto também possui Maven Wrapper, permitindo executar o Maven sem uma instalação global.

Para utilizar os recursos de inteligência artificial, também são necessárias as configurações correspondentes ao provedor do modelo e ao ambiente de embeddings.

---

## Configuração

Antes de executar o projeto, configure as informações necessárias para conexão com o banco de dados e os serviços externos.

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/macroflow
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

api.security.token.secret=${JWT_SECRET}
```

As credenciais utilizadas pelo provedor de IA e demais serviços externos também devem ser configuradas por variáveis de ambiente.

> Os valores reais não devem ser publicados no repositório.

---

## Executando o projeto

Clone o repositório:

```bash
git clone https://github.com/vinilleri/MacroFlow.git
cd MacroFlow
```

### Windows

Utilizando o Maven Wrapper:

```bash
.\mvnw.cmd spring-boot:run
```

Ou, caso o Maven esteja instalado:

```bash
mvn spring-boot:run
```

### Build

Para gerar o projeto:

```bash
.\mvnw.cmd clean package
```

O arquivo `.jar` será gerado no diretório:

```text
target/
```

---

## Documentação da API

Com a aplicação em execução, a documentação OpenAPI pode ser acessada pela interface Swagger disponibilizada pelo projeto.

A documentação é gerada utilizando **Springdoc OpenAPI**.

---

## Frontend

O frontend do MacroFlow possui um repositório independente:

**https://github.com/vinilleri/macroflow-frontend**

Sua responsabilidade é fornecer a interface de interação com o usuário e consumir os endpoints disponibilizados pela API.

```text
┌─────────────────────┐
│      FRONTEND       │
│                     │
│   HTML / CSS / JS   │
└──────────┬──────────┘
           │
           │ REST / JSON
           ▼
┌─────────────────────┐
│       BACKEND       │
│                     │
│ Spring Boot / Java  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     PostgreSQL      │
└─────────────────────┘
```

---

## Decisões arquiteturais

### Separação de responsabilidades

As regras de negócio ficam concentradas nos `Services`, enquanto os `Controllers` são responsáveis pela exposição dos endpoints.

Isso evita concentrar regras diretamente nos controllers e facilita a manutenção e evolução da aplicação.

### DTOs

A API utiliza DTOs para controlar os dados que entram e saem dos endpoints, evitando expor diretamente todas as propriedades das entidades JPA.

### Repositories

O acesso ao banco de dados é abstraído através da camada de repositories utilizando Spring Data JPA.

### Inteligência artificial

A camada de IA foi isolada em um pacote próprio, permitindo integrar RAG, memória e Tool Calling sem misturar essas responsabilidades com os controllers e serviços tradicionais.

As Tools da IA reutilizam os Services existentes sempre que uma operação precisa ser executada, mantendo as regras de negócio centralizadas.

### Segurança

A autenticação é mantida separada da lógica principal da aplicação através da configuração do Spring Security e dos serviços responsáveis por autenticação e tokens.

---

## Demonstração

O MacroFlow possui uma interface web própria para interação com as funcionalidades da aplicação.

Algumas das principais áreas do sistema incluem:

* dashboard nutricional;
* registro de consumo;
* alimentos;
* receitas;
* metas;
* acompanhamento corporal;
* recomendações;
* assistente virtual.

> Screenshots e demonstrações visuais podem ser adicionados posteriormente em `docs/images`.

---

## Status do projeto

**Pronto**

O MacroFlow foi desenvolvido como um projeto acadêmico de TCC, utilizando tecnologias e padrões comuns no desenvolvimento de aplicações web modernas.



---

## Contexto acadêmico

O MacroFlow foi desenvolvido como **Trabalho de Conclusão de Curso (TCC)** no curso técnico de **Desenvolvimento de Sistemas**.

O projeto reúne conhecimentos de:

* desenvolvimento backend;
* desenvolvimento frontend;
* engenharia de software;
* bancos de dados relacionais;
* APIs REST;
* autenticação e segurança;
* modelagem de sistemas;
* desenvolvimento orientado a objetos;
* inteligência artificial;
* recuperação de informação;
* algoritmos;
* sistemas de recomendação.

---

## Autores

**Vinícius Coelho, Guilherme Vieira, Ricardo Otávio**

Desenvolvedores responsáveis pelo desenvolvimento do projeto MacroFlow.

---

## Repositórios

| Projeto  | Repositório                                                           |
| -------- | --------------------------------------------------------------------- |
| Backend  | [MacroFlow](https://github.com/vinilleri/MacroFlow)                   |
| Frontend | [macroflow-frontend](https://github.com/vinilleri/macroflow-frontend) |

---

## Licença

Este projeto foi desenvolvido para fins acadêmicos.

Consulte os arquivos e configurações presentes no repositório para obter informações adicionais sobre sua utilização e distribuição.
