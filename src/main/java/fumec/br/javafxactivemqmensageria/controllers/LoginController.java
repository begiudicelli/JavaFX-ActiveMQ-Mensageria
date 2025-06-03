package fumec.br.javafxactivemqmensageria.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField userCodeField;
    @FXML private Button connectButton;
    @FXML private Button cancelButton;
    @FXML private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
    }

    private void setupUI() {
        Platform.runLater(() -> userCodeField.requestFocus());

        userCodeField.textProperty().addListener((_, _, newValue) -> {
            connectButton.setDisable(newValue.trim().isEmpty());
            statusLabel.setText("");
        });

        connectButton.setDisable(true);
    }

    @FXML
    private void onConnect() {
        String userCode = userCodeField.getText().trim();

        if (userCode.isEmpty()) {
            showStatus("Por favor, digite um código de usuário.", true);
            return;
        }

        if (!isValidUserCode(userCode)) {
            showStatus("Código inválido. Use apenas letras, números e underscore (2-20 caracteres).", true);
            return;
        }

        showStatus("Conectando...", false);
        connectButton.setDisable(true);

        // conexao ocorre em thread separada para nao ocupar UI!!!
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Platform.runLater(() -> {
                    try {
                        openChatWindow(userCode);
                        closeLoginWindow();
                    } catch (IOException e) {
                        showStatus("Erro ao abrir janela do chat: " + e.getMessage(), true);
                        connectButton.setDisable(false);
                        System.err.println(e.getMessage());
                    }
                });

            } catch (InterruptedException e) {
                Platform.runLater(() -> {
                    showStatus("Conexão interrompida.", true);
                    connectButton.setDisable(false);
                });
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @FXML
    private void onCancel() {
        closeLoginWindow();
    }

    @FXML
    private void onUserCodeFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !connectButton.isDisabled()) {
            onConnect();
        }
    }

    private boolean isValidUserCode(String code) {
        return code.matches("^[a-zA-Z0-9_]+$") && code.length() >= 2 && code.length() <= 20;
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: red;");
        } else {
            statusLabel.setStyle("-fx-text-fill: blue;");
        }
    }

    private void openChatWindow(String userCode) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fumec/br/javafxactivemqmensageria/chat.fxml"));
            Parent chatRoot = fxmlLoader.load();

            ChatController chatController = fxmlLoader.getController();

            if (chatController != null) {
                chatController.initializeJMS(userCode);
            }

            Stage chatStage = new Stage();
            chatStage.setTitle("Chat JMS - " + userCode);
            chatStage.setScene(new Scene(chatRoot, 600, 500));
            chatStage.setMinWidth(500);
            chatStage.setMinHeight(400);

            chatStage.setOnCloseRequest(_ -> {
                if (chatController != null) {
                    chatController.shutdown();
                }
            });

            chatStage.show();

        } catch (Exception e) {
            throw new IOException("Falha ao carregar a janela do chat", e);
        }
    }

    private void closeLoginWindow() {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Erro ao fechar janela de login: " + e.getMessage());
        }
    }
}