package com.control.repairsystem.util;

import com.control.repairsystem.model.Admin;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DatabaseHelper {

    private static final String Jdbc_driver = "com.mysql.cj.jdbc.Driver";
    private static final String Connection_string = "jdbc:mysql://localhost/rmsdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String device_table = "CREATE TABLE if not exists device(" +
            "xcode VARCHAR(20) PRIMARY KEY NOT NULL, type VARCHAR(20), devicename VARCHAR(20), datesubmitted VARCHAR(20), customername TEXT, " +
            "customeraddress TEXT, phone VARCHAR(20), bringerphoto TEXT, model TEXT, serialnumber TEXT, " +
            "omittedparts TEXT, totalcost VARCHAR(20), faults TEXT, advancepayed VARCHAR(20), " +
            "status VARCHAR(20), collectiondate VARCHAR(20))";

    public static final String admin_table = "CREATE TABLE if not exists admin(username VARCHAR(20) PRIMARY KEY NOT NULL," +
            "password TEXT NOT NULL,phonefee TEXT,gamefee TEXT,laptopfee TEXT,phones TEXT," +
            "address TEXT,photo TEXT)";
    static Connection con = null;
    static Statement stmt = null;

    public static void connect(){
        try {
            Class.forName(Jdbc_driver);
            System.out.println("Driver Loaded");
            con = DriverManager.getConnection(Connection_string, "control", "segfault");
            System.out.println("Connection established");
            stmt = con.createStatement();

        }catch (ClassNotFoundException | SQLException e){
            Platform.runLater(() -> {
                AlertMaker.showNotification("Database Error", "Unable to establish connection to database", AlertMaker.crossed);
            });
        }
    }

    public static void disconnect(){
        if(con != null){
            try {

                stmt.close();
                con.close();

            }catch (SQLException e){
                Platform.runLater(() -> {
                    AlertMaker.showNotification("Database Error", "Could not disconnect", AlertMaker.warning);
                });
            }
        }
    }

    public static void create_table(String table_name){
        try{

            stmt.executeUpdate(table_name);

        }catch (SQLException e){
            Platform.runLater(() -> {
                AlertMaker.showErrorMessage(e);
            });
        }catch (NullPointerException e){
            Platform.runLater(() -> {
                AlertMaker.showErrorMessage(e);
                System.exit(1);
            });
        }
    }

    public static void create_admin_table(){
        create_table(admin_table);
    }

    public static void create_device_table(){
        create_table(device_table);
    }

    public static void create_all_tables(){
        connect();
        create_admin_table();
        create_device_table();
        insert_root_admin();
        disconnect();
    }

    public static int insert_root_admin(){

        int val = 0;

        String hashedDefaultPassword = "";

        try {
            hashedDefaultPassword = Admin.Sha1("water");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        String sql = "insert into admin values('admin','" + hashedDefaultPassword + "','100','200','300','08024061810, 08085719687'," +
                "'S.W. 137, Opposite Kale Vision, Kwangila Road, Minna',?)";

        PreparedStatement ps = null;

        String fileName = "C:\\rms_photos\\admins\\";
        File file = new File(fileName + "admin.png");

        if(!file.exists()){
            file.mkdirs();
        }

        try{
            connect();

            ps = con.prepareStatement(sql);
            ps.setString(1, file.getAbsolutePath());
            val = ps.executeUpdate();

            disconnect();
        }catch(SQLException e){
            System.out.println("Root admin Already exist");
        }

        String format = "JPG";
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(new Image(DatabaseHelper.class.getResource("/admin.png").toExternalForm()), null), format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return val;
    }

    public static int insert_device(String imagePath){

        String q1 = "INSERT into device values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = null;

        int val = 0;
        try{
            connect();

            ps = con.prepareStatement(q1);
            ps.setString(1, NewDeviceDetail.device.getxCode());
            ps.setString(2, NewDeviceDetail.device.getType());
            ps.setString(3, NewDeviceDetail.device.getName());
            ps.setString(4, NewDeviceDetail.device.getDateSubmitted().toString());
            ps.setString(5, NewDeviceDetail.device.getCustomerName());
            ps.setString(6, NewDeviceDetail.device.getCustomerAddress());
            ps.setString(7, NewDeviceDetail.device.getPhone());
            ps.setString(8, imagePath);
            ps.setString(9, NewDeviceDetail.device.getModel());
            ps.setString(10, NewDeviceDetail.device.getSerialIMEINumber());
            ps.setString(11, NewDeviceDetail.device.getOmitted());
            ps.setString(12, String.valueOf(NewDeviceDetail.device.getTotalCost()));
            ps.setString(13, NewDeviceDetail.device.getFaults());
            ps.setString(14, String.valueOf(NewDeviceDetail.device.getAdvancePayment()));
            ps.setString(15, NewDeviceDetail.device.getStatus());
            ps.setString(16, "");

            val = ps.executeUpdate();

            disconnect();
        } catch ( SQLException ex) {
            Platform.runLater(() -> {
                AlertMaker.showWarning(ex);
            });
        }
        return val;
    }

    public static int insert_record(String sql)
    {
        int val = 0;
        try {
            connect();
            val = stmt.executeUpdate(sql);
        } catch ( SQLException e ) {
            System.out.println(e.getMessage());
        }
        return val;
    }

    public static int insert_record_using_preparedStmt(String sql, String filePath){
        int val = 0;

        PreparedStatement ps = null;

        connect();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, filePath);
            val = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return val;
    }

    public static ResultSet executeQuery(String sql)
    {
        connect();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                AlertMaker.showWarning(ex);
            });
        }

        return rs;
    }

    public static ResultSet getDevice(String xCode)
    {
        String sql = "select * from device where id=" + String.valueOf(xCode);

        return executeQuery(sql);
    }

    public static ResultSet getUserNamePassword_admin()
    {
        String sql="Select * from admin";
        return executeQuery(sql);
    }
}
