# Monitoramento de Máquinas — CloudTech Solutions

API REST em **Java 21 + Spring Boot 3** para cadastro, consulta e acompanhamento de máquinas industriais: tipo de máquina, consumo de energia (kW), horário de acionamento, peças produzidas, status de funcionamento e histórico básico de produção e consumo.

Desenvolvimento Backend — Aula 11 — Exercício 1.

## Tecnologias

- Java 21 e Spring Boot 3.4 (Spring Web e Bean Validation)
- JUnit 5 e MockMvc (testes)
- Maven
- Docker
- GitHub Actions (pipeline CI/CD)
- Thunder Client (testes manuais dos endpoints)

## Requisitos funcionais

| Requisito | Descrição | Endpoint |
|---|---|---|
| RF01 | Cadastrar máquina (identificação, tipo, setor e status) | `POST /maquinas/cadastro` |
| RF02 | Registrar dados de produção (consumo kW, horário, peças) | `POST /producao` |
| RF03 | Consultar máquinas cadastradas | `GET /maquinas` |
| RF04 | Consultar dados de produção e consumo | `GET /producao` e `GET /maquinas/{id}/historico` |
| RF05 | Verificar status da aplicação | `GET /status` |

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/` | Página inicial, com a lista de endpoints |
| GET | `/status` | Status da aplicação |
| GET | `/versao` | Versão da aplicação |
| GET | `/maquinas` | Lista as máquinas (filtro opcional `?status=parada`) |
| GET | `/maquinas/{id}` | Consulta uma máquina |
| GET | `/maquinas/cadastro` | Mostra o JSON esperado para o cadastro |
| POST | `/maquinas/cadastro` | Cadastra uma máquina |
| PATCH | `/maquinas/{id}/status` | Atualiza o status de funcionamento |
| GET | `/maquinas/{id}/historico` | Histórico de produção e consumo (totais e média) |
| GET | `/producao` | Lista a produção (filtro opcional `?maquinaId=1`) |
| POST | `/producao` | Registra dados de produção |

### Exemplos

**Cadastrar máquina:** `POST /maquinas/cadastro`

```json
{
  "nome": "Máquina CNC 01",
  "tipo": "CNC",
  "setor": "Usinagem",
  "status": "em operação"
}
```

Resposta `201 Created`:

```json
{
  "id": 1,
  "nome": "Máquina CNC 01",
  "tipo": "CNC",
  "setor": "Usinagem",
  "status": "em operação"
}
```

**Registrar produção:** `POST /producao`

```json
{
  "maquinaId": 1,
  "consumoKw": 12.5,
  "horarioAcionamento": "08:30",
  "pecasProduzidas": 150
}
```

**Status:** `GET /status`

```json
{
  "status": "online",
  "mensagem": "Aplicação de monitoramento em execução"
}
```

**Erros:** a API retorna `400` para dados inválidos (campo vazio, consumo negativo, horário fora do formato `HH:mm`) e `404` para máquina inexistente, sempre em JSON:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos",
  "detalhes": ["nome: o nome (identificação) da máquina é obrigatório"],
  "timestamp": "2026-11-05T21:54:40"
}
```

Ao iniciar, a aplicação carrega três máquinas e alguns registros de produção de exemplo. Os dados ficam em memória e são reiniciados a cada execução.

## Estrutura do repositório

```
monitoramento-maquinas/
├── src/main/java/br/com/cloudtech/monitoramento/
│   ├── config/        # dados iniciais de exemplo
│   ├── controller/    # endpoints REST
│   ├── dto/           # objetos de entrada e saída
│   ├── exception/     # tratamento de erros
│   ├── model/         # Maquina e RegistroProducao
│   ├── repository/    # armazenamento em memória
│   └── service/       # regras de negócio
├── src/main/resources/application.yml
├── tests/java/...     # testes unitários e de integração
├── thunder-client/    # coleção do Thunder Client
├── Dockerfile
├── README.md
├── .gitignore
└── .github/workflows/pipeline.yml
```

## Como executar

Pré-requisitos: Java 21 ou superior e Maven.

```bash
mvn spring-boot:run
```

A aplicação sobe em http://localhost:8080.

### Testes automatizados

```bash
mvn test
```

São 22 testes: unitários dos serviços e de integração de todos os endpoints com MockMvc.

### Docker

```bash
docker build -t monitoramento-maquinas .
docker run -p 8080:8080 monitoramento-maquinas
```

## Testes com o Thunder Client

1. Instale a extensão **Thunder Client** no VS Code.
2. Em *Collections*, clique em **☰ → Import** e selecione `thunder-client/thunder-collection_monitoramento-maquinas.json`.
3. Com a aplicação rodando, clique em **Run All** na coleção. Cada requisição já tem o status HTTP esperado configurado na aba *Tests*.

## Pipeline CI/CD

O workflow `.github/workflows/pipeline.yml` roda a cada push, pull request ou manualmente, em dois jobs:

1. **Build e testes:** compila com Java 21, executa os testes e publica os relatórios e o `.jar` como artefatos.
2. **Imagem Docker e smoke test:** constrói a imagem, sobe o container e verifica `/status`, `/versao`, `/maquinas` e `/producao`.

## Branches

O desenvolvimento foi feito em branches de funcionalidade, integradas à `main` por merge:

| Branch | Conteúdo |
|---|---|
| `feature/cadastro-maquinas` | Cadastro e consulta de máquinas, página inicial, `/status` e `/versao` |
| `feature/monitoramento` | Registro de produção, consulta de produção e histórico |
| `feature/testes` | Testes automatizados e coleção do Thunder Client |
| `feature/docker` | Dockerfile |
| `feature/pipeline-cicd` | Pipeline do GitHub Actions |
