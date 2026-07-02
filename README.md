# SerWeJa

**SerWeJa** é um web server HTTP simples desenvolvido em **Java puro**, utilizando `ServerSocket`, `Socket`, `OutputStream` e manipulação manual de requisições e respostas HTTP.

O objetivo do projeto é entender, na prática, como um servidor web funciona por baixo dos frameworks modernos, servindo arquivos estáticos e montando respostas HTTP manualmente.

---

## Sobre o projeto

Este projeto foi criado como estudo de fundamentos de backend, redes e protocolo HTTP.

Em vez de utilizar Spring Boot, Tomcat, Express, Nginx ou Apache, o SerWeJa implementa manualmente o fluxo básico de um servidor HTTP:

```txt
Navegador
   ↓
Requisição HTTP
   ↓
ServerSocket Java
   ↓
Leitura da request
   ↓
Busca do arquivo no diretório raiz
   ↓
Montagem dos headers HTTP
   ↓
Envio do body em bytes
   ↓
Resposta exibida no navegador
```

---

## Funcionalidades

Atualmente, o SerWeJa consegue:

- Escutar conexões HTTP em uma porta definida
- Ler requisições `GET`
- Interpretar o caminho solicitado pelo navegador
- Servir arquivos estáticos a partir de um diretório raiz
- Servir páginas HTML
- Servir arquivos CSS
- Servir imagens PNG, JPG, JPEG e GIF
- Servir arquivos TXT, JSON, PDF e XML
- Definir `Content-Type` de acordo com a extensão do arquivo
- Definir `Content-Length` corretamente
- Retornar status `200 OK`
- Retornar status `404 Not Found`
- Exibir uma página 404 personalizada
- Servir uma página padrão quando o caminho solicitado é um diretório

---

## Tecnologias utilizadas

- Java
- ServerSocket
- Socket
- OutputStream
- Files API
- HTTP/1.1

---

## Estrutura básica

Exemplo de estrutura do diretório servido:

```txt
serweja/
├── index.html
├── index.css
├── pagina.html
├── pagina.css
├── 404.html
├── 404.css
└── imagem.png
```

O servidor busca os arquivos a partir do `DOCUMENT_ROOT` definido na classe de configuração.

Exemplo:

```java
public static final String DOCUMENT_ROOT = "C:\\Users\\diego\\OneDrive\\Documentos\\serweja";
```

---

## Configuração de MIME Types

O projeto possui um mapeamento simples de extensões para tipos de conteúdo HTTP:

```java
put("html", "text/html");
put("htm", "text/html");
put("css", "text/css");
put("js", "text/javascript");
put("txt", "text/plain");
put("json", "application/json");
put("xml", "application/xml");
put("jpg", "image/jpeg");
put("jpeg", "image/jpeg");
put("png", "image/png");
put("gif", "image/gif");
put("pdf", "application/pdf");
```

Esse mapeamento é usado para enviar o header `Content-Type` corretamente para o navegador.

---

## Exemplo de resposta HTTP

Uma resposta enviada pelo SerWeJa segue a estrutura básica do HTTP:

```http
HTTP/1.1 200 OK
Date: 2026-07-02
Content-Type: text/html
Content-Length: 523
Connection: close

<html>
    ...
</html>
```

A separação entre headers e body é feita com uma linha vazia:

```txt
\r\n
```

Depois do body, nenhum byte extra é enviado, evitando problemas com arquivos binários como imagens e PDFs.

---

## Como executar

Clone o repositório:

```bash
git clone https://github.com/seu-usuario/serweja.git
```

Entre na pasta do projeto:

```bash
cd serweja
```

Compile o projeto:

```bash
javac -d out src/br/com/sadocktech/serweja/**/*.java
```

Execute o servidor:

```bash
java -cp out br.com.sadocktech.serweja.Main
```

Acesse no navegador:

```txt
http://localhost:8055
```

Ou diretamente:

```txt
http://localhost:8055/index.html
```

---

## Exemplo de uso

Ao acessar:

```txt
http://localhost:8055/index.html
```

O servidor procura o arquivo:

```txt
DOCUMENT_ROOT/index.html
```

Se o arquivo existir, retorna:

```http
HTTP/1.1 200 OK
```

Se o arquivo não existir, retorna:

```http
HTTP/1.1 404 Not Found
```

E exibe a página personalizada de erro.

---

## O que aprendi com este projeto

Durante o desenvolvimento do SerWeJa, foram estudados conceitos como:

- Como o navegador faz requisições HTTP
- Como ler dados vindos de um socket
- Como montar uma resposta HTTP manualmente
- Diferença entre headers e body
- Uso correto de `Content-Type`
- Uso correto de `Content-Length`
- Como servir arquivos binários sem corrompê-los
- Por que não usar `PrintWriter` para arquivos como imagens e PDFs
- Como o navegador solicita CSS e imagens separadamente
- Como servidores como Tomcat, Apache e Nginx abstraem esse processo

---

## Próximas melhorias

Algumas melhorias planejadas para o projeto:

- Suporte a múltiplas conexões com threads
- Logs de requisições no console
- Tratamento de query params
- Suporte básico a `POST`
- Página 500 personalizada
- Proteção contra path traversal
- Suporte a arquivos maiores usando stream em vez de `readAllBytes`
- Configuração externa de porta e diretório raiz
- Melhor organização com classes `Request`, `Response` e `StaticFileHandler`
- Suporte a Keep-Alive
- Suporte a cache HTTP
- Suporte a compressão GZIP

---

## Possível log futuro

Exemplo de log planejado:

```txt
[2026-07-02 18:40:22] GET /index.html -> 200 OK
[2026-07-02 18:40:23] GET /index.css -> 200 OK
[2026-07-02 18:40:30] GET /home.html -> 404 Not Found
```

---

## Objetivo

O SerWeJa não tem como objetivo substituir servidores web reais em produção.

O objetivo principal é estudar os fundamentos de HTTP, redes e backend de forma prática, entendendo o que acontece por baixo de frameworks e servidores robustos.

---

## Status do projeto

Projeto em desenvolvimento.

Atualmente funcional para servir arquivos estáticos básicos e páginas de erro personalizadas.

---

## Autor

Desenvolvido por **Diego Sadock**.

Backend Developer em formação, estudando Java, Spring Boot, arquitetura backend e fundamentos de sistemas web.

---

## Licença

Este projeto está sob a licença MIT.
