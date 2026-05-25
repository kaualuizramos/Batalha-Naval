# ⚓ Projeto Batalha Naval

## 🏛️ Organização do Projeto

* **`config`**: Cuida de ler o `game.properties` para mudar o jogo sem mexer no código.
* **`database`**: Centraliza o SQL usando SQLite via JDBC direto.
* **`domain.model`**: O coração do jogo (Tabuleiro, Navio, Coordenada).
* **`domain.service`**: Os motores do jogo — o `GameEngine` que dita os turnos, o `FleetValidator` que barra trapaças na frota e a IA da CPU (`HuntStrategy`).
* **`ui`**: Interface de terminal.

---

## 🎲 Configurações Dinâmicas (`game.properties`)

* Altera o tamanho do mapa (`board.size`).
* Modifica os nomes e tamanhos da frota (`fleet.sizes` / `fleet.names`).
* Liga, desliga ou migra o banco de dados automaticamente (`db.enabled`).

---

## 🕹️ Como Rodar e Jogar

### 1. Compilar o projeto
Para baixar as dependências (Driver do SQLite e JUnit 5) e buildar o projeto:
```bash
mvn clean package