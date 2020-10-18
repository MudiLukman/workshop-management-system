package com.control.repairsystem.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.control.repairsystem.util.UtilConstants.APP_NAME;

public class LoginView extends GridPane {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button login;

    public LoginView(){
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        login = new Button("Login");

        makeGUI();
    }

    private void makeGUI() {
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        this.setHgap(5);
        this.setVgap(10);
        Text softwareName = new Text(APP_NAME);
        GridPane.setConstraints(softwareName, 0, 0, 2, 1);
        softwareName.setFont(new Font(24));
        this.addRow(0, softwareName);
        this.addRow(1, new Label("Admin Login"));
        this.addRow(2, new Label("Username:"), usernameField);
        this.addRow(3, new Label("Password:"), passwordField);
        this.add(login, 1, 4, 2, 1);
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLogin() {
        return login;
    }

}
