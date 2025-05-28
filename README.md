# Gerador de QR Code com Java e AWS S3

## Sobre o Projeto

Este projeto implementa um gerador de QR Code utilizando Java com Spring Boot e integração com Amazon S3 para armazenamento das imagens geradas. A aplicação expõe uma API REST que permite a criação de QR Codes a partir de textos fornecidos pelo usuário, gerando a imagem correspondente e armazenando-a no serviço de armazenamento em nuvem da AWS.

## Tecnologias Utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias e frameworks:

- **Java 21**: Linguagem de programação principal
- **Spring Boot 3.5.2**: Framework para criação de aplicações Java com configuração simplificada
- **ZXing 3.5.2**: Biblioteca para geração de QR Codes
- **AWS SDK 2.24.12**: SDK para integração com serviços da Amazon Web Services
- **Maven**: Ferramenta de automação de compilação e gerenciamento de dependências

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas, organizada da seguinte forma:

```
src/main/java/br/com/wm/qrcode/
├── controller/
│   └── QrCodeController.java
├── dto/
│   ├── QrCodeGenerateRequest.java
│   └── QrCodeGenerateResponse.java
├── infrastructure/
│   └── ... (implementações de infraestrutura)
├── ports/
│   └── ... (interfaces de comunicação)
├── service/
│   └── QrCodeGeneratorService.java
└── QrcodeApplication.java
```

### Componentes Principais

- **Controller**: Expõe o endpoint REST para geração de QR Code
- **Service**: Contém a lógica de negócio para geração do QR Code e upload para o S3
- **DTO**: Objetos de transferência de dados para requisição e resposta
- **Infrastructure**: Implementações concretas para acesso a recursos externos
- **Ports**: Interfaces que definem contratos para serviços externos

## Requisitos

Para executar o projeto, você precisará:

- JDK 21 ou superior
- Maven 3.8 ou superior
- Conta na AWS com acesso ao serviço S3
- Credenciais da AWS configuradas no ambiente

## Configuração

### Configuração das Credenciais AWS

Para que a aplicação possa se comunicar com o serviço S3 da AWS, é necessário configurar as credenciais de acesso. Existem várias formas de fazer isso:

1. **Arquivo de Credenciais**: Crie ou edite o arquivo `~/.aws/credentials` com o seguinte conteúdo:

```
[default]
aws_access_key_id = sua_access_key
aws_secret_access_key = sua_secret_key
```

2. **Variáveis de Ambiente**:

```bash
export AWS_ACCESS_KEY_ID=sua_access_key
export AWS_SECRET_ACCESS_KEY=sua_secret_key
export AWS_REGION=sua_regiao
```

3. **Configuração no application.properties**:

```properties
aws.accessKeyId=sua_access_key
aws.secretKey=sua_secret_key
aws.region=sua_regiao
aws.s3.bucket=nome_do_seu_bucket
```

### Configuração do Bucket S3

É necessário criar um bucket no S3 para armazenar as imagens dos QR Codes gerados. Após criar o bucket, configure o nome no arquivo `application.properties`:

```properties
aws.s3.bucket=nome_do_seu_bucket
```

## Instalação e Execução

1. Clone o repositório:

```bash
git clone https://github.com/williammian/gerador-qrcode.git
cd gerador-qrcode/qrcode
```

2. Compile o projeto com Maven:

```bash
mvn clean install
```

3. Execute a aplicação:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Uso da API

### Geração de QR Code

Para gerar um QR Code, envie uma requisição POST para o endpoint `/qrcode`:

```bash
curl -X POST http://localhost:8080/qrcode \
  -H "Content-Type: application/json" \
  -d '{"text": "https://exemplo.com"}'
```

#### Parâmetros da Requisição

O corpo da requisição deve conter um objeto JSON com o seguinte campo:

- `text`: O texto ou URL que será codificado no QR Code

#### Resposta

A resposta será um objeto JSON contendo:

```json
{
  "url": "https://nome-do-bucket.s3.amazonaws.com/qrcode/abc123.png"
}
```

Onde `url` é o endereço completo para acessar a imagem do QR Code armazenada no S3.

## Fluxo de Funcionamento

1. O cliente envia uma requisição POST com o texto a ser codificado
2. O controlador recebe a requisição e repassa para o serviço
3. O serviço utiliza a biblioteca ZXing para gerar a imagem do QR Code
4. A imagem é enviada para o bucket S3 configurado
5. A URL da imagem armazenada é retornada ao cliente

## Detalhes de Implementação

### Geração do QR Code

A geração do QR Code é realizada utilizando a biblioteca ZXing. O processo envolve:

1. Criação de uma matriz de bits com o conteúdo a ser codificado
2. Renderização da matriz em uma imagem
3. Conversão da imagem para o formato PNG

### Upload para o S3

O upload da imagem para o S3 é feito utilizando o AWS SDK para Java. O processo inclui:

1. Criação de um nome único para o arquivo
2. Configuração dos metadados do objeto
3. Upload do arquivo para o bucket configurado
4. Geração da URL pública para acesso à imagem

## Considerações de Segurança

- As credenciais da AWS devem ser mantidas em segredo e nunca devem ser commitadas no repositório
- Considere utilizar políticas de acesso adequadas para o bucket S3
- Implemente validação de entrada para evitar injeção de código malicioso
