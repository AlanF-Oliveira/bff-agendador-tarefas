# BFF Agendador de Tarefas

Serviço **BFF (Backend for Frontend)** que atua como ponto de entrada único para o ecossistema de microsserviços de agendamento de tarefas. Ele orquestra as chamadas entre o frontend e os três microsserviços do sistema: **usuario**, **agendador-tarefas** e **notificacao**.

---

## Arquitetura do Sistema

```
                    ┌─────────────────────────────┐
                    │       Frontend / Cliente     │
                    └──────────────┬──────────────┘
                                   │
                                   ▼
                    ┌─────────────────────────────┐
                    │   BFF Agendador de Tarefas   │
                    │   (Spring Boot + OpenFeign)  │
                    └──────┬───────────┬───────────┘
                           │           │           │
           ┌───────────────┘           │           └──────────────────┐
           ▼                           ▼                              ▼
┌────────────────────┐  ┌──────────────────────┐  ┌────────────────────────┐
│      usuario       │  │  agendador-tarefas   │  │      notificacao       │
│  Spring Boot + JPA │  │  Spring Boot + Mongo │  │   Spring Boot + Email  │
│     PostgreSQL     │  │       MongoDB        │  │     Java / HTML        │
└────────────────────┘  └──────────────────────┘  └────────────────────────┘
```

O BFF recebe todas as requisições do frontend, repassa para os microsserviços corretos via OpenFeign e retorna as respostas consolidadas. Nenhum microsserviço é exposto diretamente ao frontend.

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.3 |
| Spring Cloud (OpenFeign) | 2025.1.0 |
| Feign HC5 | 13.9.3 |
| Springdoc OpenAPI | 3.0.2 |
| Lombok | 1.18.32 |
| Docker / Docker Compose | — |

---

## Estrutura do Projeto

```
bff-agendador-tarefas/
├── .github/
│   └── workflows/
│       └── maven.yml
├── src/
│   └── main/
│       └── java/com/alan/bff_agendador_tarefas/
│           ├── BffAgendadorTarefasApplication.java
│           ├── controller/
│           ├── business/
│           └── infrastructure/
│               └── client/
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## Como Rodar o Projeto

### Pré-requisitos

- Docker Desktop instalado e em execução
- Todos os repositórios clonados **lado a lado** na mesma pasta:

```
/projetos
├── bff-agendador-tarefas/   ← docker-compose.yml está aqui
├── agendador-tarefas/
├── usuario/
└── notificacao/
```

### Subindo com Docker Compose

```bash
cd bff-agendador-tarefas
docker compose up --build
```

> Use `--build` na primeira execução ou sempre que houver alterações no código.
> Nas execuções seguintes, basta:

```bash
docker compose up
```

Para derrubar os containers:

```bash
docker compose down
```

### Serviços e Portas

| Serviço | Porta | Descrição |
|---|---|---|
| `bff-agendador-tarefas` | `8083` | BFF — ponto de entrada do frontend |
| `usuario` | `8080` | Microsserviço de usuários |
| `agendador-tarefas` | `8081` | Microsserviço de tarefas |
| `notificacao` | `8082` | Microsserviço de notificações |
| `postgres` | `5433` | Banco PostgreSQL do serviço `usuario` |
| `mongo` | `27017` | Banco MongoDB do serviço `agendador-tarefas` |

### Variáveis de Ambiente

Configuradas automaticamente pelo `docker-compose.yml`:

| Variável | Valor |
|---|---|
| `USUARIO_URL` | `usuario:8080/usuario` |
| `AGENDADOR_TAREFAS_URL` | `agendador_tarefas:8081/tarefas` |
| `NOTIFICACAO_URL` | `notificacao:8082/email` |
| `SPRING_DATA_MONGODB_URI` | `mongodb://mongo:27017/db_agendador` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/db_usuario` |
| `SPRING_DATASOURCE_USERNAME` | `admin` |
| `SPRING_DATASOURCE_PASSWORD` | `admin` |

---

## Executando Sem Docker

### Pré-requisitos

- Java 17+
- MongoDB rodando localmente
- PostgreSQL rodando localmente
- Microsserviços **usuario**, **agendador-tarefas** e **notificacao** em execução

### Configuração

Edite o arquivo `src/main/resources/application.properties`:

```properties
usuario.url=http://localhost:8080
agendador.url=http://localhost:8081
notificacao.url=http://localhost:8082
```

### Executando

```bash
git clone https://github.com/AlanF-Oliveira/bff-agendador-tarefas.git
cd bff-agendador-tarefas
./mvnw spring-boot:run
```

---

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8083/swagger-ui.html
```

---

## Autenticação

O fluxo de autenticação da plataforma segue os seguintes passos:

```
1. Frontend chama  POST /usuario/login  via BFF
2. BFF repassa a requisição para o microsserviço usuario
3. usuario valida as credenciais e retorna um token JWT
4. Frontend armazena o token e o envia em todas as requisições seguintes:
   Authorization: Bearer <token>
5. BFF propaga o token para os microsserviços que precisam validá-lo
```

---

## Microsserviços Conectados

### [usuario](https://github.com/AlanF-Oliveira/usuario)

Responsável pelo cadastro, autenticação e gerenciamento de usuários. Emite o token JWT utilizado em toda a plataforma.

**Stack:** Spring Boot 4 · Spring Data JPA · PostgreSQL · Spring Security · JWT · BCrypt · Gradle

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuario` | Cadastro de novo usuário |
| `POST` | `/usuario/login` | Login — retorna token JWT |
| `GET` | `/usuario?email={email}` | Busca dados do usuário |
| `PUT` | `/usuario` | Atualiza dados do usuário |
| `DELETE` | `/usuario/{email}` | Remove o usuário |
| `POST` | `/usuario/endereco` | Cadastra endereço |
| `PUT` | `/usuario/endereco?id={id}` | Atualiza endereço |
| `POST` | `/usuario/telefone` | Cadastra telefone |
| `PUT` | `/usuario/telefone?id={id}` | Atualiza telefone |

---

### [agendador-tarefas](https://github.com/AlanF-Oliveira/agendador-tarefas)

Responsável pelo gerenciamento e agendamento de tarefas dos usuários. Autentica via JWT e persiste os dados no MongoDB.

**Stack:** Spring Boot 4 · Spring Data MongoDB · Spring Security · JWT · OpenFeign · MapStruct · Gradle

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tarefas` | Cria nova tarefa |
| `GET` | `/tarefas` | Lista tarefas do usuário |
| `GET` | `/tarefas/eventos` | Busca tarefas por período |
| `PUT` | `/tarefas?id={id}` | Atualiza uma tarefa |
| `PATCH` | `/tarefas?id={id}&status={status}` | Atualiza status de notificação |
| `DELETE` | `/tarefas?id={id}` | Remove uma tarefa |

**Status de notificação disponíveis:** `PENDENTE` · `NOTIFICADO` · `CANCELADO`

---

### [notificacao](https://github.com/AlanF-Oliveira/notificacao)

Responsável pelo envio de notificações por e-mail sobre tarefas agendadas. Utiliza templates HTML para a formatação dos e-mails.

**Stack:** Spring Boot · Gradle · Java · HTML (templates de e-mail)

---

## CI/CD

O projeto utiliza **GitHub Actions** para integração contínua. O pipeline é acionado automaticamente em:

- Pull Requests abertos, sincronizados ou reabertos para a branch `main`

**Etapas do pipeline:**

1. Checkout do código
2. Configuração do JDK 17 (Temurin)
3. Cache das dependências Maven
4. Build com Maven (`mvn -B package`)

O arquivo de configuração está em `.github/workflows/maven.yml`.