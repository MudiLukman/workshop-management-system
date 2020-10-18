package com.control.repairsystem.view;

import com.control.repairsystem.util.UtilConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SettingsView extends BorderPane{

    private Accordion changeAdminPasswordBlock;
    private ImageView adminPhoto;
    private Hyperlink updatePhoto;
    private TextField phoneNumbers;
    private Button updatePhone;
    private TextArea address;
    private RadioButton phonesFee;
    private RadioButton gamesFee;
    private RadioButton computerFee;
    private TextField feeField;
    private PasswordField newPassword;
    private PasswordField confirmPassword;
    private Button changePassword;
    private Button saveFee;
    private Button saveAddress;
    private Button close;

    public SettingsView() {
        initNode();
        makeGUI();
    }

    private void makeGUI() {
        //Build top
        BorderPane top = new BorderPane();
        VBox texts = new VBox(5);
        Text appName = new Text(UtilConstants.APP_NAME);
        appName.setFont(new Font(24));

        Text viewName = new Text("Admin Control Panel");
        viewName.setFont(new Font(18));

        texts.setAlignment(Pos.CENTER);
        texts.getChildren().addAll(appName, viewName);

        adminPhoto.setFitHeight(100);
        adminPhoto.setFitWidth(100);
        adminPhoto.setSmooth(true);
        adminPhoto.setImage(new Image(SettingsView.class.getResource("/admin.png").toExternalForm()));
        VBox imageSection = new VBox(5, adminPhoto, updatePhoto);
        imageSection.setAlignment(Pos.CENTER);

        top.setCenter(texts);
        top.setRight(imageSection);

        //Build Center
        //Build left
        BorderPane center = new BorderPane();
        VBox leftSide = new VBox(5);
        ToggleGroup feesToggleGroup = new ToggleGroup();
        feesToggleGroup.getToggles().addAll(phonesFee, gamesFee, computerFee);
        phonesFee.setSelected(true);
        HBox feesSection = new HBox(5, phonesFee, gamesFee, computerFee);
        VBox feesVBox = new VBox(5);
        feeField.setPrefColumnCount(7);
        feesVBox.getChildren().addAll(feesSection, new HBox(5, new Label("₦"), feeField), saveFee);
        HBox feesPart = new HBox(new Label("Non-Refundable Fees: "), feesVBox);
        //make accordion
        GridPane passWordRegion = new GridPane();
        passWordRegion.setHgap(3);
        passWordRegion.setVgap(5);
        passWordRegion.addRow(0, new Label("*New Password:"), newPassword);
        passWordRegion.addRow(1, new Label("*Confirm Password:"), confirmPassword);
        passWordRegion.add(changePassword, 1, 2, 2, 1);
        TitledPane titledPane = new TitledPane("Chnage Password", passWordRegion);
        titledPane.setContent(passWordRegion);
        changeAdminPasswordBlock.getPanes().add(titledPane);
        changeAdminPasswordBlock.setExpandedPane(titledPane);
        leftSide.getChildren().addAll(changeAdminPasswordBlock, feesPart);

        //Build right side
        VBox centerRight = new VBox(10);
        VBox phoneSide = new VBox(5);
        HBox phoneLine = new HBox(5, new Label("Phone(s):"), phoneNumbers);
        phoneLine.setAlignment(Pos.CENTER_RIGHT);
        phoneSide.getChildren().addAll(phoneLine, updatePhone);
        VBox addressSide = new VBox(5);
        address.setPrefColumnCount(20);
        address.setPrefRowCount(3);
        address.setWrapText(true);
        HBox addressLine = new HBox(5, new Label("Address:"), address);
        addressLine.setAlignment(Pos.CENTER_RIGHT);
        addressSide.getChildren().addAll(addressLine, saveAddress);
        centerRight.getChildren().addAll(phoneSide, addressSide);
        phoneSide.setAlignment(Pos.CENTER_RIGHT);
        addressSide.setAlignment(Pos.CENTER_RIGHT);

        center.setLeft(leftSide);
        center.setRight(centerRight);

        this.setTop(top);
        this.setCenter(center);
        close.setCancelButton(true);
        this.setBottom(new HBox(close));
        BorderPane.setMargin(center, new Insets(10));
        BorderPane.setMargin(centerRight, new Insets(30, 10, 10, 10));
        BorderPane.setMargin(leftSide, new Insets(30, 10, 10, 10));
        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
    }

    private void initNode() {
        changeAdminPasswordBlock = new Accordion();
        adminPhoto = new ImageView();
        updatePhoto = new Hyperlink("Update");
        phoneNumbers = new TextField();
        updatePhone = new Button("Update");
        address = new TextArea();
        phonesFee = new RadioButton("Phones");
        gamesFee = new RadioButton("Game Consoles");
        computerFee = new RadioButton("Computers");
        feeField = new TextField();
        newPassword = new PasswordField();
        confirmPassword = new PasswordField();
        changePassword = new Button("Change");
        saveFee = new Button("Save");
        saveAddress = new Button("Save");
        close = new Button("Close");
    }

    public Accordion getChangeAdminPasswordBlock() {
        return changeAdminPasswordBlock;
    }

    public ImageView getAdminPhoto() {
        return adminPhoto;
    }

    public Hyperlink getUpdatePhoto() {
        return updatePhoto;
    }

    public TextField getPhoneNumbers() {
        return phoneNumbers;
    }

    public Button getUpdatePhone() {
        return updatePhone;
    }

    public TextArea getAddress() {
        return address;
    }

    public RadioButton getPhonesFee() {
        return phonesFee;
    }

    public RadioButton getGamesFee() {
        return gamesFee;
    }

    public RadioButton getComputerFee() {
        return computerFee;
    }

    public TextField getFeeField() {
        return feeField;
    }

    public PasswordField getNewPassword() {
        return newPassword;
    }

    public PasswordField getConfirmPassword() {
        return confirmPassword;
    }

    public Button getChangePassword() {
        return changePassword;
    }

    public Button getSaveFee() {
        return saveFee;
    }

    public Button getSaveAddress() {
        return saveAddress;
    }

    public Button getClose() {
        return close;
    }
}
