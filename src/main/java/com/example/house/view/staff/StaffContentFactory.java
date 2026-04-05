package com.example.house.view.staff;

import com.example.house.model.enums.StaffFeature;
import javafx.scene.Node;

public interface StaffContentFactory {
    Node createContent(StaffFeature feature);

    default Node createContent(StaffFeature feature, String context) {
        return createContent(feature);
    }
}
