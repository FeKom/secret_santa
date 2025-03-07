# Secret Santa

Projeto para organização de sorteios de **Amigo Secreto** com autenticação OAuth2, utilizando **Spring Boot**, **Spring Security**, **JWT**, e **PostgreSQL** em **Docker**.

O sistema permite que múltiplos usuários participem de vários grupos de sorteio, onde cada grupo pode ter vários participantes. O sistema realiza o sorteio de forma que cada usuário será tanto um sorteador quanto um sorteado, sem poder se sortear a si mesmo.

## Tecnologias Utilizadas

  - **Java 21**
  - **Spring Boot**
  - **Spring Security** com **OAuth2** para autenticação
  - **JWT (JSON Web Token)** para validação e criação de tokens
  - **JOSE Library** para manipulação de tokens e chaves RSA
  - **PostgreSQL** em container Docker
  - **Docker** para ambiente de containers

## Funcionalidades

  - **Criação de Grupos de Amigo Secreto**: Usuários podem criar grupos para sorteios.
  - **Participação em Múltiplos Grupos**: Um usuário pode ser parte de vários grupos de sorteio.
  - **Sorteio de Amigos Secretos**: Cada usuário será tanto um sorteador quanto um sorteado, garantindo que ninguém se sorteie a si mesmo.
  - **Autenticação via OAuth2**: Sistema seguro com autenticação de usuários utilizando tokens JWT.
  - **Geração e Validação de Tokens JWT**: Utilizando chaves RSA públicas e privadas armazenadas em variáveis de ambiente.

## Como Rodar o Projeto

### Requisitos

- **Java 21**
- **Docker** e **Docker Compose**
- **PostgreSQL** em container (já configurado no Docker Compose)

### Alterando a Porta do Servidor para 4200

Para configurar o Spring Boot para rodar na porta 4200, adicione a seguinte linha no arquivo `application.properties` localizado em `src/main/resources`:

```properties
server.port=4200
```

### 1. Clone o Repositório

Clone o repositório para a sua máquina local:
```bash
git clone https://github.com/FeKom/secret_santa.git
cd secret_santa
```

 Para subir o Docker e rodar em segundo plano
```bash
docker-compose up -d

```
Para rodar o aplicativo Usando maven, ou compilar e rodar o jar
```bash
mvn spring-boot:run

```
```bash
mvnw clean package
java -jar target/secret_santa-0.0.1-SNAPSHOT.jar

```


### 2. Teste os Endpoints

Para se registar e Logar caso o Token tenha expirado
