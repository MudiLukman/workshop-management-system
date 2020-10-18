package com.control.repairsystem.presenter;

import com.control.repairsystem.App;
import com.control.repairsystem.model.Device;
import com.control.repairsystem.util.*;
import com.control.repairsystem.view.DashboardView;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DashboardPresenter {

    private DashboardView view;
    private ContextMenu contextMenu;
    private MenuItem viewDetails;
    private MenuItem addJob;
    private MenuItem completeJob;
    private MenuItem deleteDevice;
    private MenuItem markCollected;

    private String searchTerm;
    private String field;

    private Label advanceLbl;
    private TextField totalCostField;
    private Label balanceLbl;

    public DashboardPresenter(){
        view = new DashboardView();
        contextMenu = new ContextMenu();
        viewDetails = new MenuItem("Preview");
        addJob = new MenuItem("Add New Job");
        completeJob = new MenuItem("Completed?");
        deleteDevice = new MenuItem("Delete");
        markCollected = new MenuItem("Mark As Collected");
        char firstLetter = Administrator.adminName.charAt(0);
        String name = firstLetter + "";
        name = name.toUpperCase();
        getView().getAdminName().setText(name + Administrator.adminName.substring(1));
        try {
            String sql = "SELECT photo FROM admin WHERE username='" + Administrator.adminName.trim() + "'";
            String imageFromDB = "";
            ResultSet rs = DatabaseHelper.executeQuery(sql);
            try {
                while (rs.next()){
                    imageFromDB = rs.getString("photo");
                    imageFromDB = imageFromDB.replace("\\", "\\\\");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
            getView().getAdminPhoto().setImage(new Image(new FileInputStream(imageFromDB)));
            System.gc();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            getView().getAdminPhoto().setImage(new Image(DashboardPresenter.class.getResource("/admin.png").toExternalForm()));
        }
        loadAllTableData();
        addEventHandlers();
        setCellValueFactoryForTableColumns();
        setDefaultsForColumns();

        advanceLbl = new Label("");
        totalCostField = new TextField();
        balanceLbl = new Label("");
    }

    private void setDefaultsForColumns() {
        this.getView().getTableView().getColumns().get(0).setSortable(false);
        this.getView().getTableView().getColumns().get(1).setSortable(true);
        this.getView().getTableView().getColumns().get(2).setSortable(false);
        this.getView().getTableView().getColumns().get(3).setSortable(false);
        this.getView().getTableView().getColumns().get(4).setSortable(false);
        this.getView().getTableView().getColumns().get(5).setSortable(false);
        this.getView().getTableView().getColumns().get(6).setSortable(false);
        this.getView().getTableView().getColumns().get(7).setSortable(false);
        this.getView().getTableView().getColumns().get(8).setSortable(true);
        this.getView().getTableView().getColumns().get(9).setSortable(false);

        //Sort the table based on the date column
        this.getView().getTableView().getSortOrder().add(getView().getTableView().getColumns().get(8));
        this.getView().getTableView().getColumns().get(8).setSortType(TableColumn.SortType.DESCENDING);
    }

    private void setCellValueFactoryForTableColumns() {

        Callback cellValueFactory = new Callback<TableColumn.CellDataFeatures<Device, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Device, String> p){
                int currentDeviceIndex = 0;
                Device currentDevice = p.getValue();
                for(int i = 0; i < getView().getTableView().getItems().size(); i++){
                    if(currentDevice == getView().getTableView().getItems().get(i)){
                        currentDeviceIndex = ++i;
                        break;
                    }
                }

                return new ReadOnlyStringWrapper(String.valueOf(currentDeviceIndex));
            }
        };

        getView().getTableView().getColumns().get(0).setCellValueFactory(cellValueFactory);
        getView().getTableView().getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        getView().getTableView().getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        getView().getTableView().getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("model"));
        getView().getTableView().getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("serialIMEINumber"));
        getView().getTableView().getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        getView().getTableView().getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("phone"));
        getView().getTableView().getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("xCode"));
        getView().getTableView().getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("dateSubmitted"));
        getView().getTableView().getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("status"));
        getView().getTableView().setRowFactory(tv -> {
            TableRow<Device> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Device rowData = row.getItem();
                    showDeviceDetailsDialog(rowData.getxCode());
                }
            });
            return row;
        });

    }

    public static void loadAllTableData() {

        DashboardView.tableView.getItems().clear();
        DashboardView.tableName.setText("All Devices");

        String sql = "SELECT * FROM device";

        ResultSet rs = DatabaseHelper.executeQuery(sql);

        try {
            while(rs.next()) {
                String dbXCode = rs.getString("xcode");
                String dbType = rs.getString("type");
                String dbName = rs.getString("devicename");
                String dbDateSubmitted = rs.getString("datesubmitted");
                String dbCustomerName = rs.getString("customername");
                String dbAddress = rs.getString("customeraddress");
                String dbPhone = rs.getString("phone");
                String dbPhoto = rs.getString("bringerphoto");
                String dbModel = rs.getString("model");
                String dbIMEI = rs.getString("serialnumber");
                String dbOmitted = rs.getString("omittedparts");
                String dbTotoalCost = rs.getString("totalcost");
                String dbFaults = rs.getString("faults");
                String dbAdvance = rs.getString("advancepayed");
                String dbStatus = rs.getString("status");
                String dbCollectionDate = rs.getString("collectiondate");

                String imagePath = dbPhoto.replace("\\", "\\\\");
                Image photo = null;

                try {
                    try(FileInputStream fileInputStream = new FileInputStream(imagePath)){
                        photo = new Image(fileInputStream);
                    } catch (FileNotFoundException e) {
                        System.out.println(e);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

                int bringYear = Integer.parseInt(dbDateSubmitted.substring(0, 4));
                int bringMonth = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.indexOf("-") + 1, dbDateSubmitted.lastIndexOf("-")));
                int bringDay = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.lastIndexOf("-") + 1));

                LocalDate submissionDate = LocalDate.of(bringYear, bringMonth, bringDay);

                DashboardView.tableView.getItems().add(new Device(dbType, dbName, submissionDate, dbCustomerName, dbAddress, dbPhone, photo,
                        dbModel, dbIMEI, dbOmitted, Double.parseDouble(dbTotoalCost), dbFaults,
                        Double.parseDouble(dbAdvance), dbXCode, dbStatus, dbCollectionDate));

            }

            if(DashboardView.tableView.getItems().isEmpty()){
                DashboardView.tableView.setPlaceholder(new Label("No record in database. register devices to start."));
                AlertMaker.showNotification("Alert", "You currently do not have any contnet in database", AlertMaker.warning);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void loadInProgressTableData(){
        DashboardView.tableView.getItems().clear();
        DashboardView.tableName.setText("In Progress");

        String sql = "SELECT * FROM device WHERE status='In Progress'";

        ResultSet rs = DatabaseHelper.executeQuery(sql);

        try {
            while(rs.next()) {
                String dbXCode = rs.getString("xcode");
                String dbType = rs.getString("type");
                String dbName = rs.getString("devicename");
                String dbDateSubmitted = rs.getString("datesubmitted");
                String dbCustomerName = rs.getString("customername");
                String dbAddress = rs.getString("customeraddress");
                String dbPhone = rs.getString("phone");
                String dbPhoto = rs.getString("bringerphoto");
                String dbModel = rs.getString("model");
                String dbIMEI = rs.getString("serialnumber");
                String dbOmitted = rs.getString("omittedparts");
                String dbTotoalCost = rs.getString("totalcost");
                String dbFaults = rs.getString("faults");
                String dbAdvance = rs.getString("advancepayed");
                String dbStatus = rs.getString("status");
                String dbCollectionDate = rs.getString("collectiondate");

                String imagePath = dbPhoto.replace("\\", "\\\\");
                Image photo = null;

                try {
                    try(FileInputStream fileInputStream = new FileInputStream(imagePath)){
                        photo = new Image(fileInputStream);
                    } catch (FileNotFoundException e) {
                        System.out.println(e);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

                int bringYear = Integer.parseInt(dbDateSubmitted.substring(0, 4));
                int bringMonth = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.indexOf("-") + 1, dbDateSubmitted.lastIndexOf("-")));
                int bringDay = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.lastIndexOf("-") + 1));

                LocalDate submissionDate = LocalDate.of(bringYear, bringMonth, bringDay);

                DashboardView.tableView.getItems().add(new Device(dbType, dbName, submissionDate, dbCustomerName, dbAddress, dbPhone, photo,
                        dbModel, dbIMEI, dbOmitted, Double.parseDouble(dbTotoalCost), dbFaults,
                        Double.parseDouble(dbAdvance), dbXCode, dbStatus, dbCollectionDate));

            }

            if(DashboardView.tableView.getItems().isEmpty()){
                DashboardView.tableView.setPlaceholder(new Label("No Jobs for now"));
                AlertMaker.showNotification("Alert", "You currently do not have any devices to repair", AlertMaker.warning);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void loadCompletedTableData(){
        DashboardView.tableView.getItems().clear();
        DashboardView.tableName.setText("Completed Jobs");

        String sql = "SELECT * FROM device WHERE status='Completed'";

        ResultSet rs = DatabaseHelper.executeQuery(sql);

        try {
            while(rs.next()) {
                String dbXCode = rs.getString("xcode");
                String dbType = rs.getString("type");
                String dbName = rs.getString("devicename");
                String dbDateSubmitted = rs.getString("datesubmitted");
                String dbCustomerName = rs.getString("customername");
                String dbAddress = rs.getString("customeraddress");
                String dbPhone = rs.getString("phone");
                String dbPhoto = rs.getString("bringerphoto");
                String dbModel = rs.getString("model");
                String dbIMEI = rs.getString("serialnumber");
                String dbOmitted = rs.getString("omittedparts");
                String dbTotoalCost = rs.getString("totalcost");
                String dbFaults = rs.getString("faults");
                String dbAdvance = rs.getString("advancepayed");
                String dbStatus = rs.getString("status");
                String dbCollectionDate = rs.getString("collectiondate");

                String imagePath = dbPhoto.replace("\\", "\\\\");
                Image photo = null;

                try {
                    try(FileInputStream fileInputStream = new FileInputStream(imagePath)){
                        photo = new Image(fileInputStream);
                    } catch (FileNotFoundException e) {
                        System.out.println(e);
                        photo = new Image(DashboardPresenter.class.getResource("/admin.png").toExternalForm());
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

                int bringYear = Integer.parseInt(dbDateSubmitted.substring(0, 4));
                int bringMonth = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.indexOf("-") + 1, dbDateSubmitted.lastIndexOf("-")));
                int bringDay = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.lastIndexOf("-") + 1));

                LocalDate submissionDate = LocalDate.of(bringYear, bringMonth, bringDay);

                DashboardView.tableView.getItems().add(new Device(dbType, dbName, submissionDate, dbCustomerName, dbAddress, dbPhone, photo,
                        dbModel, dbIMEI, dbOmitted, Double.parseDouble(dbTotoalCost), dbFaults,
                        Double.parseDouble(dbAdvance), dbXCode, dbStatus, dbCollectionDate));

            }

            if(DashboardView.tableView.getItems().isEmpty()){
                DashboardView.tableView.setPlaceholder(new Label("No completed jobs yet"));
                AlertMaker.showNotification("Alert", "You currently do not have any completed jobs", AlertMaker.warning);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void showDeviceDetailsDialog(String xCode) {

        NewDeviceDetail.device.setCustomerName(getView().getTableView().getSelectionModel().getSelectedItem().getCustomerName());
        NewDeviceDetail.device.setCustomerAddress(getView().getTableView().getSelectionModel().getSelectedItem().getCustomerAddress());
        NewDeviceDetail.device.setModel(getView().getTableView().getSelectionModel().getSelectedItem().getModel());
        NewDeviceDetail.device.setSerialIMEINumber(getView().getTableView().getSelectionModel().getSelectedItem().getSerialIMEINumber());
        NewDeviceDetail.device.setOmitted(getView().getTableView().getSelectionModel().getSelectedItem().getOmitted());
        NewDeviceDetail.device.setDateSubmitted(getView().getTableView().getSelectionModel().getSelectedItem().getDateSubmitted());
        NewDeviceDetail.device.setCollectionDate(getView().getTableView().getSelectionModel().getSelectedItem().getCollectionDate());
        NewDeviceDetail.device.setStatus(getView().getTableView().getSelectionModel().getSelectedItem().getStatus());
        NewDeviceDetail.device.setTotalCost(getView().getTableView().getSelectionModel().getSelectedItem().getTotalCost());
        NewDeviceDetail.device.setAdvancePayment(getView().getTableView().getSelectionModel().getSelectedItem().getAdvancePayment());
        NewDeviceDetail.device.setFaults(getView().getTableView().getSelectionModel().getSelectedItem().getFaults());
        NewDeviceDetail.device.setxCode(getView().getTableView().getSelectionModel().getSelectedItem().getxCode());
        NewDeviceDetail.device.setBringerPhoto(getView().getTableView().getSelectionModel().getSelectedItem().getBringerPhoto());

        App.loadWindow("Preview", Modality.NONE, new JobOrderFormPresenter().getView());
    }

    private void addEventHandlers() {
        view.getAllDevices().setOnAction(this::onAllDevicesClicked);
        view.getInProgressJob().setOnAction(this::onInProgressClicked);
        view.getCompletedJob().setOnAction(this::onCollectedClicked);
        getView().getCollectedJob().setOnAction(this::onCollectedJobClicked);
        view.getSearch().setOnAction(this::onSearchClicked);
        getView().getGo().setOnAction(this::onGoClicked);
        getView().getSearchField().setOnAction(this::onGoClicked);
        view.getRegisterJob().setOnAction(this::onRegisterClicked);
        view.getSettings().setOnAction(this::onSettingsClicked);
        view.getLogout().setOnAction(this::onLogoutClicked);
        viewDetails.setOnAction(this::onOpenDetailsClicked);
        addJob.setOnAction(this::onAddJobClicked);
        completeJob.setOnAction(this::onCompletedClicked);
        deleteDevice.setOnAction(this::onDeleteDeviceClicked);
        markCollected.setOnAction(this::onCollectedMarked);

        //Set event handlers for MenuBar Menu Items click
        //Add event handler for file Menu Items click
        getView().getMenuBar().getMenus().get(0).getItems().get(0).setOnAction(this::onRegisterClicked);
        getView().getMenuBar().getMenus().get(0).getItems().get(1).setOnAction(this::onLogoutClicked);
        getView().getMenuBar().getMenus().get(0).getItems().get(2).setOnAction(event -> {
            System.exit(0);
        });

        //Add event handler for View Menu Item click
        getView().getMenuBar().getMenus().get(1).getItems().get(0).setOnAction(this::onToggleToolbarClicked);

        //Add event handler for About Menu Item click
        getView().getMenuBar().getMenus().get(2).getItems().get(0).setOnAction(this::onAboutClicked);

        //Set event handlers for ToolBar Items Click
        getView().getToolBar().getItems().get(0).addEventHandler(ActionEvent.ACTION, this::onRegisterClicked);
        getView().getToolBar().getItems().get(1).addEventHandler(ActionEvent.ACTION, this::onSearchClicked);
        getView().getToolBar().getItems().get(2).addEventHandler(ActionEvent.ACTION, this::onLogoutClicked);
        getView().getToolBar().getItems().get(3).addEventHandler(ActionEvent.ACTION, this::onSettingsClicked);
        getView().getToolBar().getItems().get(4).addEventHandler(ActionEvent.ACTION, this::onHelpClicked);

        setContextMenuForTableRow();
    }

    private void onCollectedMarked(ActionEvent event) {
        Device device = getView().getTableView().getSelectionModel().getSelectedItem();

        String sql = "UPDATE device SET status='Collected' WHERE xcode='" + device.getxCode() + "'";

        if(DatabaseHelper.insert_record(sql) != 0){
            sql = "UPDATE device SET collectiondate='" + LocalDate.now() + "' WHERE xcode='" + device.getxCode() + "'";
            DatabaseHelper.insert_record(sql);
            AlertMaker.showNotification("Done", "Record has been updated successfully", AlertMaker.checked);
            if(!DashboardView.tableName.equals("All Devices")){
                getView().getTableView().getItems().remove(device);
            }
            device.setStatus("Collected");
            device.setCollectionDate(LocalDate.now().toString());
            NewDeviceDetail.device.setCollectionDate(device.getCollectionDate());

        }
        else {
            AlertMaker.showNotification("Error", "Unable to update record", AlertMaker.warning);
        }

    }

    private void setContextMenuForTableRow() {
        ImageView openIcon = new ImageView(new Image(DashboardPresenter.class.getResource("/open.png").toExternalForm()));
        openIcon.setFitWidth(20);
        openIcon.setFitHeight(20);
        viewDetails.setGraphic(openIcon);

        ImageView addJobIcon = new ImageView(new Image(DashboardPresenter.class.getResource("/add.png").toExternalForm()));
        addJobIcon.setFitWidth(20);
        addJobIcon.setFitHeight(20);
        addJob.setGraphic(addJobIcon);

        ImageView deleteIcon = new ImageView(new Image(DashboardPresenter.class.getResource("/delete.png").toExternalForm()));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        deleteDevice.setGraphic(deleteIcon);

        ImageView markAsCollected = new ImageView(new Image(DashboardPresenter.class.getResource("/mark.png").toExternalForm()));
        markAsCollected.setFitHeight(20);
        markAsCollected.setFitWidth(20);
        markCollected.setGraphic(markAsCollected);

        ImageView completedTaskIcon = new ImageView(new Image(DashboardPresenter.class.getResource("/completed.png").toExternalForm()));
        completedTaskIcon.setFitWidth(20);
        completedTaskIcon.setFitHeight(20);
        completeJob.setGraphic(completedTaskIcon);

        contextMenu.getItems().addAll(viewDetails, addJob, completeJob, new SeparatorMenuItem(), deleteDevice);
        getView().getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTableRowRightClicked);
    }

    private void onAllDevicesClicked(ActionEvent event){
        loadAllTableData();
    }

    private void onInProgressClicked(ActionEvent event){
        loadInProgressTableData();
    }

    private void onCollectedClicked(ActionEvent event){
        loadCompletedTableData();
    }

    private void onCollectedJobClicked(ActionEvent event){
        loadCollectedJobTableData();
    }

    private void loadCollectedJobTableData() {
        DashboardView.tableView.getItems().clear();
        DashboardView.tableName.setText("Collected Jobs");

        String sql = "SELECT * FROM device WHERE status='Collected'";

        ResultSet rs = DatabaseHelper.executeQuery(sql);

        try {
            while(rs.next()) {
                String dbXCode = rs.getString("xcode");
                String dbType = rs.getString("type");
                String dbName = rs.getString("devicename");
                String dbDateSubmitted = rs.getString("datesubmitted");
                String dbCustomerName = rs.getString("customername");
                String dbAddress = rs.getString("customeraddress");
                String dbPhone = rs.getString("phone");
                String dbPhoto = rs.getString("bringerphoto");
                String dbModel = rs.getString("model");
                String dbIMEI = rs.getString("serialnumber");
                String dbOmitted = rs.getString("omittedparts");
                String dbTotoalCost = rs.getString("totalcost");
                String dbFaults = rs.getString("faults");
                String dbAdvance = rs.getString("advancepayed");
                String dbStatus = rs.getString("status");
                String dbCollectionDate = rs.getString("collectiondate");

                String imagePath = dbPhoto.replace("\\", "\\\\");
                Image photo = null;

                try {
                    try(FileInputStream fileInputStream = new FileInputStream(imagePath)){
                        photo = new Image(fileInputStream);
                    } catch (FileNotFoundException e) {
                        System.out.println(e);
                        photo = new Image(DashboardPresenter.class.getResource("/admin.png").toExternalForm());
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

                int bringYear = Integer.parseInt(dbDateSubmitted.substring(0, 4));
                int bringMonth = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.indexOf("-") + 1, dbDateSubmitted.lastIndexOf("-")));
                int bringDay = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.lastIndexOf("-") + 1));

                LocalDate submissionDate = LocalDate.of(bringYear, bringMonth, bringDay);

                DashboardView.tableView.getItems().add(new Device(dbType, dbName, submissionDate, dbCustomerName, dbAddress, dbPhone, photo,
                        dbModel, dbIMEI, dbOmitted, Double.parseDouble(dbTotoalCost), dbFaults,
                        Double.parseDouble(dbAdvance), dbXCode, dbStatus, dbCollectionDate));

            }

            if(DashboardView.tableView.getItems().isEmpty()){
                DashboardView.tableView.setPlaceholder(new Label("No collected jobs yet"));
                AlertMaker.showNotification("Alert", "You currently do not have any collected jobs", AlertMaker.warning);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void onSearchClicked(ActionEvent event){
        Stage searchStage = new Stage(StageStyle.DECORATED);
        HBox searchPane = new HBox(10);
        searchPane.getChildren().addAll(new Label("Field:"), getView().getSearchBase(),
                new Label("Term:"), getView().getSearchField(), getView().getGo());
        searchPane.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        searchStage.setScene(new Scene(searchPane));
        searchStage.sizeToScene();
        searchStage.initModality(Modality.APPLICATION_MODAL);
        searchStage.setTitle("Search");
        searchStage.setResizable(false);
        searchStage.showAndWait();
    }

    private void onRegisterClicked(ActionEvent event){
        App.loadWindow("Register New Device", Modality.APPLICATION_MODAL, new RegisterDevicePresenter().getView());
    }

    private void onSettingsClicked(ActionEvent event){
        App.loadWindow("Settings", Modality.APPLICATION_MODAL, new SettingsPresenter().getView());
    }

    private void onLogoutClicked(ActionEvent event){
        App.switchWindow(this.getView(), new LoginPresenter().getView(), "Login");

    }

    private void onOpenDetailsClicked(ActionEvent event){
        showDeviceDetailsDialog(getView().getTableView().getSelectionModel().getSelectedItem().getxCode());
    }

    private void onAddJobClicked(ActionEvent event){
        RegisterDevicePresenter.existingDevice = getView().getTableView().getSelectionModel().getSelectedItem();;
        App.loadWindow("Register New Device", Modality.APPLICATION_MODAL, new RegisterDevicePresenter().getView());
    }

    private void onCompletedClicked(ActionEvent event) {
        Stage stage = new Stage(StageStyle.DECORATED);
        BorderPane layout = new BorderPane();
        Button proceed = new Button("Proceed");

        proceed.setOnAction(e -> {
            ObservableList<Device> devices = getView().getTableView().getItems();
            for(Device device : devices){
                if(device.getxCode().equals(getView().getTableView().getSelectionModel().getSelectedItem().getxCode())){

                    String formattedPhoneNumber = formatNumber(device.getPhone());

                    String sql = "UPDATE device SET status='Completed' WHERE xcode='" + device.getxCode() + "'";
                    if(DatabaseHelper.insert_record(sql) != 0){
                        sql = "UPDATE device SET totalcost='" + totalCostField.getText() + "' WHERE xcode='" + device.getxCode() + "'";
                        DatabaseHelper.insert_record(sql);
                        Twilio.init(UtilConstants.ACCOUNT_SID, UtilConstants.AUTH_TOKEN);
                        //Replace +234 803 810 4910 with formattedPhoneNumber after the upgrade
                        try{
                            //Message message = Message.creator(new PhoneNumber("+234 901 5466 709"),
                            //        new PhoneNumber(UtilConstants.DEDICATED_NUMBER), "Hello there, Your " +
                            //                device.getModel() + " is ready for collection. Do remember to come along with the" +
                            //                " X-Code you received earlier. Total cost of repair :₦" + totalCostField.getText() +
                            //                ", Advance Paid: ₦" + advanceLbl.getText() + ", Balance to be paid: ₦" +
                            //                balanceLbl.getText()).create();
                            AlertMaker.showNotification("Done", "SMS has been sent successfully", AlertMaker.checked);
                            DashboardView.tableView.getItems().clear();
                            DashboardPresenter.loadAllTableData();

                            NewDeviceDetail.device.setxCode(device.getxCode());
                            NewDeviceDetail.device.setCustomerName(device.getCustomerName());
                            NewDeviceDetail.device.setCustomerAddress(device.getCustomerAddress());
                            NewDeviceDetail.device.setType(device.getType());
                            NewDeviceDetail.device.setModel(device.getModel());
                            NewDeviceDetail.device.setTotalCost(device.getTotalCost());
                            NewDeviceDetail.device.setStatus("Completed");
                            NewDeviceDetail.device.setSerialIMEINumber(device.getSerialIMEINumber());
                            NewDeviceDetail.device.setDateSubmitted(device.getDateSubmitted());
                            NewDeviceDetail.device.setFaults(device.getFaults());
                            NewDeviceDetail.device.setBringerPhoto(device.getBringerPhoto());

                            App.switchWindow(layout, new JobOrderFormPresenter().getView(), "Completed");
                            break;
                        }catch (ApiException e1){
                            AlertMaker.showNotification("Error", "Unable to send SMS. Check your internet connection", AlertMaker.crossed);
                        }
                    }
                }
            }

            getView().getTableView().setItems(devices);
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e2 -> {
            ((Stage) cancel.getScene().getWindow()).close();
        });
        VBox top = new VBox(3);

        advanceLbl.setText(getView().getTableView().getSelectionModel().getSelectedItem().getAdvancePayment() + "");
        totalCostField.textProperty().addListener(this::handleTotalCostChanged);
        top.getChildren().add(new Label("Confirm Action"));
        top.getChildren().add(new Label("Indicate that you have completed this job"));
        top.getChildren().add(new HBox(10, new Label("Advance Paid ₦:"), advanceLbl));
        top.getChildren().add(new HBox(10, new Label("Total Cost ₦:"), totalCostField));
        top.getChildren().add(new HBox(10, new Label("Balance ₦:"), balanceLbl));
        top.getChildren().add(new Label("This will send an invitation to the device owner via SMS"));
        top.setPadding(new Insets(7));
        layout.setTop(top);
        HBox bottom = new HBox(10);
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(proceed, cancel);
        proceed.setDefaultButton(true);
        cancel.setCancelButton(true);
        layout.setBottom(bottom);
        layout.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        stage.setTitle("Confirm Action");
        stage.initOwner(getView().getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(layout));
        stage.showAndWait();
    }

    private void handleTotalCostChanged(ObservableValue<? extends String> prop, String oldValue, String newValue){

        try {
            if(Integer.parseInt(newValue) < 0){
                totalCostField.setText("");
                balanceLbl.setText("0.0");
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
            totalCostField.setText("");
            System.out.println(e);
            return;
        }

        int advancePay = (int) getView().getTableView().getSelectionModel().getSelectedItem().getAdvancePayment();

        int newBalance = newVal - advancePay;

        balanceLbl.setText(newBalance + "");
    }

    private String formatNumber(String phone) {
        phone = phone.substring(1);
        phone = "+234" + phone;
        phone = phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7, 11) + " " + phone.substring(11);
        return phone;
    }

    private void onDeleteDeviceClicked(ActionEvent event){
        showConfirmDeleteDialog();
    }

    private void onToggleToolbarClicked(ActionEvent event){
        if(((CheckMenuItem) getView().getMenuBar().getMenus().get(1).getItems().get(0)).isSelected()){
            getView().getToolBar().setVisible(true);
        }else {
            getView().getToolBar().setVisible(false);
        }
    }

    private void onAboutClicked(ActionEvent event){
        App.loadWindow("About", Modality.NONE, new AboutPresenter().getView());
    }

    private void onHelpClicked(ActionEvent event){
        Stage stage = new Stage(StageStyle.DECORATED);
        ScrollPane pane = new ScrollPane();
        pane.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        String text = "Repair Management System (RMS) is a software application licenced under the Apache open source" +
                " software. Built on top of java with java's RIA framework 'JavaFx' with additional libraries from Twilio," +
                " JFeonix, JFXControls, and Control Tech. The software is intended for use by businesses who specialize in the" +
                " repair, servicing, and general contractors of Mobile phones, Game Consoles, and Computing devices. It runs on the JVM" +
                " by providing a built in Java Runtime and JavaFx Runtime, it uses a mysql database from a local server to store, update, retreive," +
                " and destroy its data. RMS solves the problem of false claims and accusations that may come from the repairer" +
                " and/or the customer by offering a non-repudiation management system, It collects user details(Passport photograph," +
                " Name, Address, Device type, Model, Serial/IMEI number, phone number, cost(s), omitted parts in device, and fault description)." +
                " It provides an easy to use user interface to support all level of technical abilities.";

        Text helpText = new Text(text);
        helpText.setWrappingWidth(500);
        helpText.setFont(new Font(12));
        helpText.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        VBox vBox = new VBox(helpText);
        vBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        vBox.setAlignment(Pos.CENTER);
        pane.setContent(vBox);
        stage.setScene(new Scene(pane));
        stage.setResizable(false);
        stage.setHeight(300);
        stage.setWidth(600);
        stage.setTitle("Help");
        stage.initOwner(getView().getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void onGoClicked(ActionEvent event){
        if(getView().getSearchField().getText() == null || getView().getSearchField().getText().equals("")){
            AlertMaker.showNotification("Error", "Empty input value", AlertMaker.crossed);
            return;
        }
        if(getView().getSearchBase().getSelectionModel().getSelectedItem() == null ||
                getView().getSearchBase().getSelectionModel().getSelectedItem().equals("")){
            AlertMaker.showNotification("Error", "Select Field to search", AlertMaker.crossed);
            return;
        }

        searchTerm = getView().getSearchField().getText().trim();
        String fieldChosen = getView().getSearchBase().getSelectionModel().getSelectedItem();
        if(fieldChosen.equals("Customer Name")){
            field = "customername";
        }
        else if(fieldChosen.equals("X-Code")){
            field = "xcode";
        }
        else if(fieldChosen.equals("Serial No")){
            field = "serialnumber";
        }
        else {
            field = "";
        }
        ((Stage) getView().getGo().getScene().getWindow()).close();
        updateTable();
        getView().getSearchField().setText("");
        getView().getSearchBase().getSelectionModel().clearSelection();
    }

    private void updateTable() {

        DashboardView.tableView.getItems().clear();

        String sql = "SELECT * FROM device WHERE " + field + "='" + searchTerm + "'";

        ResultSet rs = DatabaseHelper.executeQuery(sql);

        try {
            while(rs.next()) {
                String dbXCode = rs.getString("xcode");
                String dbType = rs.getString("type");
                String dbName = rs.getString("devicename");
                String dbDateSubmitted = rs.getString("datesubmitted");
                String dbCustomerName = rs.getString("customername");
                String dbAddress = rs.getString("customeraddress");
                String dbPhone = rs.getString("phone");
                String dbPhoto = rs.getString("bringerphoto");
                String dbModel = rs.getString("model");
                String dbIMEI = rs.getString("serialnumber");
                String dbOmitted = rs.getString("omittedparts");
                String dbTotoalCost = rs.getString("totalcost");
                String dbFaults = rs.getString("faults");
                String dbAdvance = rs.getString("advancepayed");
                String dbStatus = rs.getString("status");
                String dbCollectionDate = rs.getString("collectiondate");

                String imagePath = dbPhoto.replace("\\", "\\\\");
                Image photo = null;

                try {
                    try(FileInputStream fileInputStream = new FileInputStream(imagePath)){
                        photo = new Image(fileInputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

                int bringYear = Integer.parseInt(dbDateSubmitted.substring(0, 4));
                int bringMonth = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.indexOf("-") + 1, dbDateSubmitted.lastIndexOf("-")));
                int bringDay = Integer.parseInt(dbDateSubmitted.substring(dbDateSubmitted.lastIndexOf("-") + 1));

                LocalDate submissionDate = LocalDate.of(bringYear, bringMonth, bringDay);

                DashboardView.tableView.getItems().add(new Device(dbType, dbName, submissionDate, dbCustomerName, dbAddress, dbPhone, photo,
                        dbModel, dbIMEI, dbOmitted, Double.parseDouble(dbTotoalCost), dbFaults,
                        Double.parseDouble(dbAdvance), dbXCode, dbStatus, dbCollectionDate));

            }

            if(DashboardView.tableView.getItems().isEmpty()){
                AlertMaker.showNotification("Alert", "No results found for " + searchTerm
                        + " in field '" + field + "'", AlertMaker.warning);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void showConfirmDeleteDialog() {
        Stage stage = new Stage(StageStyle.DECORATED);
        BorderPane layout = new BorderPane();
        Button proceed = new Button("Proceed");
        proceed.setOnAction(event -> {
            ObservableList<Device> devices = getView().getTableView().getItems();
            for(Device device : devices){
                if(device.getxCode().equals(getView().getTableView().getSelectionModel().getSelectedItem().getxCode())){
                    getView().getTableView().getItems().remove(device);
                    String sql = "DELETE FROM device where xcode='" + device.getxCode() + "'";
                    String pathFromDB = "";
                    String queryForPath = "SELECT * FROM device where xcode='" + device.getxCode() + "'";
                    ResultSet resultSet = DatabaseHelper.executeQuery(queryForPath);
                    try {
                        resultSet.next();
                        pathFromDB = resultSet.getString("bringerphoto");
                    } catch (SQLException e) {
                        System.out.println(e);
                    }

                    if(DatabaseHelper.insert_record(sql) > 0){
                        File photoToDelete = new File(pathFromDB.replace("\\", "\\\\"));
                        if(photoToDelete.exists()){
                            photoToDelete.delete();
                            photoToDelete = null;
                        }
                        AlertMaker.showNotification("Complete", "Delete successful", AlertMaker.checked);
                    }
                    else {
                        AlertMaker.showNotification("Error", "Unable to delete device", AlertMaker.checked);
                        return;
                    }

                    break;
                }
            }

            getView().getTableView().setItems(devices);
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> {
            ((Stage) cancel.getScene().getWindow()).close();
        });
        VBox top = new VBox(3);
        top.getChildren().add(new Label("Confirm Delete"));
        top.getChildren().add(new Label("This will Delete record from database"));
        top.setPadding(new Insets(7));
        layout.setTop(top);
        HBox bottom = new HBox(10);
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(proceed, cancel);
        proceed.setDefaultButton(true);
        cancel.setCancelButton(true);
        layout.setBottom(bottom);
        layout.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");
        stage.setTitle("Confirm Delete");
        stage.initOwner(getView().getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(layout));
        stage.showAndWait();
    }

    private void onTableRowRightClicked(MouseEvent event){
        if(event.getButton() == MouseButton.SECONDARY){
            if(getView().getTableView().getSelectionModel().getSelectedItem() != null){
                Device device = getView().getTableView().getSelectionModel().getSelectedItem();
                if(device.getStatus().equals("Completed")){
                    contextMenu.getItems().remove(completeJob);
                    if(!contextMenu.getItems().contains(markCollected)){
                        contextMenu.getItems().add(2, markCollected);
                    }
                }
                else {
                    if(!contextMenu.getItems().contains(completeJob)){
                        contextMenu.getItems().add(2, completeJob);
                    }
                    if(contextMenu.getItems().contains(markCollected)){
                        contextMenu.getItems().remove(markCollected);
                    }
                    if(device.getStatus().equals("Collected")){
                        contextMenu.getItems().remove(completeJob);
                    }
                }

                contextMenu.show(getView().getTableView(), event.getScreenX(), event.getScreenY());
            }
        }
        else {
            contextMenu.hide();
        }
    }

    public DashboardView getView() {
        return view;
    }

}
