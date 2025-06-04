package fumec.br.javafxactivemqmensageria.model.services;

import fumec.br.javafxactivemqmensageria.model.entities.ChatMessage;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.function.Consumer;

public class JMSService {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String BROADCAST_TOPIC = "chat.broadcast";
    private static final String PRIVATE_QUEUE_PREFIX = "chat.private.";

    private Connection connection;
    private Session session;
    private MessageProducer broadcastProducer;
    private MessageConsumer broadcastConsumer;
    private MessageConsumer privateConsumer;

    private final String userCode;

    public JMSService(String userCode) throws JMSException {
        this.userCode = userCode;
        initializeConnection();
    }

    private void initializeConnection() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic broadcastTopic = session.createTopic(BROADCAST_TOPIC);
        broadcastProducer = session.createProducer(broadcastTopic);
        broadcastConsumer = session.createConsumer(broadcastTopic);

        Queue privateQueue = session.createQueue(PRIVATE_QUEUE_PREFIX + userCode);
        privateConsumer = session.createConsumer(privateQueue);

        connection.start();
    }

    public void sendBroadcastMessage(String content) throws JMSException {
        ChatMessage message = new ChatMessage(userCode, content);
        sendMessage(message, broadcastProducer);
    }

    public void sendPrivateMessage(String recipientCode, String content) throws JMSException {
        ChatMessage message = new ChatMessage(userCode, recipientCode, content);
        Queue recipientQueue = session.createQueue(PRIVATE_QUEUE_PREFIX + recipientCode);

        try (MessageProducer privateProducer = session.createProducer(recipientQueue)) {
            sendMessage(message, privateProducer);
        }
    }

    private void sendMessage(ChatMessage message, MessageProducer producer) throws JMSException {
        ObjectMessage objectMessage = session.createObjectMessage(message);
        producer.send(objectMessage);
    }

    public void setBroadcastMessageListener(Consumer<ChatMessage> messageHandler) throws JMSException {
        broadcastConsumer.setMessageListener(message -> {
            try {
                if (message instanceof ObjectMessage objectMessage) {
                    ChatMessage chatMessage = (ChatMessage) objectMessage.getObject();
                    if (!chatMessage.getSenderCode().equals(userCode)) {
                        messageHandler.accept(chatMessage);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem broadcast: " + e.getMessage());
            }
        });
    }

    public void setPrivateMessageListener(Consumer<ChatMessage> messageHandler) throws JMSException {
        privateConsumer.setMessageListener(message -> {
            try {
                if (message instanceof ObjectMessage objectMessage) {
                    ChatMessage chatMessage = (ChatMessage) objectMessage.getObject();
                    messageHandler.accept(chatMessage);
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem privada: " + e.getMessage());
            }
        });
    }

    public void close() {
        try {
            if (broadcastConsumer != null) broadcastConsumer.close();
            if (privateConsumer != null) privateConsumer.close();
            if (broadcastProducer != null) broadcastProducer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println("Erro ao fechar conexões JMS: " + e.getMessage());
        }
    }
}
