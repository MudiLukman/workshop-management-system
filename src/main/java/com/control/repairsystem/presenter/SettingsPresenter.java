package com.control.repairsystem.presenter;

import com.control.repairsystem.model.Admin;
import com.control.repairsystem.util.AlertMaker;
import com.control.repairsystem.util.DatabaseHelper;
import com.control.repairsystem.view.DashboardView;
import com.control.repairsystem.view.SettingsView;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SettingsPresenter {

    private SettingsView view;

    private Image adminPhoto;
    private String fee;
    private String phones;
    private String address;
    private String colName;

    public SettingsPresenter() {
        view = new SettingsView();
        addEventListeners();
        setDefaultsFromDB();
    }

    private void setDefaultsFromDB() {

        if(getView().getPhonesFee().getToggleGroup().getToggles().get(0).isSelected()){
            colName = "phonefee";
        }
        else if(getView().getPhonesFee().getToggleGroup().getToggles().get(1).isSelected()){
            colName = "gamefee";
        }
        else{
            colName = "laptopfee";
        }

        loadDBContents();

        getView().getAdminPhoto().setImage(adminPhoto);
        getView().getFeeField().setText(fee);
        getView().getPhoneNumbers().setText(phones);
        getView().getAddress().setText(address);

    }

    private void loadDBContents() {

        String sql = "SELECT * FROM admin WHERE username='admin'";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try{
            resultSet.next();
            String imagePath = resultSet.getString("photo");
            File file = new File(imagePath.replace("\\", "\\\\"));
            try(FileInputStream fileInputStream = new FileInputStream(file)){
                adminPhoto = new Image(fileInputStream);
            }
            fee = resultSet.getString(colName);
            phones = resultSet.getString("phones");
            address = resultSet.getString("address");
        }catch(SQLException | IOException e){
            System.out.println(e);
        }

    }

    private void addEventListeners() {
        getView().getClose().setOnAction(event -> getView().getScene().getWindow().hide());
        getView().getUpdatePhoto().setOnAction(this::updatePhoto);
        getView().getSaveAddress().setOnAction(this::updateAddress);
        getView().getUpdatePhone().setOnAction(this::updatePhone);
        getView().getChangePassword().setOnAction(this::onUpdatePasswordClicked);
        getView().getSaveFee().setOnAction(this::saveFeesClicked);
        getView().getPhonesFee().getToggleGroup().selectedToggleProperty().addListener(this::onToggleChanged);
    }

    private void onToggleChanged(ObservableValue<? extends Toggle> prop, Toggle oldValue, Toggle newValue){
        String sql = "SELECT ";
        String col = "";

        if(newValue == getView().getPhonesFee().getToggleGroup().getToggles().get(0)){
            sql += "phonefee ";
            col = "phonefee";
        }
        else if(newValue == getView().getPhonesFee().getToggleGroup().getToggles().get(1)){
            sql += "gamefee ";
            col = "gamefee";
        }
        else{
            sql += "laptopfee ";
            col = "laptopfee";
        }

        sql += "FROM admin WHERE username='admin'";

        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try{
            resultSet.next();
            getView().getFeeField().setText(resultSet.getString(col));
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    private void saveFeesClicked(ActionEvent event){
        String sql = "UPDATE admin set ";
        if(getView().getPhonesFee().getToggleGroup().getToggles().get(0).isSelected()){
            sql += "phonefee='" + getView().getFeeField().getText().trim() + "' WHERE username='admin'";
        }
        else if(getView().getPhonesFee().getToggleGroup().getToggles().get(1).isSelected()){
            sql += "gamefee='" + getView().getFeeField().getText().trim() + "' WHERE username='admin'";
        }
        else {
            sql += "laptopfee='" + getView().getFeeField().getText().trim() + "' WHERE username='admin'";
        }

        if(DatabaseHelper.insert_record(sql) != 0){
            AlertMaker.showNotification("Done", "Fee Updated", AlertMaker.checked);
        }
    }

    private void onUpdatePasswordClicked(ActionEvent event){
        String newPass = getView().getNewPassword().getText();
        String confirmPass = getView().getConfirmPassword().getText();

        if((newPass != null && confirmPass != null) && (!newPass.equals("") && !confirmPass.equals(""))){

            if(!newPass.equals(confirmPass)){
                AlertMaker.showNotification("Error", "Passwords must be the same", AlertMaker.warning);
                return;
            }

            String hashedPasswordToSave = "";
            try {
                hashedPasswordToSave = Admin.Sha1(newPass);
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
            }

            String sql = "UPDATE admin set password='" + hashedPasswordToSave + "' WHERE username='admin'";
            if(DatabaseHelper.insert_record(sql) != 0){
                AlertMaker.showNotification("Done", "Password Changed", AlertMaker.checked);
            }
        }
    }

    private void updatePhone(ActionEvent event){
        String newPhone = getView().getPhoneNumbers().getText();
        String sql = "UPDATE admin set phones='" + newPhone + "' WHERE username='admin'";
        if(DatabaseHelper.insert_record(sql) != 0){
            AlertMaker.showNotification("Done", "Phones updated", AlertMaker.checked);
        }
    }

    private void updateAddress(ActionEvent event){
        String newAddress = getView().getAddress().getText();
        String sql = "UPDATE admin set address='" + newAddress + "' WHERE username='admin'";
        if(DatabaseHelper.insert_record(sql) != 0){
            AlertMaker.showNotification("Done", "Address updated", AlertMaker.checked);
        }
    }

    private void updatePhoto(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.JPG", ".jpg", ".PNG", ".png"));
        fileChooser.setTitle("Choose new Photo");
        File fileChoosen = fileChooser.showOpenDialog(getView().getScene().getWindow());
        if(fileChoosen == null){
            return;
        }

        try{
            //Save new photo to drive
            String format = "JPG";
            String fileName = "C:\\rms_photos\\admins\\";
            File fileToSaveTo = new File(fileName + "admin.jpg");
            if(!fileToSaveTo.exists()){
                fileToSaveTo.mkdirs();
            }
            adminPhoto = new Image(new FileInputStream(fileChoosen));
            ImageIO.write(SwingFXUtils.fromFXImage(adminPhoto, null), format, fileToSaveTo);

            //update link to new photo in DB
            String sqlPut = "UPDATE admin set photo=?" + " WHERE username='admin'";

            if(DatabaseHelper.insert_record_using_preparedStmt(sqlPut, fileToSaveTo.getAbsolutePath()) != 0){
                System.out.println("Db link to photo updated successfully");
            }

            AlertMaker.showNotification("Success", "Profile Photo Updated", AlertMaker.checked);

            //set imageview to new image
            String sql = "SELECT photo FROM admin WHERE username='admin'";
            String imageFromDB = "";
            ResultSet rs = DatabaseHelper.executeQuery(sql);
            rs.next();
            imageFromDB = rs.getString("photo");
            imageFromDB = imageFromDB.replace("\\", "\\\\");
            getView().getAdminPhoto().setImage(new Image(new FileInputStream(imageFromDB)));

            //Update dashboard photo too
            DashboardView.adminPhoto.setImage(getView().getAdminPhoto().getImage());
            System.gc();

        }catch(IOException | SQLException e){
            System.out.println(e);
        }
    }

    public SettingsView getView(){
        return view;
    }
}
