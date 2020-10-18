package com.control.repairsystem.util;

import com.control.repairsystem.model.Device;
import com.control.repairsystem.presenter.DashboardPresenter;
import javafx.scene.image.Image;

import java.time.LocalDate;

public class NewDeviceDetail {

    public static Device device = new Device("Phone", "Dell", LocalDate.now(),
            "Mudi Lukman", "Gidan Kwanu", "08163630715",
            new Image(DashboardPresenter.class.getResource("/profile.png").toExternalForm()), "Infinix Hot 5 Pro", "X7526gtUSDGF726282HG",
            "Battery, Cover, Pouch", 5000.00, "Weak Battery, Slow Working, No Camera, Poor Sound",
            1000.00,"1978173329",
            "In Progress", "");

}
