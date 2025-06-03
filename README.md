Este projeto implementa um sistema de chat rudimentar usando Java Message Service (JMS) com Apache ActiveMQ como broker de mensagens.
📁 Estrutura do Projeto
src/
├── main/
│   ├── java/
│   │   └── com/example/chat/
│   │       ├── client/
│   │       │   └── ChatClientApp.java        # Aplicação principal
│   │       ├── controller/
│   │       │   ├── LoginController.java      # Controlador do login
│   │       │   └── ChatController.java       # Controlador do chat
│   │       ├── model/
│   │       │   └── ChatMessage.java          # Modelo da mensagem
│   │       ├── service/
│   │       │   └── JMSService.java           # Serviço JMS
│   │       └── server/
│   │           └── ChatServerApp.java        # Servidor embarcado (opcional)
│   └── resources/
│       ├── fxml/
│       │   ├── login.fxml                    # Interface de login
│       │   └── chat.fxml                     # Interface do chat
│       └── css/
│           └── styles.css                    # Estilos CSS
🚀 Como Executar
Opção 1: ActiveMQ Standalone (Recomendado)

Baixar e iniciar ActiveMQ:
bash# Baixar de: https://activemq.apache.org/components/classic/download/
# Extrair e executar:
./bin/activemq start

Compilar e executar o cliente:
bashmvn clean compile
mvn javafx:run

Funcionalidades Implementadas
✅ Cliente

 Interface gráfica com JavaFX
 Campo para digitar mensagens
 Campo para código do destinatário
 Área de exibição das mensagens recebidas
 Botão enviar e suporte a tecla Enter
 Código de usuário obrigatório
 Mensagens broadcast (todos os usuários)
 Mensagens privadas (usuário específico)
 Timestamp nas mensagens
 Scroll automático na área de mensagens

✅ Servidor/Broker

 Distribuição de mensagens via JMS
 Uso de Topics para mensagens broadcast
 Uso de Queues para mensagens privadas
 Servidor embarcado opcional

🛠️ Tecnologias Utilizadas

Java 11+
JavaFX 17 (Interface gráfica)
Apache ActiveMQ 5.17.x (Broker JMS)
Maven (Gerenciamento de dependências)
Jackson (Serialização JSON)
