package com.control.repairsystem.presenter;

import com.control.repairsystem.App;
import com.control.repairsystem.util.AlertMaker;
import com.control.repairsystem.util.DatabaseHelper;
import com.control.repairsystem.util.NewDeviceDetail;
import com.control.repairsystem.util.UtilConstants;
import com.control.repairsystem.view.ConfirmRegisterView;
import com.control.repairsystem.view.DashboardView;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class ConfirmRegisterPresenter {

    private ConfirmRegisterView view;

    public ConfirmRegisterPresenter() {
        this.view = new ConfirmRegisterView();
        attachListeners();
    }

    private void attachListeners() {
        view.getProceed().setOnAction(this::onProceed);
        view.getCancel().setOnAction(this::onCancel);
    }

    private void onProceed(ActionEvent event){
        generateXCode();
    }

    private boolean generateXCode() {

        LocalDate today = LocalDate.now();
        int year = Integer.parseInt(String.valueOf(today.getYear()).substring(2, 4));
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        Date date = new Date();
        String xCode = "" + year + month + day + date.getHours() + date.getMinutes() + date.getSeconds();
        NewDeviceDetail.device.setxCode(xCode);
        Twilio.init(UtilConstants.ACCOUNT_SID, UtilConstants.AUTH_TOKEN);

        String formattedPhoneNumber = formatNumber(NewDeviceDetail.device.getPhone());

        try {
            if(true)
                throw new ApiException("For Demo purpose only");
            //Replace '+234 901 5466 709' with users no i.e formattedPhoneNumber
            Message message = Message.creator(new PhoneNumber("+234 901 5466 709"),
                    new PhoneNumber(UtilConstants.DEDICATED_NUMBER), "The X-Code for your " + NewDeviceDetail.device.getModel() + " is '"
                            + xCode + "'. please do remember to come with this code on device collection date. You will receive an SMS invitation when your device is ready for collection").create();
            saveDeviceToDB();
            App.switchWindow(getView(), new JobOrderFormPresenter().getView(), "New Record");
            DashboardView.tableView.getItems().clear();
            DashboardPresenter.loadAllTableData();
        }catch (ApiException e){
            //delete the following
            saveDeviceToDB();
            App.switchWindow(getView(), new JobOrderFormPresenter().getView(), "New Record");
            DashboardView.tableView.getItems().clear();
            DashboardPresenter.loadAllTableData();
            //stop here
            //AlertMaker.showNotification("Error", "Unable to send SMS. Check your internet connection", AlertMaker.crossed);
            return false;
        }

        return true;
    }

    private void saveDeviceToDB() {
        String fileName = "C:\\rms_photos\\tasks\\";
        File file = new File(fileName + NewDeviceDetail.device.getxCode() + ".jpg");

        if(!file.exists()){
            file.mkdirs();
        }

        int val = 0;
        String format = "JPG";
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(NewDeviceDetail.device.getBringerPhoto(), null), format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        val = DatabaseHelper.insert_device(file.getAbsolutePath());
        file = null;
    }

    private String formatNumber(String phone) {
        phone = phone.substring(1);
        phone = "+234" + phone;
        phone = phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7, 11) + " " + phone.substring(11);
        return phone;
    }

    private void onCancel(ActionEvent event){
        ((Stage) view.getCancel().getScene().getWindow()).close();
    }

    public ConfirmRegisterView getView(){
        return view;
    }
}
