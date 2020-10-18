package com.control.repairsystem.presenter;

import com.control.repairsystem.util.DatabaseHelper;
import com.control.repairsystem.util.NewDeviceDetail;
import com.control.repairsystem.view.DashboardView;
import com.control.repairsystem.view.JobOrderFormView;
import javafx.event.ActionEvent;
import javafx.print.PrinterJob;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JobOrderFormPresenter {

    private JobOrderFormView view;

    public JobOrderFormPresenter() {
        view = new JobOrderFormView();
        attachListeners();
        initContent();
    }

    private void initContent() {

        if(NewDeviceDetail.device.getStatus().equals("Completed")){
            getView().getFormName().setText("Job Completion Form");
        }
        if(NewDeviceDetail.device.getStatus().equals("Collected")){
            getView().getFormName().setText("Job Collection Form");
            getView().getTotalCostHBox().setVisible(true);
            getView().getBalanceHBox().setVisible(true);
            getView().getCollectionDateLabel().setVisible(true);
            getView().getCollectionDate().setVisible(true);
            getView().getTotalCostLbl().setText(NewDeviceDetail.device.getTotalCost() + "");
            getView().getBalanceLbl().setText((NewDeviceDetail.device.getTotalCost() - NewDeviceDetail.device.getAdvancePayment()) + "");
            getView().getCollectionDate().setText(NewDeviceDetail.device.getCollectionDate());
        }

        String sqlAddress = "SELECT address FROM admin WHERE username='admin'";
        String address = "";
        ResultSet rsAddrss = DatabaseHelper.executeQuery(sqlAddress);
        try {
            rsAddrss.next();
            address = rsAddrss.getString("address");
        } catch (SQLException e) {
            System.out.println(e);
        }
        getView().getCompanyDescription().setText("Address: " + address);

        String sqlContact = "SELECT phones FROM admin WHERE username='admin'";
        String contact = "";
        ResultSet rsContact = DatabaseHelper.executeQuery(sqlContact);
        try {
            rsContact.next();
            contact = rsContact.getString("phones");
        } catch (SQLException e) {
            System.out.println(e);
        }
        getView().getContactPhone().setText("Contact: " + contact);

        getView().getBringerPhoto().setImage(NewDeviceDetail.device.getBringerPhoto());
        getView().getCustomerName().setText(NewDeviceDetail.device.getCustomerName());
        getView().getCustomerAddress().setText(NewDeviceDetail.device.getCustomerAddress());
        getView().getDeviceModel().setText(NewDeviceDetail.device.getModel());
        getView().getDeviceType().setText(NewDeviceDetail.device.getType());
        getView().getSerialNumber().setText(NewDeviceDetail.device.getSerialIMEINumber());
        getView().getRegisteredOn().setText(NewDeviceDetail.device.getDateSubmitted().toString());
        if(NewDeviceDetail.device.getStatus().equals("Completed")){
            getView().getNonRefundableFee().setVisible(false);
            getView().getNonRefFeeLabel().setVisible(false);
            getView().getFaultsLabel().setText("Resolved: ");
        }
        getView().getFaults().setText(NewDeviceDetail.device.getFaults());

        String sql = "SELECT * FROM admin WHERE username='admin'";
        String columnName = "";

        String type = "";

        if(DashboardView.tableView.getSelectionModel().getSelectedItem() == null){
            type = NewDeviceDetail.device.getType();
        }
        else if(DashboardView.tableView.getSelectionModel().getSelectedItem().getxCode().equals(NewDeviceDetail.device.getxCode())){
            type = DashboardView.tableView.getSelectionModel().getSelectedItem().getType();
        }
        else if(!DashboardView.tableView.getSelectionModel().getSelectedItem().getxCode().equals(NewDeviceDetail.device.getxCode())){
            type = DashboardView.tableView.getSelectionModel().getSelectedItem().getType();
        }
        else {
            type = NewDeviceDetail.device.getType();
        }

        switch (type){
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

        String nonRefFee = "";

        ResultSet rs = DatabaseHelper.executeQuery(sql);
        try {
            rs.next();
            nonRefFee = rs.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getView().getNonRefundableFee().setText(nonRefFee);
        getView().getCollectionDate().setText(NewDeviceDetail.device.getCollectionDate());
        getView().getStatus().setText(NewDeviceDetail.device.getStatus());
        getView().getOmittedParts().setText(NewDeviceDetail.device.getOmitted());
        getView().getAdvancePayed().setText(NewDeviceDetail.device.getAdvancePayment() + "");
        getView().getxCode().setText(NewDeviceDetail.device.getxCode());
    }

    private void attachListeners() {
        getView().getPrint().setOnAction(this::onPrintClicked);
    }

    private void onPrintClicked(ActionEvent event){
        getView().getxCodeLabel().setVisible(false);
        getView().getxCode().setVisible(false);
        getView().getPrint().setVisible(false);

        doPrintJob(getView(), ((Stage) getView().getScene().getWindow()));

        getView().getxCodeLabel().setVisible(true);
        getView().getxCode().setVisible(true);
        getView().getPrint().setVisible(true);
    }

    private void doPrintJob(Pane node, Stage owner) {
        PrinterJob job = PrinterJob.createPrinterJob();

        if(job == null){
            return;
        }

        boolean proceed = job.showPrintDialog(owner);
        if(proceed){
            boolean printed = job.printPage(node);
            if(printed){
                job.endJob();
            }
        }
    }

    public JobOrderFormView getView(){
        return view;
    }
}
