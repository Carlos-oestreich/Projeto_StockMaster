# 🚀 StockMaster - Backend

![Status](https://img.shields.io/badge/status-finalizado-brightgreen)

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-blue?style=for-the-badge&logo=java">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=springboot">
  <img src="https://img.shields.io/badge/PostgreSQL-Database-316192?style=for-the-badge&logo=postgresql">
  <img src="https://img.shields.io/badge/MapStruct-DTO%20Mapper-orange?style=for-the-badge">
  <img src="https://img.shields.io/badge/Testes-Postman-yellow?style=for-the-badge&logo=postman">
</p>

<p align="center">API REST profissional para gestão de estoque, com arquitetura escalável, segura e pronta para integração com front-end.</p>
<p align="center"><strong>Desenvolvido exclusivamente para fins acadêmicos</strong> por <a href="https://github.com/Carlos-oestreich">Carlos Eduardo Oestreich</a> e <a href="https://github.com/larissalaumann">Larissa Maria Laumann.</p>

---

## 📌 Sobre o Projeto

O **StockMaster** é um sistema completo de gestão de estoque desenvolvido como API REST (backend) utilizando **Java 25** e **Spring Boot**.

- Estruturado seguindo padrões profissionais de mercado.
- Código modular, limpo e pronto para escala.
- Desenvolvido para fins acadêmicos, mas serve como ótima base para aplicações reais.

> **Nota:** Este repositório contém somente o backend do projeto.

---

## 🔗 Integração com o Front-end

O sistema está pronto para integração com o front-end em React.

> Repositório do front-end:  
> [https://github.com/seu-usuario/stockmaster-frontend](https://github.com/Carlos-oestreich/StockMasterReact) 

---

## 🌐 Versão em PHP

Também desenvolvemos este mesmo sistema usando PHP.  
Confira o repositório:  
[https://github.com/seu-usuario/StockMaster-php](https://github.com/Carlos-oestreich/ProjetoStockMaster.git)

---

## ✨ Funcionalidades Principais

- Autenticação com JWT
- Cadastro inicial de empresa e usuário
- Gestão de categorias e empresas
- Gestão de fornecedores e produtos
- Controle de movimentações de estoque
- Relatórios completos
- API pronta para consumo via React ou outra stack

---

## 🧱 Arquitetura

O projeto segue arquitetura em camadas, separando responsabilidades para facilitar manutenção e evolução:

```
src/main/java/br/edu/ifpr/bsi/StockMaster
├── config         # Configurações globais
├── controllers    # Endpoints REST
├── mappers        # Conversão entre DTOs e entidades (MapStruct)
├── model          # Entidades do domínio e DTOs
├── repositories   # Acesso aos dados (Spring Data JPA)
├── security       # Segurança/JWT
└── services       # Regras de negócio
```

---

## 🚀 Tecnologias Utilizadas

- **Java 25**
- **Spring Boot 4.x**
  - Spring Web
  - Spring Data JPA
  - Spring Security
- **PostgreSQL**
- **JWT** (segurança)
- **MapStruct** (DTO Mapper)
- **Lombok** (boilerplate)
- **Maven** (gerenciamento de dependências)

---

## 📡 Endpoints Exemplos

### 🔐 Autenticação

- `POST /auth/login` — Login com retorno JWT

### 📦 Produtos

- `GET    /produtos`
- `POST   /produtos`
- `PUT    /produtos/{id}`
- `DELETE /produtos/{id}`

### 🏢 Empresas

- `GET /empresas`

> Os demais endpoints seguem o padrão RESTful.

---

## 🛡️ Autenticação JWT

**Fluxo básico:**

1. **Login:**  
   `POST /auth/login`  
   Retorno: `Bearer SEU_TOKEN`

2. **Uso:**  
   Toda requisição autenticada deve enviar:  
   `Authorization: Bearer SEU_TOKEN`

---

## ⚙️ Configuração

Configure as variáveis de acesso ao banco no arquivo:

`src/main/resources/application.properties`

Exemplo:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## ▶️ Como Executar

1. **Clonar o projeto**
   ```bash
   git clone https://github.com/Carlos-oestreich/Projeto_StockMaster.git
   cd Projeto_StockMaster
   ```

2. **Editar configurações do banco**  
   Edite o arquivo `src/main/resources/application.properties`.

3. **Build**

   Para Windows:
   ```bash
   .\mvnw.cmd clean install
   ```

   Para Linux/macOS:
   ```bash
   ./mvnw clean install
   ```

4. **Executar**
   ```bash
   ./mvnw spring-boot:run
   ```

5. Acesse a aplicação:  
   [http://localhost:8080](http://localhost:8080)

---

### 💻 Ambiente de Desenvolvimento

Este projeto foi desenvolvido e testado utilizando o **IntelliJ IDEA**.  
Você pode abrir o repositório diretamente pelo IntelliJ, importar como projeto Maven e rodar a aplicação facilmente.

- Versão recomendada do IntelliJ IDEA: 2024.x ou superior
- Também pode ser executado em outras IDEs compatíveis com projetos Maven e Java 25.

---

## 🧪 Testes

Os testes e validações dos endpoints foram realizados utilizando o **Postman**.

- Você pode importar a coleção de requisições para facilitar os testes;
- Sinta-se livre para criar suas próprias requisições REST no Postman ou ferramenta similar.

---

## 🔒 Segurança

- Autenticação via JWT
- Senhas criptografadas
- Uso de DTOs
- Separação clara de responsabilidades
- **Nunca suba credenciais sensíveis no repositório!**

---

## 👨‍💻 Autores

- [Carlos Eduardo Oestreich](https://github.com/Carlos-oestreich)
- [Larissa Maria Laumann](https://github.com/larissalaumann)

---

## 📌 Observação Final

Este projeto foi desenvolvido para fins acadêmicos, mas segue boas práticas e padrões profissionais, podendo ser aproveitado como base para novos sistemas.

<div align="center">

💡 Dúvidas, feedbacks e colaborações são bem-vindos!


</div>

---

---

# 🇬🇧 English version

---

# 🚀 StockMaster - Backend

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-blue?style=for-the-badge&logo=java">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=springboot">
  <img src="https://img.shields.io/badge/PostgreSQL-Database-316192?style=for-the-badge&logo=postgresql">
  <img src="https://img.shields.io/badge/MapStruct-DTO%20Mapper-orange?style=for-the-badge">
  <img src="https://img.shields.io/badge/Testing-Postman-yellow?style=for-the-badge&logo=postman">
</p>

<p align="center">A professional REST API for inventory management, with scalable architecture, secure and ready for frontend integration.</p>
<p align="center"><strong>Developed exclusively for academic purposes</strong> by <a href="https://github.com/Carlos-oestreich">Carlos Eduardo Oestreich</a> and <a href="https://github.com/larissalaumann">Larissa Maria Laumann.</p>


---

## 📌 About the Project

**StockMaster** is a complete inventory management system developed as a REST API (backend) using **Java 25** and **Spring Boot**.

- Structured following professional industry standards.
- Modular, clean, and scale-ready codebase.
- Developed for academic purposes, but also a solid foundation for real-world projects.

> **Note:** This repository contains only the backend of the project.

---

## 🔗 Frontend Integration

The system is ready for integration with a React frontend.

> Frontend repository:  
> [https://github.com/seu-usuario/stockmaster-frontend](https://github.com/Carlos-oestreich/StockMasterReact) <!-- Replace with the correct link after creating -->

---

## 🌐 PHP Version

We have also developed this same system using PHP.  
Check out the repository:  
[https://github.com/seu-usuario/StockMaster-php](https://github.com/Carlos-oestreich/ProjetoStockMaster.git)

---

## ✨ Main Features

- Authentication with JWT
- Initial company and user registration
- Category and company management
- Supplier and product management
- Stock movement control
- Comprehensive reports
- API ready to be consumed by React or any other stack

---

## 🧱 Architecture

The project follows a layered architecture, separating responsibilities for easy maintenance and evolution:

```
src/main/java/br/edu/ifpr/bsi/StockMaster
├── config         # Global configurations
├── controllers    # REST endpoints
├── mappers        # DTO/entity conversion (MapStruct)
├── model          # Domain entities and DTOs
├── repositories   # Data access (Spring Data JPA)
├── security       # Security/JWT
└── services       # Business logic
```

---

## 🚀 Technologies Used

- **Java 25**
- **Spring Boot 4.x**
  - Spring Web
  - Spring Data JPA
  - Spring Security
- **PostgreSQL**
- **JWT** (security)
- **MapStruct** (DTO Mapper)
- **Lombok** (boilerplate)
- **Maven** (dependency management)

---

## 📡 Example Endpoints

### 🔐 Authentication

- `POST /auth/login` — Login with JWT

### 📦 Products

- `GET    /produtos`
- `POST   /produtos`
- `PUT    /produtos/{id}`
- `DELETE /produtos/{id}`

### 🏢 Companies

- `GET /empresas`

> All other endpoints follow the standard RESTful pattern.

---

## 🛡️ JWT Authentication

**Basic flow:**

1. **Login:**  
   `POST /auth/login`  
   Returns: `Bearer YOUR_TOKEN`

2. **Usage:**  
   Every authenticated request must send:  
   `Authorization: Bearer YOUR_TOKEN`

---

## ⚙️ Configuration

Configure your database variables in:

`src/main/resources/application.properties`

Example:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## ▶️ How to Run

1. **Clone the project**
   ```bash
   git clone https://github.com/Carlos-oestreich/Projeto_StockMaster.git
   cd Projeto_StockMaster
   ```

2. **Edit database settings**  
   Edit the file `src/main/resources/application.properties`.

3. **Build**

   On Windows:
   ```bash
   .\mvnw.cmd clean install
   ```

   On Linux/macOS:
   ```bash
   ./mvnw clean install
   ```

4. **Run**
   ```bash
   ./mvnw spring-boot:run
   ```

5. Access the application:  
   [http://localhost:8080](http://localhost:8080)

---

### 💻 Development Environment

This project was developed and tested using **IntelliJ IDEA**.  
You can open the repository directly in IntelliJ, import it as a Maven project, and run the application easily.

- Recommended IntelliJ IDEA version: 2024.x or later
- You can also use any IDE compatible with Maven projects and Java 25.

---

## 🧪 Testing

All endpoint tests and validations were performed using **Postman**.

- You can import a collection of requests for easier testing;
- Feel free to create your own REST requests via Postman or any similar tool.

---

## 🔒 Security

- JWT authentication
- Encrypted passwords
- Use of DTOs
- Clear separation of responsibilities
- **Never upload sensitive credentials to the repository!**

---

## 👨‍💻 Authors

- [Carlos Eduardo Oestreich](https://github.com/Carlos-oestreich)
- [Larissa Maria Laumann](https://github.com/larissalaumann)

---

## 📌 Final Note

This project was developed for academic purposes, but follows best practices and professional standards—making it a strong base for real-world applications.

<div align="center">

💡 Questions, feedback, and collaborations are welcome!


</div>
