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

O **MacroFlow** é uma aplicação web de acompanhamento nutricional desenvolvida como projeto de **Trabalho de Conclusão de Curso (TCC)**.

A plataforma foi desenvolvida com o objetivo de centralizar informações relacionadas à alimentação, composição corporal e metas nutricionais em uma única aplicação.

Em vez de tratar alimentação apenas como um registro de refeições, o MacroFlow busca relacionar diferentes informações do usuário para auxiliar no acompanhamento de sua evolução e na tomada de decisões relacionadas à sua rotina alimentar.

Entre os principais recursos estão:

* registro e acompanhamento do consumo alimentar;
* cálculo e acompanhamento de macronutrientes;
* definição de metas nutricionais;
* acompanhamento de medidas corporais;
* definição de objetivos;
* evolução do peso;
* criação e gerenciamento de receitas;
* recomendações personalizadas;
* assistente virtual baseado em IA;
* autenticação e controle de acesso;
* recuperação e verificação de conta por e-mail.

---

## Objetivo

O projeto surgiu a partir de um problema comum em aplicações de acompanhamento nutricional: a fragmentação das informações.

Dados como **alimentação, metas, peso, medidas corporais e objetivos** normalmente são registrados separadamente, dificultando uma visão geral da evolução do usuário.

O MacroFlow procura integrar essas informações em uma única plataforma, permitindo que os dados registrados pelo usuário sejam utilizados não apenas para armazenamento, mas também como base para análises, recomendações e assistência personalizada.

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

Os dados consumidos podem então ser comparados com as metas estabelecidas, permitindo acompanhar o progresso nutricional ao longo do dia.

---

### Objetivos

O usuário pode definir seu objetivo dentro da aplicação, como:

* emagrecimento;
* ganho de massa;
* manutenção.

O objetivo é utilizado como parte das informações que orientam o acompanhamento do usuário.

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

O sistema possui um mecanismo de recomendação responsável por analisar características dos alimentos e atribuir uma **pontuação de relevância** de acordo com o contexto do usuário.

A arquitetura de recomendação foi desenvolvida de forma separada da camada de apresentação, permitindo evoluir posteriormente os critérios utilizados pelo sistema.

---

### Assistente virtual

O MacroFlow possui um assistente virtual integrado à aplicação.

A funcionalidade utiliza informações relacionadas ao usuário para fornecer respostas contextualizadas sobre sua rotina dentro do sistema.

As interações com o assistente são armazenadas, permitindo manter o histórico da conversa.

A arquitetura foi projetada para permitir a evolução futura do mecanismo de inteligência artificial sem acoplar a lógica de IA diretamente às demais funcionalidades do sistema.

---

### Autenticação e segurança

O backend utiliza **Spring Security** para controle de acesso e autenticação.

A autenticação utiliza **JSON Web Tokens (JWT)** para identificar usuários nas requisições autenticadas.

Também existem mecanismos relacionados a:

* criação de conta;
* login;
* verificação de e-mail;
* recuperação de senha;
* tokens de recuperação;
* proteção de endpoints;
* controle de acesso aos dados do usuário.

---

## Arquitetura

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

### Organização do backend

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
│   ├── Usuario
│   ├── Comida
│   ├── ComidaUsuario
│   ├── Consumo
│   ├── Receita
│   ├── ReceitaItem
│   ├── ReceitaItemUsuario
│   ├── Meta
│   ├── Objetivo
│   ├── MedidasCorporais
│   ├── AtividadeFisica
│   ├── AssistenteVirtual
│   └── ...
│
├── repository/
│
└── service/
    ├── AuthService
    ├── UsuarioService
    ├── ComidaService
    ├── ConsumoService
    ├── ReceitaService
    ├── RecomendacaoService
    ├── AssistenteVirtualService
    └── ...
```

A estrutura atual do projeto contém essas camadas e separa explicitamente controllers, serviços, modelos, DTOs, repositories e configurações.

---

## Tecnologias

### Backend

| Tecnologia            | Utilização                            |
| --------------------- | ------------------------------------- |
| **Java 21**           | Linguagem principal                   |
| **Spring Boot 4.0.5** | Framework principal                   |
| **Spring Web MVC**    | API REST                              |
| **Spring Data JPA**   | Persistência e ORM                    |
| **PostgreSQL**        | Banco de dados                        |
| **Spring Security**   | Autenticação e autorização            |
| **JWT**               | Autenticação baseada em tokens        |
| **Lombok**            | Redução de código repetitivo          |
| **Spring Mail**       | Comunicação por e-mail                |
| **Springdoc OpenAPI** | Documentação da API                   |
| **Maven**             | Gerenciamento de dependências e build |

Essas dependências estão declaradas no `pom.xml` atual do projeto.

### Frontend

O frontend é mantido em um repositório separado:

**[MacroFlow Frontend](https://github.com/vinilleri/macroflow-frontend)**

A separação entre frontend e backend permite que a API REST seja desenvolvida e evoluída independentemente da camada de apresentação.

---

## API REST

O backend disponibiliza uma API REST responsável por intermediar a comunicação entre a interface e os dados da aplicação.

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

## Segurança

A aplicação utiliza autenticação baseada em JWT.

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

---

## Configuração

Antes de executar o projeto, configure as informações necessárias para conexão com o banco de dados e os serviços externos.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/macroflow
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

api.security.token.secret=${JWT_SECRET}
```

> Os valores reais não devem ser publicados no repositório.

Dependendo da configuração utilizada, também podem ser necessárias credenciais relacionadas ao serviço de e-mail e ao provedor utilizado pelo assistente virtual.

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

## Estrutura do projeto

O repositório principal contém:

```text
MacroFlow/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tcc/macroflow/
│   │   │       ├── component/
│   │   │       ├── configuration/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── enums/
│   │   │       ├── helper/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │
│   │   └── resources/
│   │
│   └── test/
│
├── Dockerfile
├── pom.xml
├── mvnw
└── mvnw.cmd
```

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

### Segurança

A autenticação é mantida separada da lógica principal da aplicação através da configuração do Spring Security e dos serviços responsáveis por autenticação e tokens.

---

## Inteligência artificial

Uma das áreas de evolução do MacroFlow é a utilização de inteligência artificial para transformar os dados registrados pelo usuário em informações mais úteis.

A IA pode utilizar o contexto nutricional e os dados cadastrados na aplicação para fornecer respostas mais relevantes do que um chatbot genérico.

A intenção é que o assistente faça parte do ecossistema do MacroFlow, e não seja apenas uma interface para conversar com um modelo de linguagem.

---

## Sistema de recomendações

O MacroFlow também possui uma camada dedicada à geração de recomendações.

A lógica de recomendação utiliza uma abordagem baseada em **pontuação**, permitindo classificar diferentes alimentos de acordo com critérios definidos pela aplicação.

De forma simplificada:

```text
Dados do usuário
       │
       ▼
Características dos alimentos
       │
       ▼
Cálculo de pontuação
       │
       ▼
Ordenação dos resultados
       │
       ▼
Alimentos recomendados
```

Essa arquitetura permite adicionar novos critérios futuramente sem alterar a estrutura principal da API.

---

## Status do projeto

**Em desenvolvimento.**

O MacroFlow está sendo desenvolvido como um projeto acadêmico de TCC, mas sua arquitetura foi construída utilizando tecnologias e padrões comuns no desenvolvimento de aplicações web modernas.

Atualmente, o projeto concentra seus esforços principalmente em:

* evolução da experiência do usuário;
* sistema de recomendações;
* integração e evolução do assistente de IA;
* refinamento da API;
* segurança;
* organização da arquitetura;
* integração entre frontend e backend.

---

## Próximos passos

Algumas das áreas previstas para evolução incluem:

* aprimoramento do sistema de recomendações;
* evolução do assistente virtual;
* utilização mais inteligente dos dados históricos;
* melhorias na experiência do usuário;
* expansão da análise nutricional;
* melhorias de observabilidade e tratamento de erros;
* evolução da documentação da API.

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
* algoritmos e sistemas de recomendação.

---

## Autor

**Vinícius Coelho**

Desenvolvedor responsável pelo desenvolvimento do projeto MacroFlow.

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