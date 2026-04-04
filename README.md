# 🔀 BFF Agendador de Tarefas

Serviço **BFF (Backend for Frontend)** que atua como ponto de entrada único para o ecossistema de microsserviços de agendamento de tarefas. Ele orquestra as chamadas entre o frontend e os três microsserviços do sistema: **usuario**, **agendador-tarefas** e **notificacao**.

---

## 🏗️ Arquitetura do Sistema

```
                        ┌─────────────────────────────┐
                        │       Frontend / Cliente     │
                        └──────────────┬──────────────┘
                                       │
                                       ▼
                        ┌─────────────────────────────┐
                        │   BFF Agendador de Tarefas   │
                        │   (Spring Boot + OpenFeign)  │
                        └──────┬──────────┬────────────┘
                               │          │          │
               ┌───────────────┘          │          └──────────────────┐
               ▼                          ▼                             ▼
  ┌────────────────────┐   ┌──────────────────────┐   ┌────────────────────────┐
  │      usuario       │   │  agendador-tarefas   │   │      notificacao       │
  │  Spring Boot + JPA │   │  Spring Boot + Mongo │   │   Spring Boot + Email  │
  │     PostgreSQL     │   │       MongoDB        │   │     Java/HTML          │
  └────────────────────┘   └──────────────────────┘   └────────────────────────┘
```

O BFF recebe todas as requisições do frontend, repassa para os microsserviços corretos via **OpenFeign**, e retorna as respostas consolidadas. Nenhum microsserviço é exposto diretamente ao frontend.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.3 | Framework base |
| Spring Cloud OpenFeign | 2025.1.0 | Comunicação com microsserviços |
| Feign HC5 | 13.9.3 | Cliente HTTP de alta performance |
| SpringDoc OpenAPI | 3.0.2 | Documentação Swagger |
| Lombok | — | Redução de boilerplate |
| Maven | Wrapper incluso | Build |
| Docker | — | Containerização |
| Docker Compose | 3.8 | Orquestração local dos serviços |

---

## 📁 Estrutura do Projeto

```
bff-agendador-tarefas/
├── .github/
│   └── workflows/              # Pipelines CI/CD
├── src/
│   └── main/
│       └── java/com/alan/bff_agendador_tarefas/
│           ├── BffAgendadorTarefasApplication.java
│           ├── controller/     # Endpoints REST expostos ao frontend
│           ├── business/       # Lógica de orquestração e DTOs
│           └── infrastructure/
│               └── client/     # Feign Clients para cada microsserviço
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## 🐳 Executando com Docker (recomendado)

O `docker-compose.yml` sobe todo o ecossistema de uma vez: BFF, os 3 microsserviços, PostgreSQL e MongoDB.

### Pré-requisitos

- [Docker](https://www.docker.com/) instalado
- Os repositórios clonados lado a lado na mesma pasta:

```
/projetos
├── bff-agendador-tarefas/   ← docker-compose.yml está aqui
├── agendador-tarefas/
├── usuario/
└── notificacao/
```

### Subindo tudo

```bash
cd bff-agendador-tarefas
docker-compose up --build
```

### Serviços e portas

| Serviço | Porta | Descrição |
|---|---|---|
| `bff-agendador-tarefas` | `8083` | BFF — ponto de entrada do frontend |
| `usuario` | `8080` | Microsserviço de usuários |
| `agendador-tarefas` | `8081` | Microsserviço de tarefas |
| `notificacao` | `8082` | Microsserviço de notificações |
| `postgres` | `5433` | Banco de dados do serviço usuario |
| `mongo` | `27017` | Banco de dados do serviço agendador-tarefas |

### Variáveis de ambiente configuradas automaticamente

| Variável | Valor |
|---|---|
| `USUARIO_URL` | `usuario:8080/usuario` |
| `AGENDADOR_TAREFAS_URL` | `agendador_tarefas:8081/tarefas` |
| `NOTIFICACAO_URL` | `notificacao:8082/email` |
| `SPRING_DATA_MONGODB_URI` | `mongodb://mongo:27017/db_agendador` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/db_usuario` |
| `SPRING_DATASOURCE_USERNAME` | `admin` |
| `SPRING_DATASOURCE_PASSWORD` | `admin` |

### Derrubando os containers

```bash
docker-compose down
```

---

## ▶️ Executando sem Docker

### Pré-requisitos

- Java 17+
- MongoDB rodando localmente
- PostgreSQL rodando localmente
- Os microsserviços **usuario**, **agendador-tarefas** e **notificacao** rodando

### Configuração

Edite o `src/main/resources/application.properties`:

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

## 🔧 Dockerfile

O BFF utiliza um build multi-stage para manter a imagem final enxuta:

```dockerfile
# Stage 1 — build
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Stage 2 — runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
CMD ["java", "-jar", "/app/app.jar"]
```

---

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8083/swagger-ui.html
```

---

## 🧩 Microsserviços Conectados

### 👤 [usuario](https://github.com/AlanF-Oliveira/usuario)

Responsável pelo cadastro, autenticação e gerenciamento de usuários. Emite o token **JWT** utilizado em toda a plataforma.

- **Stack:** Spring Boot 4 · Spring Data JPA · PostgreSQL · Spring Security · JWT · BCrypt · Gradle
- **Principais endpoints consumidos pelo BFF:**

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

### 📋 [agendador-tarefas](https://github.com/AlanF-Oliveira/agendador-tarefas)

Responsável pelo gerenciamento e agendamento de tarefas dos usuários. Autentica via JWT e persiste as tarefas no MongoDB.

- **Stack:** Spring Boot 4 · Spring Data MongoDB · Spring Security · JWT · OpenFeign · MapStruct · Gradle
- **Principais endpoints consumidos pelo BFF:**

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

### 🔔 [notificacao](https://github.com/AlanF-Oliveira/notificacao)

Responsável pelo envio de notificações aos usuários sobre suas tarefas agendadas. Possui templates HTML para os e-mails disparados.

- **Stack:** Spring Boot · Gradle · Java · HTML (templates de e-mail)

---

## 🔐 Autenticação

O fluxo de autenticação da plataforma é:

```
1. Frontend chama  POST /usuario/login  via BFF
2. BFF repassa para o microsserviço usuario
3. usuario valida as credenciais e retorna um token JWT
4. Frontend armazena o token e envia em todas as próximas requisições:
   Authorization: Bearer <token>
5. BFF propaga o token para os microsserviços que precisam validá-lo
```

---


## 👤 Autor

**Alan F. Oliveira** — [github.com/AlanF-Oliveira](https://github.com/AlanF-Oliveira)