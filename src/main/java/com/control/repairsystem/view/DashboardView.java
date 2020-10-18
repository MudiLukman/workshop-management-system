package com.control.repairsystem.view;

import com.control.repairsystem.model.Device;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.LocalDate;

import static com.control.repairsystem.util.UtilConstants.APP_NAME;

public class DashboardView extends BorderPane{

    //Menu and Toolbar controls
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem newFileMenuItem;
    private MenuItem logoutFileMenuItem;
    private MenuItem exitFileMenuItem;
    private Menu viewMenu;
    private CheckMenuItem showOrHideToolbarMenuItem;
    private Menu aboutMenu;
    private MenuItem aboutMenuItem;
    private ToolBar toolBar;
    private Button addDevice;
    private Button searchDevice;
    private Button exitSystem;
    private Button settingsButton;
    private Button aboutButton;

    public static Label tableName;
    public static ImageView adminPhoto;
    private Label adminName;
    private Button allDevices;
    private Button inProgressJob;
    private Button completedJob;
    private Button collectedJob;
    private Button search;
    private ChoiceBox<String> searchBase;
    private TextField searchField;
    private Button go;
    private Button registerJob;
    private Button settings;
    private Button logout;
    private Pane jobsTableFraction;
    public static TableView<Device> tableView;
    private TableColumn<Device, String> lineNumber;
    private TableColumn<Device, String> deviceType;
    private TableColumn<Device, String> deviceName;
    private TableColumn<Device, String> deviceModel;
    private TableColumn<Device, String> deviceSerialNumber;
    private TableColumn<Device, String> customerName;
    private TableColumn<Device, LocalDate> date;
    private TableColumn<Device, String> status;
    private TableColumn<Device, String> customerPhone;
    private TableColumn<Device, String> smsCode;

    public DashboardView(){
        menuBar = new MenuBar();

        fileMenu = new Menu("_File");
        newFileMenuItem = new MenuItem("_Register New Device");
        logoutFileMenuItem = new MenuItem("_Logout");
        exitFileMenuItem = new MenuItem("_Exit");
        fileMenu.getItems().addAll(newFileMenuItem, logoutFileMenuItem, exitFileMenuItem);

        viewMenu = new Menu("_View");
        showOrHideToolbarMenuItem = new CheckMenuItem("Show Toolbar");
        showOrHideToolbarMenuItem.setSelected(true);
        viewMenu.getItems().add(showOrHideToolbarMenuItem);

        aboutMenu = new Menu("_About");
        aboutMenuItem = new MenuItem("About");
        aboutMenu.getItems().add(aboutMenuItem);

        toolBar = new ToolBar();

        ImageView plusIcon = new ImageView(new Image(DashboardView.class.getResource("/new.png").toExternalForm()));
        plusIcon.setFitHeight(25);
        plusIcon.setFitWidth(25);
        addDevice = new Button("", plusIcon);
        addDevice.setTooltip(new Tooltip("Register New Device"));

        ImageView searchButtonIcon = new ImageView(new Image(DashboardView.class.getResource("/search.png").toExternalForm()));
        searchButtonIcon.setFitWidth(25);
        searchButtonIcon.setFitHeight(25);
        searchDevice = new Button("", searchButtonIcon);
        searchDevice.setTooltip(new Tooltip("Search"));

        ImageView exitButtonIcon = new ImageView(new Image(DashboardView.class.getResource("/exit.png").toExternalForm()));
        exitButtonIcon.setFitHeight(25);
        exitButtonIcon.setFitWidth(25);
        exitSystem = new Button("", exitButtonIcon);
        exitSystem.setTooltip(new Tooltip("Logout"));

        ImageView settingsButtonIcon = new ImageView(new Image(DashboardView.class.getResource("/new_settings.png").toExternalForm()));
        settingsButtonIcon.setFitWidth(25);
        settingsButtonIcon.setFitHeight(25);
        settingsButton = new Button("", settingsButtonIcon);
        settingsButton.setTooltip(new Tooltip("Go to Settings"));

        ImageView aboutButtonIcon = new ImageView(new Image(DashboardView.class.getResource("/info.png").toExternalForm()));
        aboutButtonIcon.setFitHeight(25);
        aboutButtonIcon.setFitWidth(25);
        aboutButton = new Button("", aboutButtonIcon);
        aboutButton.setTooltip(new Tooltip("Learn more"));

        toolBar.getItems().addAll(addDevice, searchDevice, exitSystem, settingsButton, aboutButton);

        tableName = new Label("All Devices");

        adminPhoto = new ImageView(DashboardView.class.getResource("/profile.png").toExternalForm());
        adminPhoto.setFitWidth(100);
        adminPhoto.setFitHeight(100);
        adminPhoto.setSmooth(true);
        adminName = new Label("Control");
        adminName.setFont(new Font(20));
        adminName.setStyle("-fx-font-weight: bold;");
        allDevices = new Button("All Devices");
        inProgressJob = new Button("In Progress");
        completedJob = new Button("Completed");
        collectedJob = new Button("Collected");

        ImageView searchIcon = new ImageView(DashboardView.class.getResource("/search.png").toExternalForm());
        searchIcon.setFitHeight(20);
        searchIcon.setFitWidth(20);
        search = new Button("Search", searchIcon);
        search.setTooltip(new Tooltip("Find a Device"));
        searchBase = new ChoiceBox<>();
        searchBase.getItems().addAll("Customer Name", "X-Code", "Serial No");
        searchField = new TextField();
        searchField.setTooltip(new Tooltip("Enter query here"));
        go = new Button("Go");
        go.setTooltip(new Tooltip("Find results"));
        go.setDefaultButton(true);
        registerJob = new Button("Register New Device");

        ImageView settingsIcon = new ImageView(DashboardView.class.getResource("/settings.png").toExternalForm());
        settingsIcon.setFitHeight(25);
        settingsIcon.setFitWidth(25);
        settings = new Button("Settings", settingsIcon);
        logout = new Button("Logout");
        logout.setCancelButton(true);
        jobsTableFraction = new Pane();
        tableView = new TableView<>();

        lineNumber = new TableColumn<>("S/N");
        deviceType = new TableColumn<>("Device Type");
        deviceName = new TableColumn<>("Device Name");
        deviceModel = new TableColumn<>("Device Model");
        deviceSerialNumber = new TableColumn<>("Serial/IMEI Number");
        customerName = new TableColumn<>("Customer Name");
        date = new TableColumn<>("Date Acquired");
        status = new TableColumn<>("Status");
        customerPhone = new TableColumn<>("Phone");
        smsCode = new TableColumn<>("X-Code");

        setupUI();

    }

    private void setupUI() {
        jobsTableFraction.setStyle("-fx-padding: 3;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");

        this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-color: green;");

        menuBar.getMenus().addAll(fileMenu, viewMenu, aboutMenu);
        VBox top = new VBox(menuBar, toolBar, makeTop());
        this.setTop(top);
        this.setLeft(makeLeft());
        this.setCenter(makeCenter());
        this.setBottom(makeBottom());
    }

    private Node makeCenter() {
        tableView.getColumns().addAll(lineNumber, deviceType, deviceName, deviceModel, deviceSerialNumber, customerName, customerPhone, smsCode,
                date, status);
        return tableView;
    }

    private Node makeLeft() {
        bindSizesOfNodes();
        VBox leftFraction = new VBox(10);
        leftFraction.setAlignment(Pos.CENTER);
        leftFraction.setPadding(new Insets(5));
        VBox adminPhotoAndName = new VBox(2, adminPhoto, adminName);
        adminPhotoAndName.setAlignment(Pos.CENTER);
        leftFraction.getChildren().addAll(adminPhotoAndName, allDevices, inProgressJob,
                completedJob, collectedJob, new Separator(), search, registerJob, settings);

        return leftFraction;
    }

    private Node makeBottom(){
        HBox bottom = new HBox(logout);
        logout.setCancelButton(true);
        bottom.setAlignment(Pos.CENTER_LEFT);
        return  bottom;
    }

    private void bindSizesOfNodes() {
        allDevices.prefWidthProperty().bind(registerJob.widthProperty());
        inProgressJob.prefWidthProperty().bind(registerJob.widthProperty());
        completedJob.prefWidthProperty().bind(registerJob.widthProperty());
        collectedJob.prefWidthProperty().bind(registerJob.widthProperty());
        search.prefWidthProperty().bind(registerJob.widthProperty());
        settings.prefWidthProperty().bind(registerJob.widthProperty());
        logout.prefWidthProperty().bind(registerJob.widthProperty());
    }

    private Node makeTop() {
        Label appName = new Label(APP_NAME);
        appName.setFont(new Font(24));
        Label dashBoardHeader = new Label("Admin Dashboard : ");
        dashBoardHeader.setFont(new Font(20));
        tableName.setFont(new Font(20));
        HBox headerHBox = new HBox(dashBoardHeader, tableName);
        headerHBox.setAlignment(Pos.CENTER);
        VBox top = new VBox(4,appName, headerHBox);
        top.setAlignment(Pos.CENTER);
        return top;
    }

    public ImageView getAdminPhoto() {
        return adminPhoto;
    }

    public Label getAdminName() {
        return adminName;
    }

    public Button getInProgressJob() {
        return inProgressJob;
    }

    public Button getCompletedJob() {
        return completedJob;
    }

    public Button getSearch() {
        return search;
    }

    public Button getRegisterJob() {
        return registerJob;
    }

    public Button getSettings() {
        return settings;
    }

    public Pane getJobsTableFraction() {
        return jobsTableFraction;
    }

    public TableView<Device> getTableView() {
        return tableView;
    }

    public Button getAllDevices() {
        return allDevices;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Menu getFileMenu() {
        return fileMenu;
    }

    public Menu getViewMenu() {
        return viewMenu;
    }

    public Menu getAboutMenu() {
        return aboutMenu;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public Button getAddDevice() {
        return addDevice;
    }

    public Button getSearchDevice() {
        return searchDevice;
    }

    public Button getExitSystem() {
        return exitSystem;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getAboutButton() {
        return aboutButton;
    }

    public MenuItem getNewFileMenuItem() {
        return newFileMenuItem;
    }

    public MenuItem getLogoutFileMenuItem() {
        return logoutFileMenuItem;
    }

    public MenuItem getExitFileMenuItem() {
        return exitFileMenuItem;
    }

    public CheckMenuItem getShowOrHideToolbarMenuItem() {
        return showOrHideToolbarMenuItem;
    }

    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getGo() {
        return go;
    }

    public Button getLogout() {
        return logout;
    }

    public Button getCollectedJob() {
        return collectedJob;
    }

    public ChoiceBox<String> getSearchBase() {
        return searchBase;
    }
}
