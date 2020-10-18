package com.control.repairsystem.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConfirmRegisterView extends BorderPane {

    private Button proceed;
    private Button cancel;

    public ConfirmRegisterView(){
        proceed = new Button("Proceed");
        cancel = new Button("Cancel");
        setupGUI();
    }

    private void setupGUI() {
        VBox top = new VBox(3);
        top.getChildren().add(new Label("Confirm Submission"));
        top.getChildren().add(new Label("This will send an X-CODE via"));
        top.getChildren().add(new Label("SMS to the customers phone. proceed?"));
        top.setPadding(new Insets(7));
        this.setTop(top);
        HBox bottom = new HBox(10);
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(proceed, cancel);
        proceed.setDefaultButton(true);
        cancel.setCancelButton(true);
        this.setBottom(bottom);
        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
    }

    public Button getProceed() {
        return proceed;
    }

    public Button getCancel() {
        return cancel;
    }
}
