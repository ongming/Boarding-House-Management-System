package com.example.house.view.admin.content;

import com.example.house.view.admin.AdminFeature;
import javafx.scene.Node;

public interface AdminContentFactory {
    Node createContent(AdminFeature feature);

    default Node createContent(AdminFeature feature, String context) {
        return createContent(feature);
    }
}
