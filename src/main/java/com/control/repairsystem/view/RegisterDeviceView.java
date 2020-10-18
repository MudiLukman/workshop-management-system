package com.control.repairsystem.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RegisterDeviceView extends Pane{

    private ImageView bringerPhoto;
    private ChoiceBox<String> deviceType;
    private TextField deviceName;
    private TextField deviceModel;
    private TextField serialNumber;
    private TextField customerName;
    private TextField customerPhone;
    private TextField customerAddress;
    private TextArea omittedParts;
    private TextArea faults;
    private TextField nonRefundableFee;
    private TextField advancePayment;
    private TextField totalCost;
    private Label balanceRemaining;
    private Button register;
    private Button cancel;

    public RegisterDeviceView() {
        bringerPhoto = new ImageView();
        deviceType = new ChoiceBox<>();
        deviceName = new TextField();
        deviceModel = new TextField();
        serialNumber = new TextField();
        customerName = new TextField();
        customerPhone = new TextField();
        customerAddress = new TextField();
        omittedParts = new TextArea();
        faults = new TextArea();
        nonRefundableFee = new TextField();
        advancePayment = new TextField();
        totalCost = new TextField();
        balanceRemaining = new Label("");
        register = new Button("Register");
        cancel = new Button("Cancel");

        initNodes();

        makeGUI();
    }

    private void makeGUI() {

        VBox layout = new VBox(5);
        layout.setAlignment(Pos.CENTER);
        Label sectionName = new Label("Register A New Device");
        sectionName.setFont(new Font(20));
        sectionName.setAlignment(Pos.CENTER);

        VBox deviceDetails = new VBox(7);
        deviceDetails.getChildren().add(new HBox(4, new Label("Device Type:"), deviceType));
        deviceDetails.getChildren().add(new HBox(4, new Label("Device Name:"), deviceName));
        deviceDetails.getChildren().add(new HBox(4, new Label("Device Model:"), deviceModel));
        deviceDetails.getChildren().add(new HBox(4, new Label("Serial/IMEI Number:"), serialNumber));
        deviceDetails.getChildren().add(new HBox(4, new Label("Customer Name:"), customerName));
        deviceDetails.getChildren().add(new HBox(4, new Label("Customer Phone:"), customerPhone));
        deviceDetails.getChildren().add(new HBox(4, new Label("Customer Address:"), customerAddress));
        omittedParts.setPrefColumnCount(15);
        omittedParts.setPrefRowCount(3);
        omittedParts.setWrapText(true);
        deviceDetails.getChildren().add(new HBox(4, new Label("Omitted Parts:"), omittedParts));
        faults.setPrefColumnCount(15);
        faults.setPrefRowCount(3);
        faults.setWrapText(true);
        deviceDetails.getChildren().add(new HBox(4, new Label("Faults:"), faults));
        deviceDetails.getChildren().add(new HBox(4, new Label("Non-Refundable Fee: "), nonRefundableFee));
        //deviceDetails.getChildren().add(new HBox(4, new Label("Total Cost: ₦"), totalCost));
        deviceDetails.getChildren().add(new HBox(4, new Label("Advance paid: "), advancePayment));
        //deviceDetails.getChildren().add(new HBox(4, new Label("Balance: ₦"), balanceRemaining));

        HBox bottom = new HBox(10);
        bottom.getChildren().addAll(register, cancel);
        bottom.setAlignment(Pos.CENTER);

        HBox passportSection = new HBox(bringerPhoto);
        passportSection.setMaxSize(bringerPhoto.getFitWidth(), bringerPhoto.getFitHeight());
        passportSection.setPrefWidth(Double.MAX_VALUE);
        passportSection.setPrefHeight(Double.MAX_VALUE);
        passportSection.setAlignment(Pos.CENTER_LEFT);
        passportSection.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");

        layout.getChildren().addAll(sectionName, new Label("passport:"), passportSection, deviceDetails, bottom);
        layout.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        this.getChildren().add(layout);

    }

    private void initNodes() {
        bringerPhoto.setFitWidth(100);
        bringerPhoto.setFitHeight(100);
        deviceType.getItems().addAll("Phone", "Laptop", "Game Console");
        nonRefundableFee.setPrefColumnCount(7);
        nonRefundableFee.setEditable(false);
        nonRefundableFee.setText("100");
        advancePayment.setPrefColumnCount(7);
        totalCost.setPrefColumnCount(7);
    }

    public ImageView getBringerPhoto() {
        return bringerPhoto;
    }

    public ChoiceBox<String> getDeviceType() {
        return deviceType;
    }

    public TextField getDeviceModel() {
        return deviceModel;
    }

    public TextField getSerialNumber() {
        return serialNumber;
    }

    public TextField getCustomerName() {
        return customerName;
    }

    public TextField getCustomerPhone() {
        return customerPhone;
    }

    public TextField getCustomerAddress() {
        return customerAddress;
    }

    public TextArea getOmittedParts() {
        return omittedParts;
    }

    public TextArea getFaults() {
        return faults;
    }

    public TextField getNonRefundableFee() {
        return nonRefundableFee;
    }

    public TextField getAdvancePayment() {
        return advancePayment;
    }

    public TextField getTotalCost() {
        return totalCost;
    }

    public Label getBalanceRemaining() {
        return balanceRemaining;
    }

    public Button getRegister() {
        return register;
    }

    public Button getCancel() {
        return cancel;
    }

    public TextField getDeviceName() {
        return deviceName;
    }
}
