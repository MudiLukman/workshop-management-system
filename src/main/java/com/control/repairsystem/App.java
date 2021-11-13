package com.control.repairsystem;

import com.control.repairsystem.presenter.LoginPresenter;
import com.control.repairsystem.util.DatabaseHelper;
import com.control.repairsystem.view.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.time.LocalDate;

public class App extends Application{
    public static void main( String[] args )
    {
        launch(App.class, args);
    }

    public static void loadWindow(String title, Modality modality, Pane view){
        Stage stage = new Stage(StageStyle.DECORATED);
        if(view instanceof RegisterDeviceView){
            stage.setResizable(false);
        }
        else if(view instanceof AboutView){
            stage.setWidth(500);
            stage.setHeight(400);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(!stage.isFocused()){
                    stage.close();
                }
            });
        }
        else if(view instanceof SettingsView){
            stage.sizeToScene();
            stage.setResizable(false);
        }
        stage.setTitle(title);
        stage.setScene(new Scene(view));
        stage.initModality(modality);
        stage.showAndWait();
    }

    public static void switchWindow(Pane from, Pane to, String title){
        Stage stage = new Stage(StageStyle.DECORATED);
        if(to instanceof DashboardView){
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
            stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
            stage.setWidth(visualBounds.getWidth());
            stage.setHeight(visualBounds.getHeight());
            stage.setOnCloseRequest(App::onWindowCloseRequest);
        }else if(to instanceof ConfirmRegisterView){
            stage.setResizable(false);
        }

        if(to instanceof JobOrderFormView){
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            stage.setMaxHeight(visualBounds.getHeight());
        }

        stage.setTitle(title);
        stage.setScene(new Scene(to));
        ((Stage) from.getScene().getWindow()).close();
        stage.show();
    }

    private static void onWindowCloseRequest(WindowEvent event){
        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER_RIGHT);
        Button ok = new Button("Ok");
        ok.setDefaultButton(true);
        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        buttonsHBox.getChildren().addAll(ok, cancel);
        VBox pane = new VBox(5, new Label("Are you sure you want to exit RMS?"), buttonsHBox);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-padding: 10;");
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Confirm Exit");
        stage.setScene(new Scene(pane));
        stage.show();
        ok.setOnAction(event1 -> {
            System.exit(0);
        });
        cancel.setOnAction(event1 -> {
            cancel.getScene().getWindow().hide();
        });

        event.consume();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent loginView = new LoginPresenter().getView();
        Scene scene = new Scene(loginView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.show();
        new Thread(DatabaseHelper::create_all_tables).start();
    }
}
