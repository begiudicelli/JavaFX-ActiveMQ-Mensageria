module fumec.br.javafxactivemqmensageria {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires java.naming;
    requires jakarta.messaging;
    requires activemq.client;
    requires java.transaction.xa;

    opens fumec.br.javafxactivemqmensageria to javafx.fxml;
    opens fumec.br.javafxactivemqmensageria.controllers to javafx.fxml;


    exports fumec.br.javafxactivemqmensageria;
    exports fumec.br.javafxactivemqmensageria.controllers;
    exports fumec.br.javafxactivemqmensageria.model.entities to com.fasterxml.jackson.databind;
}
