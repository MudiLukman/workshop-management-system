package com.control.repairsystem.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Admin implements Serializable{

    private StringProperty username = new SimpleStringProperty(this, "username", null);
    private StringProperty password = new SimpleStringProperty(this, "password", null);
    private StringProperty phoneFee = new SimpleStringProperty(this, "phoneFee", null);
    private StringProperty gameFee = new SimpleStringProperty(this, "gameFee", null);
    private StringProperty laptopFee = new SimpleStringProperty(this, "laptopFee", null);
    private StringProperty phones = new SimpleStringProperty(this, "phones", null);
    private StringProperty address = new SimpleStringProperty(this, "address", null);
    private ObjectProperty<Image> photo = new SimpleObjectProperty<>(this, "photo", null);

    public Admin(String username, String password, String phoneFee, String gameFee,
                 String laptopFee, String phones, String address, Image photo) {
        this.setUsername(username);
        this.setPassword(password);
        this.setPhoneFee(phoneFee);
        this.setGameFee(gameFee);
        this.setLaptopFee(laptopFee);
        this.setPhones(phones);
        this.setAddress(address);
        this.setPhoto(photo);

    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getPhoneFee() {
        return phoneFee.get();
    }

    public StringProperty phoneFeeProperty() {
        return phoneFee;
    }

    public void setPhoneFee(String phoneFee) {
        this.phoneFee.set(phoneFee);
    }

    public String getGameFee() {
        return gameFee.get();
    }

    public StringProperty gameFeeProperty() {
        return gameFee;
    }

    public void setGameFee(String gameFee) {
        this.gameFee.set(gameFee);
    }

    public String getLaptopFee() {
        return laptopFee.get();
    }

    public StringProperty laptopFeeProperty() {
        return laptopFee;
    }

    public void setLaptopFee(String laptopFee) {
        this.laptopFee.set(laptopFee);
    }

    public String getPhones() {
        return phones.get();
    }

    public StringProperty phonesProperty() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones.set(phones);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public Image getPhoto() {
        return photo.get();
    }

    public ObjectProperty<Image> photoProperty() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo.set(photo);
    }

    public static String Sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++)
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username=" + username +
                ", password=" + password +
                ", phoneFee=" + phoneFee +
                ", gameFee=" + gameFee +
                ", laptopFee=" + laptopFee +
                ", phones=" + phones +
                ", address=" + address +
                ", photo=" + photo +
                '}';
    }
}
