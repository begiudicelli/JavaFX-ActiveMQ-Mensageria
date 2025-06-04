💬 Sistema de Chat JMS
Um sistema de chat em tempo real implementado com Java Message Service (JMS) e Apache ActiveMQ, oferecendo uma interface gráfica moderna e funcionalidades completas de comunicação.

🌟 Características

Interface gráfica moderna com JavaFX
Mensagens em tempo real via JMS
Chat público (broadcast para todos)
Mensagens privadas entre usuários
Timestamps automáticos
Scroll automático na área de mensagens
Arquitetura robusta com ActiveMQ

🚀 Como Executar
Opção 1: ActiveMQ Standalone (Recomendado)

Baixar e iniciar ActiveMQ:
bash# Baixar de: https://activemq.apache.org/components/classic/download/
# Extrair e executar:
./bin/activemq start

Compilar e executar o cliente:
bashmvn clean compile
mvn javafx:run


✨ Funcionalidades Implementadas

✅ Cliente

🖥️ Interface gráfica com JavaFX

✍️ Campo para digitar mensagens

👤 Campo para código do destinatário

📺 Área de exibição das mensagens recebidas

🔄 Botão enviar e suporte a tecla Enter

🆔 Código de usuário obrigatório

📢 Mensagens broadcast (todos os usuários)

💬 Mensagens privadas (usuário específico)

⏰ Timestamp nas mensagens

📜 Scroll automático na área de mensagens

✅ Servidor/Broker

📤 Distribuição de mensagens via JMS

📡 Uso de Topics para mensagens broadcast

📫 Uso de Queues para mensagens privadas

🔧 Servidor embarcado opcional

🛠️ Tecnologias Utilizadas
Java11+ JavaFX17 Apache ActiveMQ5.17.xBroker JMSMaven Jackson

📝 Licença
Este projeto é desenvolvido para fins educacionais e demonstração de conceitos JMS.
