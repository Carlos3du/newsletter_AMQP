# Projeto Newsletter AMQP Consumidor

Este projeto é um consumidor de mensagens via AMQP utilizando Python e RabbitMQ. O projeto utiliza Maven para gerenciamento de dependências e integra o Python para a comunicação com o RabbitMQ, utilizando a biblioteca `pika`.

## Como Rodar

### 1. Clone o repositório e abra a pasta no IntelliJ IDEA

Faça o clone do repositório e abra a pasta correspondente no IntelliJ IDEA.

### 2. Instale as dependências do Maven

Ao abrir o projeto no IntelliJ IDEA, o Maven detectará automaticamente o arquivo `pom.xml`. Aceite o aviso para instalar as dependências necessárias.

### 3. Aceite o aviso para instalar o plugin Python

Abra parte do consumidor o IntelliJ IDEA pode dectectar arquivos Python no projeto eele pode solicitar a instalação do plugin Python. Aceite o aviso para garantir que o plugin seja instalado corretamente.

### 4. Selecione o Python Interpreter

Após a instalação do plugin Python, configure o Python Interpreter seguindo os passos abaixo:
- Vá até `File > Project Structure` ou pressione `Ctrl + Alt + Shift + S`.
- Na janela **Project Structure**, selecione `SDKs` na seção **Platform Settings**.
- Clique no ícone de `+` no canto superior e escolha a opção **Add Python SDK** no menu pop-up.
- Selecione o interpretador Python apropriado para o projeto (pode ser o interpretador do sistema ou um ambiente virtual).

### 5. Instalar o Pika 

`pip install pika`


### 6. Instalar o RabbitMQ ( eu nao tinha no pc então to instalando)
https://www.rabbitmq.com/docs/install-windows#installer
