package com.control.repairsystem.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.time.LocalDate;

public class Device {

    private StringProperty type = new SimpleStringProperty(this, "type", null);
    private StringProperty name = new SimpleStringProperty(this, "name", null);
    private ObjectProperty<LocalDate> dateSubmitted = new SimpleObjectProperty<>(this, "dateSubmitted", null);
    private StringProperty customerName = new SimpleStringProperty(this, "customerName", null);
    private StringProperty customerAddress = new SimpleStringProperty(this, "customerAddress", null);
    private StringProperty phone = new SimpleStringProperty(this, "phone", null);
    private ObjectProperty<Image> bringerPhoto = new SimpleObjectProperty<>(this, "bringerPhoto", null);
    private StringProperty model = new SimpleStringProperty(this, "model", null);
    private StringProperty serialIMEINumber = new SimpleStringProperty(this, "serialIMEINumber", null);
    private StringProperty omitted = new SimpleStringProperty(this, "omitted", null);
    private DoubleProperty totalCost = new SimpleDoubleProperty(this, "totalCost", 0.0);
    private StringProperty faults = new SimpleStringProperty(this, "faults", null);
    private DoubleProperty advancePayment = new SimpleDoubleProperty(this, "advancePayment", 0.0);
    private StringProperty xCode = new SimpleStringProperty(this, "xCode", null);
    private StringProperty status = new SimpleStringProperty(this, "status", null);
    private String collectionDate;

    public Device(String type, String name, LocalDate dateSubmitted, String customerName,
                  String customerAddress, String phone, Image bringerPhoto,
                  String model, String serialIMEINumber,
                  String omitted, Double totalCost, String faults, Double advancePayment,
                  String xCode, String status, String collectionDate) {

        this.setType(type);
        this.setName(name);
        this.setDateSubmitted(dateSubmitted);
        this.setCustomerName(customerName);
        this.setCustomerAddress(customerAddress);
        this.setPhone(phone);
        this.setBringerPhoto(bringerPhoto);
        this.setModel(model);
        this.setSerialIMEINumber(serialIMEINumber);
        this.setOmitted(omitted);
        this.setTotalCost(totalCost);
        this.setFaults(faults);
        this.setAdvancePayment(advancePayment);
        this.setxCode(xCode);
        this.setStatus(status);
        this.collectionDate = collectionDate;
    }



    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public LocalDate getDateSubmitted() {
        return dateSubmitted.get();
    }

    public ObjectProperty<LocalDate> dateSubmittedProperty() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted.set(dateSubmitted);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getCustomerAddress() {
        return customerAddress.get();
    }

    public StringProperty customerAddressProperty() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress.set(customerAddress);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public Image getBringerPhoto() {
        return bringerPhoto.get();
    }

    public ObjectProperty<Image> bringerPhotoProperty() {
        return bringerPhoto;
    }

    public void setBringerPhoto(Image bringerPhoto) {
        this.bringerPhoto.set(bringerPhoto);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getSerialIMEINumber() {
        return serialIMEINumber.get();
    }

    public StringProperty serialIMEINumberProperty() {
        return serialIMEINumber;
    }

    public void setSerialIMEINumber(String serialIMEINumber) {
        this.serialIMEINumber.set(serialIMEINumber);
    }

    public String getOmitted() {
        return omitted.get();
    }

    public StringProperty omittedProperty() {
        return omitted;
    }

    public void setOmitted(String omitted) {
        this.omitted.set(omitted);
    }

    public double getTotalCost() {
        return totalCost.get();
    }

    public DoubleProperty totalCostProperty() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost.set(totalCost);
    }

    public String getFaults() {
        return faults.get();
    }

    public StringProperty faultsProperty() {
        return faults;
    }

    public void setFaults(String faults) {
        this.faults.set(faults);
    }

    public double getAdvancePayment() {
        return advancePayment.get();
    }

    public DoubleProperty advancePaymentProperty() {
        return advancePayment;
    }

    public void setAdvancePayment(double advancePayment) {
        this.advancePayment.set(advancePayment);
    }

    public String getxCode() {
        return xCode.get();
    }

    public StringProperty xCodeProperty() {
        return xCode;
    }

    public void setxCode(String xCode) {
        this.xCode.set(xCode);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    @Override
    public String toString() {
        return "Device{" +
                "type=" + type +
                ", name=" + name +
                ", dateSubmitted=" + dateSubmitted +
                ", customerName=" + customerName +
                ", customerAddress=" + customerAddress +
                ", phone=" + phone +
                ", bringerPhoto=" + bringerPhoto +
                ", model=" + model +
                ", serialIMEINumber=" + serialIMEINumber +
                ", omitted=" + omitted +
                ", totalCost=" + totalCost +
                ", faults=" + faults +
                ", advancePayment=" + advancePayment +
                ", xCode=" + xCode +
                ", status=" + status +
                ", collectionDate='" + collectionDate + '\'' +
                '}';
    }
}
