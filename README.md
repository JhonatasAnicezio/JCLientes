# JClients - API de Gerenciamento de Clientes

## Introdução

### Visão Geral
Esta API fornece funcionalidades para gerenciar informações de clientes, incluindo criação, leitura, atualização e exclusão de registros. Ela também oferece um sistema de autenticação e autorização para proteger os dados e funcionalidades da aplicação.

<details>
  <summary><strong>Tecnologias Utilizadas</strong></summary><br />
  
- **Linguagem de Programação**: Java 17
- **Framework**: Spring Boot 3.4.3
- **Banco de Dados**: PostgreSQL e H2 (para testes)
- **Segurança**: Spring Security e JWT (JSON Web Tokens)
- **Outras Dependências**: Spring Data JPA, Lombok, Spring Boot Actuator, Spring Boot Validation
</details>

<details>
  <summary><strong>Segurança</strong></summary><br />
  
- A API utiliza um sistema de segurança para proteger os endpoints.
- A autenticação é baseada em tokens JWT.
- As senhas são armazenadas de forma segura, utilizando criptografia bcrypt.
- As permissões são baseadas em roles (USER e ADMIN).
  </details>

## Arquitetura

<details>
  <summary><strong>Visão Geral</strong></summary><br />
  
A aplicação segue uma arquitetura em camadas:
- **Controller**: Recebe as requisições HTTP e retorna as respostas.
- **Service**: Contém a lógica de negócios.
- **Repository**: Gerencia a comunicação com o banco de dados.
- **Security**: Responsável pela autenticação e autorização.
</details>

<details>
  <summary><strong>Fluxo de Autenticação</strong></summary><br />

1. O cliente envia suas credenciais para o endpoint de autenticação.
2. A API verifica as credenciais e gera um token JWT de acesso.
3. O token é retornado ao cliente.
4. O cliente inclui o token no cabeçalho das requisições subsequentes.
5. A API valida o token e autoriza o acesso aos endpoints protegidos.
</details>

## API

<details>
  <summary><strong>Endpoints</strong></summary><br />

#### Retorna os dados do usuário de acordo com o token
```bash
GET /clients/me

header: {
  "authorization": "Bearer token"
}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `authorization` | `string` | **Obrigatório**. O token JWT gerado pelo login |

##### Respostas:
- **200 OK**: Retorna os dados do usuário.
- **401 Unauthorized**: Token inválido ou ausente.

#### Retorna um token referente ao usuário do login
```bash
POST /authentication

body: {
  "email": "user@user.com",
  "password": "secret_user"
}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `email`      | `string` | **Obrigatório**. O email do seu usuário |
| `password`   | `string` | **Obrigatório**. A senha do seu usuário |

##### Respostas:
- **200 OK**: Retorna o token JWT.
- **400 Bad Request**: Campos inválidos.
- **401 Unauthorized**: Credenciais incorretas.

#### Realiza o cadastro de um novo usuário e retorna um token referente ao login
```bash
POST /clients

body: {
  "email": "venenozo@gmail.com",
  "password": "mod100%feliz",
  "name": "Kageyama Mob",
  "role": "user"
}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `email`     | `string`   | **Obrigatório**. O email do seu usuário |
| `password`  | `string`   | **Obrigatório**. A senha do seu usuário |
| `name`      | `string`   | **Obrigatório**. O nome do seu usuário |
| `role`      | `string`   | **Opcional**. A role do usuário (padrão: user) |

##### Respostas:
- **201 Created**: Usuário cadastrado e token retornado.
- **400 Bad Request**: Campos inválidos ou email já cadastrado.
</details>

<details>
  <summary><strong>Rotas restritas a usuários administradores</strong></summary><br />

Nessas rotas é obrigatório a realização do login por parte de um administrador.

#### Retorna todos os usuários
```bash
GET /clients

header: {
  "authorization": "Bearer token"
}
```
| Parâmetro   | Tipo       | Descrição                                                                     |
| :---------- | :--------- |:------------------------------------------------------------------------------|
| `authorization` | `string` | **Obrigatório**. O token JWT de um administrador ou gerente gerado pelo login |

##### Respostas:
- **200 OK**: Retorna a lista de usuários.
- **403 Forbidden**: Acesso negado para usuários não administradores.

#### Remove um usuário de acordo com o seu ID
```bash
DELETE /clients/${id}

header: {
  "authorization": "Bearer token"
}
```
| Parâmetro   | Tipo       | Descrição                                                          |
| :---------- | :--------- |:-------------------------------------------------------------------|
| `id`        | `string`   | **Obrigatório**. ID do usuário que deseja apagar                   |
| `authorization` | `string` | **Obrigatório**. O token JWT de um administrador gerado pelo login |


##### Respostas:
- **204 No Content**: Usuário removido com sucesso.
- **404 Not Found**: Usuário não encontrado.
- **403 Forbidden**: Acesso negado.

#### Atualiza a role de um usuário de acordo com seu ID
```bash
PUT /clients/${id}

body: {
  "role": "admin"
}

header: {
  "authorization": "Bearer token"
}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`        | `string`   | **Obrigatório**. ID do usuário que deseja atualizar |
| `role`      | `string`   | **Obrigatório**. A nova role do usuário |
| `authorization` | `string` | **Obrigatório**. O token JWT de um administrador gerado pelo login |

##### Respostas:
- **200 OK**: Role atualizada com sucesso.
- **400 Bad Request**: Role inválida.
- **403 Forbidden**: Acesso negado.
- **404 Not Found**: Usuário não encontrado.
</details>

## Banco de Dados

### Modelo de Dados
- **Tabela de Clientes**: ID, name, email, password, role.

### Esquema do Banco de Dados (PostgreSQL)
```sql
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    name VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(255)
);
```

<details>
  <summary><strong>Criação Automática do Banco de Dados</strong></summary><br />

## Como o Banco de Dados é Criado

A aplicação Spring Boot utiliza o PostgreSQL como banco de dados e configura a criação do banco automaticamente, se necessário. Se o banco de dados não existir, o Spring Boot irá criá-lo automaticamente com base nas configurações definidas no arquivo `application.properties`. A configuração do Hibernate está preparada para gerenciar a criação e atualização do banco de dados conforme as entidades JPA.

A configuração de banco de dados no `application.properties` é a seguinte:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clientsdb
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=admin
spring.datasource.password=senhasupersecreta
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

A propriedade `spring.jpa.hibernate.ddl-auto` pode ser configurada de diferentes formas para controlar como o banco de dados será criado ou atualizado. Durante o desenvolvimento, é possível usar a opção `update`, que cria ou altera o banco de dados automaticamente. Em ambientes de produção, recomenda-se desativar a criação automática e configurar o banco de dados manualmente.

#### Exemplo de Configuração application.properties

```properties
spring.jpa.hibernate.ddl-auto=update
```

Isso permite que a aplicação crie ou altere a estrutura do banco de dados automaticamente com base nas suas entidades JPA.

Se necessário, você pode também configurar o banco de dados manualmente com o seguinte comando SQL:

```sql
CREATE DATABASE clientsdb;
```
Após a criação do banco de dados, a aplicação pode ser executada normalmente.
</details>

<details>
  <summary><strong>Criação e Execução do Banco de Dados com Docker</strong></summary><br>

  ## Configuração do Banco de Dados Localmente com Docker

  Para rodar o banco de dados PostgreSQL localmente, você pode utilizar o Docker. O arquivo docker-compose.yml a seguir configura o PostgreSQL, criando o banco de dados automaticamente ao iniciar o container.

### Exemplo de Configuração `docker-compose.yml`

```yaml
  version: '3.8'
  
  services:
    postgres:
      image: postgres:latest
      container_name: clientsdb
      restart: always
      environment:
        POSTGRES_DB: clientsdb
        POSTGRES_USER: admin
        POSTGRES_PASSWORD: senhasecreta
      ports:
        - "5432:5432"
  ```

### Explicação das Configurações

- **POSTGRES_DB**: Nome do banco de dados que será criado (neste caso, `clientsdb`).
- **POSTGRES_USER**: Nome de usuário para acessar o banco de dados (neste caso, `admin`).
- **POSTGRES_PASSWORD**: Senha para o usuário (neste caso, `senhasecreta`).
- **ports**: Mapeia a porta 5432 do container para a porta 5432 da sua máquina local, permitindo que a aplicação se conecte ao banco.

### Como Executar

Para rodar o PostgreSQL com Docker, basta executar o comando:

```bash
  docker-compose up
```

Isso irá iniciar o container do PostgreSQL e criar o banco de dados clientsdb automaticamente.

### Integração com a Aplicação

Após rodar o banco localmente, a aplicação Spring Boot irá se conectar ao PostgreSQL conforme configurado no `application.properties` ou `docker-compose.yml`, utilizando a URL `jdbc:postgresql://localhost:5432/clientsdb`.

Este método facilita o processo de desenvolvimento e teste da aplicação sem precisar de uma instalação manual do banco de dados.

</details>

## Segurança
- Autenticação baseada em tokens JWT.
- Criptografia de senhas com bcrypt.
- Proteção contra ataques comuns (SQL Injection, XSS).
- A API não mantém sessões, ela é **stateless**.

## Monitoramento e Logging
- A API pode utilizar Spring Boot Actuator para métricas e logs.
- Logs são armazenados utilizando SLF4J com Logback.
- Monitoramento pode ser integrado ao Prometheus e Grafana.

