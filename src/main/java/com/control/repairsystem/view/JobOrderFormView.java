package com.control.repairsystem.view;

import com.control.repairsystem.util.UtilConstants;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class JobOrderFormView extends Pane {

    private Text companyDescription;
    private Text contactAddress;
    private Text contactPhone;
    private Label formName;
    private Label collectedLabel;
    private Label nonRefFeeLabel;
    private Label collectionDateLabel;
    private Label faultsLabel;
    private ImageView bringerPhoto;
    private Label customerName;
    private Label customerAddress;
    private Label deviceType;
    private Label deviceModel;
    private Label serialNumber;
    private Text omittedParts;
    private Label registeredOn;
    private Label status;
    private Text nonRefundableFee;
    private Text collectionDate;
    private HBox totalCostHBox;
    private Label totalCostLbl;
    private Label advancePayed;
    private HBox balanceHBox;
    private Label balanceLbl;
    private Text faults;
    private Label xCode;
    private Label xCodeLabel;
    private Button print;

    public JobOrderFormView(){
        companyDescription = new Text("Sales and repair of computer games, phones, laptops\n and general contractors");
        companyDescription.setTextAlignment(TextAlignment.CENTER);
        companyDescription.setWrappingWidth(400);
        contactAddress = new Text();
        contactAddress.setTextAlignment(TextAlignment.CENTER);
        contactAddress.setWrappingWidth(400);
        contactPhone = new Text();
        contactPhone.setTextAlignment(TextAlignment.CENTER);
        contactPhone.setWrappingWidth(400);
        bringerPhoto = new ImageView();
        customerName = new Label();
        customerAddress = new Label();
        deviceType = new Label();
        deviceModel = new Label();
        serialNumber = new Label();
        omittedParts = new Text();
        omittedParts.setTextAlignment(TextAlignment.LEFT);
        omittedParts.setWrappingWidth(400);
        registeredOn = new Label();
        status = new Label();
        nonRefundableFee = new Text();
        nonRefundableFee.setTextAlignment(TextAlignment.LEFT);
        nonRefundableFee.setWrappingWidth(50);
        collectionDate = new Text();
        collectionDate.setTextAlignment(TextAlignment.LEFT);
        collectionDate.setWrappingWidth(70);
        totalCostHBox = new HBox(3);
        totalCostLbl = new Label("");
        advancePayed = new Label();
        balanceHBox = new HBox(3);
        balanceLbl = new Label("");
        faults = new Text();
        faults.setTextAlignment(TextAlignment.LEFT);
        faults.setWrappingWidth(400);
        xCode = new Label();
        xCodeLabel = new Label("X-Code:");
        print = new Button("Print");

        makeGUI();

    }

    private void makeGUI() {
        VBox layout = new VBox(3);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(12));

        VBox header = new VBox(4);
        header.setAlignment(Pos.CENTER);
        Label appName = new Label(UtilConstants.APP_NAME);
        appName.setFont(new Font(24));
        formName = new Label("Job Order Form");
        collectedLabel = new Label("Collection Date:");
        nonRefFeeLabel = new Label("Non-Refundable Fee: ");
        collectionDateLabel = new Label("Collected On:");
        faultsLabel = new Label("Faults:");
        formName.setStyle("-fx-font-weight: bold;");
        formName.setFont(new Font(20));
        companyDescription.setFont(new Font(14));
        contactAddress.setFont(new Font(14));
        contactPhone.setFont(new Font(14));
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-background-color: black;");
        header.getChildren().addAll(appName, companyDescription, contactAddress, contactPhone, formName, separator);

        VBox mainLayout = new VBox(3);
        mainLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.getChildren().add(bringerPhoto);
        bringerPhoto.setImage(new Image(JobOrderFormView.class.getResource("/old_person.png").toExternalForm()));
        bringerPhoto.setFitHeight(70);
        bringerPhoto.setFitWidth(70);
        bringerPhoto.setSmooth(true);
        mainLayout.getChildren().add(new HBox(3, new Label("Customer Name:"), customerName));
        mainLayout.getChildren().add(new HBox(3, new Label("Address:"), customerAddress));
        mainLayout.getChildren().add(new HBox(3, new Label("Device Type:"), deviceType));
        mainLayout.getChildren().add(new HBox(3, new Label("Device Model:"), deviceModel));
        mainLayout.getChildren().add(new HBox(3, new Label("Serial Number:"), serialNumber));
        mainLayout.getChildren().add(new HBox(3, new Label("Omitted Parts:"), omittedParts));
        mainLayout.getChildren().add(new HBox(3, new Label("Registered On:"), registeredOn));
        mainLayout.getChildren().add(new HBox(3, new Label("Status:"), status));

        BorderPane costPane = new BorderPane();
        HBox nonRefFeeFraction = new HBox(2, nonRefFeeLabel, nonRefundableFee);
        HBox collectionDateFraction = new HBox(2, collectionDateLabel, collectionDate);
        collectionDateLabel.setVisible(false);
        collectionDate.setVisible(false);
        VBox rightVBox = new VBox(5, nonRefFeeFraction, collectionDateFraction);
        costPane.setRight(rightVBox);

        mainLayout.getChildren().add(costPane);
        totalCostHBox.getChildren().addAll(new Label("Total Cost: "), totalCostLbl);
        totalCostHBox.setVisible(false);
        mainLayout.getChildren().add(totalCostHBox);
        mainLayout.getChildren().add(new HBox(3, new Label("Advance Payed: "), advancePayed));
        balanceHBox.getChildren().addAll(new Label("Balance to be Paid: "), balanceLbl);
        balanceHBox.setVisible(false);
        mainLayout.getChildren().add(balanceHBox);
        mainLayout.getChildren().add(new HBox(3, faultsLabel, faults));
        mainLayout.getChildren().add(new HBox(3, xCodeLabel, xCode));
        customerName.setStyle("-fx-font-weight: bold;");
        customerAddress.setStyle("-fx-font-weight: bold;");
        deviceType.setStyle("-fx-font-weight: bold;");
        deviceModel.setStyle("-fx-font-weight: bold;");
        serialNumber.setStyle("-fx-font-weight: bold;");
        omittedParts.setStyle("-fx-font-weight: bold;");
        registeredOn.setStyle("-fx-font-weight: bold;");
        status.setStyle("-fx-font-weight: bold;");
        nonRefFeeFraction.setStyle("-fx-font-weight: bold;");
        advancePayed.setStyle("-fx-font-weight: bold;");
        faults.setStyle("-fx-font-weight: bold;");
        xCode.setStyle("-fx-font-weight: bold;");

        VBox bottom = new VBox(4);
        bottom.setAlignment(Pos.CENTER);
        BorderPane signatureBlock = new BorderPane();
        signatureBlock.setRight(new Label("Managers' Sign: __________________________"));
        signatureBlock.setLeft(new Label("Customers' Sign: __________________________"));
        bottom.getChildren().addAll(signatureBlock,
                new Label("*Important: you are expected to bring this form on device collection date."), print);
        ImageView printIcon = new ImageView(new Image(JobOrderFormView.class.getResource("/print.png").toExternalForm()));
        printIcon.setFitWidth(20);
        printIcon.setFitHeight(20);
        print.setGraphic(printIcon);
        print.setFont(new Font(12));

        layout.getChildren().addAll(header, mainLayout, bottom);
        layout.setStyle("-fx-padding: 20;");
        this.getChildren().add(layout);
        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
    }

    public Text getCompanyDescription() {
        return companyDescription;
    }

    public Text getContactAddress() {
        return contactAddress;
    }

    public ImageView getBringerPhoto() {
        return bringerPhoto;
    }

    public Label getCustomerName() {
        return customerName;
    }

    public Label getCustomerAddress() {
        return customerAddress;
    }

    public Label getDeviceType() {
        return deviceType;
    }

    public Label getDeviceModel() {
        return deviceModel;
    }

    public Label getSerialNumber() {
        return serialNumber;
    }

    public Text getOmittedParts() {
        return omittedParts;
    }

    public Label getRegisteredOn() {
        return registeredOn;
    }

    public Label getStatus() {
        return status;
    }

    public Label getAdvancePayed() {
        return advancePayed;
    }


    public Text getFaults() {
        return faults;
    }

    public Label getxCode() {
        return xCode;
    }

    public Label getxCodeLabel() {
        return xCodeLabel;
    }

    public Text getContactPhone() {
        return contactPhone;
    }

    public Text getNonRefundableFee() {
        return nonRefundableFee;
    }

    public Button getPrint() {
        return print;
    }

    public Label getFormName() {
        return formName;
    }

    public void setFormName(Label formName) {
        this.formName = formName;
    }

    public Label getCollectedLabel() {
        return collectedLabel;
    }

    public void setCollectedLabel(Label collectedLabel) {
        this.collectedLabel = collectedLabel;
    }

    public Label getNonRefFeeLabel() {
        return nonRefFeeLabel;
    }

    public void setNonRefFeeLabel(Label nonRefFeeLabel) {
        this.nonRefFeeLabel = nonRefFeeLabel;
    }

    public Label getFaultsLabel() {
        return faultsLabel;
    }

    public void setFaultsLabel(Label faultsLabel) {
        this.faultsLabel = faultsLabel;
    }

    public HBox getTotalCostHBox() {
        return totalCostHBox;
    }

    public Label getTotalCostLbl() {
        return totalCostLbl;
    }

    public HBox getBalanceHBox() {
        return balanceHBox;
    }

    public Label getBalanceLbl() {
        return balanceLbl;
    }

    public Label getCollectionDateLabel() {
        return collectionDateLabel;
    }

    public Text getCollectionDate() {
        return collectionDate;
    }
}
