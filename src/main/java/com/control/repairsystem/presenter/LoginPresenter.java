package com.control.repairsystem.presenter;

import com.control.repairsystem.App;
import com.control.repairsystem.model.Admin;
import com.control.repairsystem.util.Administrator;
import com.control.repairsystem.util.AlertMaker;
import com.control.repairsystem.util.DatabaseHelper;
import com.control.repairsystem.view.LoginView;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPresenter {

    private LoginView view;
    private String username;
    private String password;

    public LoginPresenter(){
        view = new LoginView();
        setEventHandlers();
    }

    private void setEventHandlers() {
        view.getLogin().setTooltip(new Tooltip("Click to login"));
        view.getLogin().setDefaultButton(true);
        view.getLogin().setOnAction(this::setOnLoginPressed);
    }

    private void setOnLoginPressed(ActionEvent event){
        username = view.getUsernameField().getText();
        password = view.getPasswordField().getText();

        if(firstValidation(username, password)){
            if(secondValidation(username, password)){
                App.switchWindow(getView(), new DashboardPresenter().getView(), "Administrator Dashboard");
            }else{
                AlertMaker.showNotification("Error", "No such user in existence", AlertMaker.warning);
            }
        }
        else{
            AlertMaker.showNotification("Empty values", "You must provide both username and password", AlertMaker.warning);
        }
    }

    private boolean firstValidation(String username, String password) {
        return ((username != null && !username.equals("")) && (password != null && !password.equals("")));
    }

    private boolean secondValidation(String username, String password) {

        ResultSet resultSet = DatabaseHelper.getUserNamePassword_admin();
        try {
            while (resultSet.next()){
                if(username.equals(resultSet.getString("username")) && Admin.Sha1(password).equals(resultSet.getString("password"))){
                    Administrator.adminName = resultSet.getString("username");
                    return true;
                }
            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        } catch (NoSuchAlgorithmException e) {
            AlertMaker.showErrorMessage(e);
        }

        return false;
    }

    public LoginView getView(){
        return view;
    }

}
