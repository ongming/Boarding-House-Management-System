package com.example.house.view.admin;

import com.example.house.model.enums.AdminFeature;
import javafx.scene.Node;

public interface AdminContentFactory {
    Node createContent(AdminFeature feature);

    default Node createContent(AdminFeature feature, String context) {
        return createContent(feature);
    }
}
