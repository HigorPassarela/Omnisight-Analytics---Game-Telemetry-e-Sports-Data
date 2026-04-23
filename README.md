# 🎮 Omnisight Analytics - Game Telemetry & e-Sports Data

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen?style=for-the-badge&logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=for-the-badge&logo=docker)
![Gradle](https://img.shields.io/badge/Gradle-Build_Tool-02303A?style=for-the-badge&logo=gradle)

Uma API REST de alta performance desenvolvida em Java 21 para processar e analisar telemetria de partidas de e-Sports em tempo real. Este projeto foge do tradicional "CRUD" e foca em **Big Data, Performance e Agregações Complexas** usando o motor nativo do MongoDB.

## 🎯 O Desafio
Em jogos multiplayer massivos, cada ação (tiro, compra de item, morte, movimentação) gera um evento. Um jogo com milhares de jogadores gera milhões de registros por dia. 
O objetivo deste projeto foi construir uma arquitetura backend capaz de:
1. Ingerir dados massivos rapidamente sem estourar a memória (Heap).
2. Extrair métricas analíticas complexas (K/D Ratio, Mapas de Calor, Dashboards) em milissegundos.
3. Processar cálculos matemáticos pesados direto no banco de dados (C++), poupando o servidor Java.

---

## 🚀 Funcionalidades Principais

* **Massive Data Seeder:** Um script de inicialização (`CommandLineRunner`) que gera e insere **200.000 eventos** de partidas simuladas no banco de dados usando *Bulk Inserts* em lotes.
* **K/D Ratio Dinâmico:** Cálculo de *Kills/Deaths* em tempo real usando operadores condicionais (`$cond`) e matemáticos (`$divide`) direto no banco.
* **Heatmap Geoespacial:** Agrupamento de coordenadas (X, Y) em quadrantes de mapa usando operadores de módulo e subtração matemática (`$mod`, `$subtract`).
* **Dashboard Facetado (`$facet`):** Execução de múltiplas sub-queries simultâneas (Distribuição de eventos + Horário de pico) varrendo a base de dados uma única vez.
* **Tunning de Performance:** Utilização de **Compound Indexes** (Índices Compostos) que reduziram o tempo de resposta das agregações de ~120ms (Collection Scan) para **~48ms** (Index Scan).

---

## 🛠️ Tecnologias Utilizadas

* **Java 21:** Uso de *Records* para DTOs imutáveis (Arquitetura sem Lombok).
* **Spring Boot 3:** Spring Web, Spring Data MongoDB.
* **MongoDB 7:** Aggregation Framework, Compound Indexes.
* **Docker & Docker Compose:** Infraestrutura de banco de dados conteinerizada.
* **Datafaker:** Geração de dados realistas para testes de carga.
* **Springdoc OpenAPI 3 (Swagger):** Documentação interativa da API.

---

## 📁 Estrutura do Projeto

```text
src/main/java/br/com/omnisightAnalytics/
├── controller/
│   └── PlayerStatsController.java      # Endpoints REST documentados com Swagger
├── domain/
│   ├── Coordinates.java                # Sub-documento embutido (Embedded)
│   └── MatchEvent.java                 # Entidade principal mapeada com @CompoundIndex
├── dto/
│   ├── DashboardFacetedDTO.java        # Records Java 21 para saída de dados
│   ├── EventTypeStatDTO.java
│   ├── HeatmapZoneDTO.java
│   └── PlayerStatsDTO.java
├── repository/
│   ├── MatchEventRepository.java       # Interface padrão Spring Data
│   ├── MatchEventRepositoryCustom.java # Interface para queries complexas
│   └── MatchEventRepositoryImpl.java   # Implementação do Aggregation Framework
├── seeder/
│   └── DatabaseSeeder.java             # Gerador de 200k registros (Bulk Insert)
└── NexusTelemetryApplication.java      # Classe Main
```

---

## ⚙️ Como Executar o Projeto localmente

### Pré-requisitos
* [Docker](https://www.docker.com/) rodando na máquina.
* [Java 21](https://jdk.java.net/21/) instalado.

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/omnisight-analytics.git
   cd omnisight-analytics
   ```

2. **Suba o Banco de Dados (MongoDB via Docker):**
   ```bash
   docker-compose up -d
   ```

3. **Inicie a Aplicação Spring Boot:**
   ```bash
   ./gradlew bootRun
   ```
   *Nota: Na primeira execução, a aplicação levará alguns segundos extras para gerar e inserir os 200.000 registros no MongoDB. Você verá o progresso no console.*

4. **Acesse a Documentação (Swagger):**
   Abra o navegador e acesse: 👉 `http://localhost:8080/swagger-ui/index.html`

---

## 📊 Endpoints da API

A API possui 3 endpoints analíticos principais (testáveis via Swagger):

| Método | Endpoint | Descrição | MongoDB Feature Utilizada |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/stats/top-players` | Retorna o Top N jogadores baseado no K/D Ratio. | `$match`, `$group`, `$project`, `$cond`, `$divide` |
| `GET` | `/api/stats/heatmap` | Agrupa mortes por "quadrantes" de mapa (ex: 50x50). | `$subtract`, `$mod`, `$group`, `$sort` |
| `GET` | `/api/stats/dashboard` | Retorna métricas variadas em uma única chamada. | `$facet`, `$hour` |

---

## 🧠 Lições de Arquitetura

1. **Separação de Responsabilidades (Custom Repositories):** As queries complexas não poluem os Services ou Controllers. Elas são implementadas em interfaces `Custom` usando o `MongoTemplate` de forma programática e *Type-Safe*.
2. **Profiles e Connection Pool:** Uso de `application-local.yaml` para separar credenciais e configuração de Pool de Conexões para suportar alta concorrência.
3. **Mapeamento Explícito vs Mágico:** Uso cuidadoso de mapeamento rigoroso de variáveis do Java para as colunas do MongoDB (`event_type`, `player_id`) para garantir o funcionamento perfeito do Aggregation Pipeline.
4. **Imutabilidade:** Adoção de `records` do Java 21 para garantir que os dados de transferência (DTOs) sejam Thread-Safe e imutáveis por padrão.

---
Desenvolvido com ☕ e focado em Performance Backend.
---