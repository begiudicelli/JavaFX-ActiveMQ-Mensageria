package fumec.br.javafxactivemqmensageria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fumec/br/javafxactivemqmensageria/login.fxml"));
        Parent loginRoot = fxmlLoader.load();

        primaryStage.setTitle("Chat JMS - Login");
        primaryStage.setScene(new Scene(loginRoot));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
        launch(args);
    }
}