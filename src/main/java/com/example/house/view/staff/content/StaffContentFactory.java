package com.example.house.view.staff.content;

import com.example.house.view.staff.StaffFeature;
import javafx.scene.Node;

public interface StaffContentFactory {
    Node createContent(StaffFeature feature);

    default Node createContent(StaffFeature feature, String context) {
        return createContent(feature);
    }
}
