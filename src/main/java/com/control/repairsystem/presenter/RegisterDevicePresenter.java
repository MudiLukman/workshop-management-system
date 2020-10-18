package com.control.repairsystem.presenter;

import com.control.repairsystem.App;
import com.control.repairsystem.model.Device;
import com.control.repairsystem.util.AlertMaker;
import com.control.repairsystem.util.DatabaseHelper;
import com.control.repairsystem.util.NewDeviceDetail;
import com.control.repairsystem.view.RegisterDeviceView;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class RegisterDevicePresenter {

    private RegisterDeviceView view;

    public static Device existingDevice;

    private Image photo;
    private String deviceType;
    private String deviceName;
    private String deviceModel;
    private String serialNumber;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String omittedParts;
    private String faults;
    private LocalDate collectionDate;
    private double nonRefundableFee;
    private double advance;

    public RegisterDevicePresenter() {
        view = new RegisterDeviceView();
        attachDragAndDropListenerForImageView();
        addListeners();
        bindProperties();
        configureDatePicker();
        tryLoadingExistingUserDetail();
    }

    private void tryLoadingExistingUserDetail() {
        if(existingDevice == null){
            return;
        }
        getView().getCustomerName().setText(existingDevice.getCustomerName());
        getView().getCustomerAddress().setText(existingDevice.getCustomerAddress());
        getView().getCustomerPhone().setText(existingDevice.getPhone());

        String sql = "SELECT * FROM device WHERE xcode='" + existingDevice.getxCode() + "'";
        ResultSet rs = DatabaseHelper.executeQuery(sql);
        try {
            rs.next();
            String dbPhoto = rs.getString("bringerphoto");
            String imagePath = dbPhoto.replace("\\", "\\\\");
            try {
                try(FileInputStream fileInputStream = new FileInputStream(imagePath)){
                    photo = new Image(fileInputStream);
                    getView().getBringerPhoto().setImage(photo);

                } catch (FileNotFoundException e) {
                    System.out.println(e);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        existingDevice = null;
    }

    private void bindProperties() {
        getView().getDeviceType().valueProperty().addListener(this::handleDeviceTypeChanged);
    }

    private void handleDeviceTypeChanged(ObservableValue<? extends String> prop, String oldValue, String newValue){
        String columnName = "";
        switch (newValue){
            case "Phone":
                columnName = "phonefee";
                break;

            case "Laptop":
                columnName = "laptopfee";
                break;

            case "Game Console":
                columnName = "gamefee";
                break;
        }

        String sql = "SELECT * FROM admin WHERE username='admin'";

        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try {
            resultSet.next();
            getView().getNonRefundableFee().setText(resultSet.getString(columnName));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    private void handleAdvancePaymentChanged(ObservableValue<? extends String> prop, String oldValue, String newValue){
        try{
            if(Integer.parseInt(getView().getAdvancePayment().getText()) < 0){
                getView().getAdvancePayment().setText("");
                AlertMaker.showNotification("Non negative", "Only Positive values allowed", AlertMaker.crossed);
                return;
            }
        }catch (NumberFormatException e){
            System.out.println(e);
        }

        int newVal = 0;

        try {
            newVal = Integer.parseInt(newValue);
        }catch (NumberFormatException e){
            System.out.println(e);
        }

    }

    private void configureDatePicker() {
        Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell(){
                            @Override
                            public void updateItem(LocalDate item, boolean empty){

                                super.updateItem(item, empty);

                                DayOfWeek dayOfWeek = DayOfWeek.from(item);

                                if(item.isBefore(LocalDate.now()) || dayOfWeek == DayOfWeek.SUNDAY){
                                    this.setDisable(true);
                                }

                                if (dayOfWeek == DayOfWeek.SUNDAY){
                                    this.setTextFill(Color.RED);
                                }
                            }
                        };
                    }
                };

        //getView().getCollectionDate().setDayCellFactory(dayCellFactory);
        //the literal value 7 should read from db before production
        //getView().getCollectionDate().setValue(computeCollectionDate(7));
    }

    private LocalDate computeCollectionDate(int numberOfDayToAdd){
        LocalDate today = LocalDate.now();
        today.plusDays(numberOfDayToAdd);
        today = today.plusDays(numberOfDayToAdd);
        DayOfWeek day = DayOfWeek.SUNDAY;
        if(today.getDayOfWeek() == day){
            today = today.plusDays(1);
        }
        return today;
    }

    private void addListeners() {
        getView().getCancel().setOnAction(event -> {
            ((Stage) getView().getScene().getWindow()).close();
        });
        getView().getRegister().setOnAction(this::onRegisterClicked);
        getView().getBringerPhoto().setOnMouseClicked(this::onBringerPhotoClicked);
        getView().getAdvancePayment().textProperty().addListener(this::handleAdvancePaymentChanged);

    }

    private void onRegisterClicked(ActionEvent event){
        if(!inputValidation()){
            AlertMaker.showNotification("Error", "Incomplete Input data", AlertMaker.crossed);
            return;
        }
        NewDeviceDetail.device.setBringerPhoto(photo);
        NewDeviceDetail.device.setModel(deviceModel);
        NewDeviceDetail.device.setType(deviceType);
        NewDeviceDetail.device.setName(deviceName);
        NewDeviceDetail.device.setSerialIMEINumber(serialNumber);
        NewDeviceDetail.device.setCustomerName(customerName);
        NewDeviceDetail.device.setCustomerAddress(customerAddress);
        NewDeviceDetail.device.setPhone(customerPhone);
        NewDeviceDetail.device.setDateSubmitted(LocalDate.now());
        NewDeviceDetail.device.setOmitted(omittedParts);
        NewDeviceDetail.device.setTotalCost(0.0);
        NewDeviceDetail.device.setAdvancePayment(advance);
        NewDeviceDetail.device.setStatus("In Progress");
        NewDeviceDetail.device.setFaults(faults);

        App.loadWindow("Confirm Action", Modality.APPLICATION_MODAL, new ConfirmRegisterPresenter().getView());
        getView().getScene().getWindow().hide();
    }

    private boolean inputValidation() {

        Image retrievedImage = view.getBringerPhoto().getImage();

        if(retrievedImage != null){
            photo = retrievedImage;
        }
        else {
            AlertMaker.showErrorMessage("No Image found", "You must select a valid passport");
            return false;
        }

        deviceType = view.getDeviceType().getValue();
        if(deviceType == null || deviceType.trim().equals("")){
            return false;
        }

        deviceName = getView().getDeviceName().getText();
        if(deviceName == null || deviceName.trim().equals("")){
            return false;
        }

        deviceModel = view.getDeviceModel().getText();
        if(deviceModel == null || deviceModel.trim().equals("")){
            return false;
        }

        serialNumber = view.getSerialNumber().getText();
        if(serialNumber == null || serialNumber.trim().equals("")){
            return false;
        }

        customerName = view.getCustomerName().getText();
        if(customerName == null || customerName.trim().equals("")){
            return false;
        }

        customerPhone = view.getCustomerPhone().getText();
        if(!validatePhoneNumber(customerPhone)){
            AlertMaker.showNotification("Invalid Format", "Not a valid phone number", AlertMaker.warning);
            return false;
        }
        if(customerPhone == null || customerPhone.trim().equals("")){
            return false;
        }

        customerAddress = view.getCustomerAddress().getText();
        if(customerAddress == null || customerAddress.trim().equals("")){
            return false;
        }

        faults = view.getFaults().getText();
        if(faults == null || faults.trim().equals("")){
            return false;
        }

        //collectionDate = view.getCollectionDate().getValue();
        omittedParts = view.getOmittedParts().getText();
        nonRefundableFee = Double.parseDouble(view.getNonRefundableFee().getText());

        try{
            advance = Double.parseDouble(view.getAdvancePayment().getText());
            if(advance < 0.0){
                return false;
            }
        }catch (NumberFormatException e){
            AlertMaker.showErrorMessage(e);
            return false;
        }

        if(view.getFaults().getText() != null || !view.getFaults().getText().trim().equals("")){
            faults = view.getFaults().getText();
        }

        return true;
    }

    private boolean validatePhoneNumber(String phone) {

        if(phone.length() != 11){
            return false;
        }

        char[] digits = phone.toCharArray();
        for(int i = 0; i < digits.length; i++){
            if(!Character.isDigit(digits[i])){
                return false;
            }
        }

        return true;
    }

    private void onBringerPhotoClicked(MouseEvent event){
        FileChooser fileChooserDialog = new FileChooser();
        fileChooserDialog.setInitialDirectory(new File("C:\\"));
        fileChooserDialog.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.jpg", "*.gif"));
        fileChooserDialog.setTitle("Select Picture");
        File file = fileChooserDialog.showOpenDialog(getView().getScene().getWindow());
        if(file == null){
            return;
        }
        try {
            view.getBringerPhoto().setImage(new Image(file.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            AlertMaker.showErrorMessage(e);
        }
    }

    private void attachDragAndDropListenerForImageView() {
        getView().getBringerPhoto().setPickOnBounds(true);
        getView().getBringerPhoto().setOnDragOver(this::onDragOver);
        getView().getBringerPhoto().setOnDragDropped(this::onDragDropped);
    }

    private void onDragOver(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if(dragboard.hasImage() || dragboard.hasFiles()){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void onDragDropped(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        Image image = null;
        try {
            image = new Image(dragboard.getFiles().get(0).toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getView().getBringerPhoto().setImage(image);
        event.setDropCompleted(true);
        event.consume();
    }

    public RegisterDeviceView getView(){
        return view;
    }
}
