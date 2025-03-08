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
GET /clientes/me

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
POST /autenticacao

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
POST /clientes

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
GET /clientes
```
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `api_key`   | `string`   | **Obrigatório**. A chave da sua API |

##### Respostas:
- **200 OK**: Retorna a lista de usuários.
- **403 Forbidden**: Acesso negado para usuários não administradores.

#### Remove um usuário de acordo com o seu ID
```bash
DELETE /clientes/${id}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`        | `string`   | **Obrigatório**. ID do usuário que deseja apagar |

##### Respostas:
- **204 No Content**: Usuário removido com sucesso.
- **404 Not Found**: Usuário não encontrado.
- **403 Forbidden**: Acesso negado.

#### Atualiza a role de um usuário de acordo com seu ID
```bash
PUT /clientes/${id}

body: {
  "role": "admin"
}
```
| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `id`        | `string`   | **Obrigatório**. ID do usuário que deseja atualizar |
| `role`      | `string`   | **Obrigatório**. A nova role do usuário |

##### Respostas:
- **200 OK**: Role atualizada com sucesso.
- **400 Bad Request**: Role inválida.
- **403 Forbidden**: Acesso negado.
- **404 Not Found**: Usuário não encontrado.
</details>

## Banco de Dados

### Modelo de Dados
- **Tabela de Clientes**: ID, nome, email, senha, role, etc.

### Esquema do Banco de Dados (PostgreSQL)
```sql
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    senha VARCHAR(255),
    role VARCHAR(255) DEFAULT 'user'
);
```

## Segurança
- Autenticação baseada em tokens JWT.
- Criptografia de senhas com bcrypt.
- Proteção contra ataques comuns (SQL Injection, XSS, CSRF).
- A API não mantém sessões, ela é **stateless**.

## Monitoramento e Logging
- A API pode utilizar Spring Boot Actuator para métricas e logs.
- Logs são armazenados utilizando SLF4J com Logback.
- Monitoramento pode ser integrado ao Prometheus e Grafana.

