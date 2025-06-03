package fumec.br.javafxactivemqmensageria.controllers;

import fumec.br.javafxactivemqmensageria.model.entities.ChatMessage;
import fumec.br.javafxactivemqmensageria.model.entities.MessageType;
import fumec.br.javafxactivemqmensageria.model.services.JMSService;
import jakarta.jms.JMSException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML private Label userLabel;
    @FXML private TextArea messagesArea;
    @FXML private TextField recipientField;
    @FXML private TextField messageField;
    @FXML private Button exitButton;

    private JMSService jmsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
    }

    private void setupUI() {
        messagesArea.setEditable(false);
        messagesArea.setWrapText(true);

        messageField.setOnAction(_ -> onSendMessage());
        recipientField.setOnAction(_ -> messageField.requestFocus());

        Platform.runLater(() -> messageField.requestFocus());
    }

    public void initializeJMS(String userCode) {
        userLabel.setText("Usuário: " + userCode);

        try {
            jmsService = new JMSService(userCode);
            setupMessageListeners();
            addSystemMessage();
        } catch (JMSException e) {
            showError("Erro ao conectar com o servidor",
                    "Não foi possível estabelecer conexão com o servidor JMS: " + e.getMessage());
        }
    }

    private void setupMessageListeners() throws JMSException {
        jmsService.setBroadcastMessageListener(message ->
                Platform.runLater(() -> addMessage(message))
        );

        jmsService.setPrivateMessageListener(message ->
                Platform.runLater(() -> addMessage(message))
        );
    }

    @FXML
    private void onSendMessage() {
        String content = messageField.getText().trim();
        if (content.isEmpty()) {
            return;
        }

        String recipient = recipientField.getText().trim();

        try {
            if (recipient.isEmpty()) {
                jmsService.sendBroadcastMessage(content);
                addSentMessage(content, "todos");
            } else {
                jmsService.sendPrivateMessage(recipient, content);
                addSentMessage(content, recipient);
            }

            messageField.clear();
            messageField.requestFocus();

        } catch (JMSException e) {
            showError("Erro ao enviar mensagem", e.getMessage());
        }
    }

    @FXML
    private void onMessageFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onSendMessage();
        }
    }

    @FXML
    private void onClearMessages() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar limpeza");
        alert.setHeaderText("Limpar todas as mensagens");
        alert.setContentText("Deseja realmente limpar todas as mensagens da tela?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            messagesArea.clear();
        }
    }

    @FXML
    private void onExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar saída");
        alert.setHeaderText("Sair do chat");
        alert.setContentText("Deseja realmente sair do chat?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            closeApplication();
        }
    }

    private void addMessage(ChatMessage message) {
        String formattedMessage = message.getFormattedMessage();
        messagesArea.appendText(formattedMessage + "\n");
        scrollToBottom();
    }

    private void addSentMessage(String content, String recipient) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeStr = LocalDateTime.now().format(formatter);
        String formattedMessage;

        if ("todos".equals(recipient)) {
            formattedMessage = String.format("[%s] Você (para todos): %s", timeStr, content);
        } else {
            formattedMessage = String.format("[%s] Você -> %s: %s", timeStr, recipient, content);
        }

        messagesArea.appendText(formattedMessage + "\n");
        scrollToBottom();
    }

    private void addSystemMessage() {
        ChatMessage systemMessage = new ChatMessage("SISTEMA", "Conectado ao chat. Bem-vindo!");
        systemMessage.setType(MessageType.SYSTEM);
        addMessage(systemMessage);
    }

    private void scrollToBottom() {
        messagesArea.setScrollTop(Double.MAX_VALUE);
    }

    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void closeApplication() {
        if (jmsService != null) {
            jmsService.close();
        }

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void shutdown() {
        if (jmsService != null) {
            jmsService.close();
        }
    }
}