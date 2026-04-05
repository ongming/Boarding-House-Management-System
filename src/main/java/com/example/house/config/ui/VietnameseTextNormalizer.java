package com.example.house.config.ui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;

import java.util.LinkedHashMap;
import java.util.Map;

public final class VietnameseTextNormalizer {
    private static final Map<String, String> REPLACEMENTS = new LinkedHashMap<>();

    static {
        REPLACEMENTS.put("Báº£ng", "Bảng");
        REPLACEMENTS.put("Ä‘iá»u", "điều");
        REPLACEMENTS.put("khiá»n", "khiển");
        REPLACEMENTS.put("nhÃ¢n", "nhân");
        REPLACEMENTS.put("viÃªn", "viên");
        REPLACEMENTS.put("ÄÄng", "Đăng");
        REPLACEMENTS.put("xuáº¥t", "xuất");
        REPLACEMENTS.put("Trang chá»§", "Trang chủ");
    }

    private VietnameseTextNormalizer() {
    }

    public static void normalizeNodeTree(Node node) {
        if (node == null) {
            return;
        }

        if (node instanceof Labeled labeled) {
            labeled.setText(normalize(labeled.getText()));
        }
        if (node instanceof TextInputControl input) {
            input.setPromptText(normalize(input.getPromptText()));
        }
        if (node instanceof ComboBox<?> comboBox) {
            ObservableList<?> items = comboBox.getItems();
            if (items != null && !items.isEmpty() && items.get(0) instanceof String) {
                @SuppressWarnings("unchecked")
                ObservableList<String> stringItems = (ObservableList<String>) items;
                for (int i = 0; i < stringItems.size(); i++) {
                    stringItems.set(i, normalize(stringItems.get(i)));
                }
            }
        }

        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                normalizeNodeTree(child);
            }
        }
    }

    public static String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String fixed = text;
        for (Map.Entry<String, String> entry : REPLACEMENTS.entrySet()) {
            fixed = fixed.replace(entry.getKey(), entry.getValue());
        }
        return fixed;
    }
}

