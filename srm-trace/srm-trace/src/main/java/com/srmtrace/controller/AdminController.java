package com.srmtrace.controller;

import com.srmtrace.model.Item;
import com.srmtrace.service.ItemService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class AdminController {
    @FXML public ListView<String> itemsList;
    @FXML public Button verifyBtn;
    @FXML public Button deleteBtn;
    @FXML public Label status;

    public void refresh() {
        itemsList.getItems().clear();
        List<Item> all = ItemService.all();
        for (Item it : all) {
            itemsList.getItems().add(String.format("id:%d | %s | %s | verified:%b", it.id, it.type, it.title, it.verified));
        }
    }

    @FXML
    public void onVerify() {
        String sel = itemsList.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        int id = Integer.parseInt(sel.split("\\|")[0].replace("id:","").trim());
        ItemService.verify(id, true);
        status.setText("Verified " + id);
        refresh();
    }

    @FXML
    public void onDelete() {
        String sel = itemsList.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        int id = Integer.parseInt(sel.split("\\|")[0].replace("id:","").trim());
        ItemService.delete(id);
        status.setText("Deleted " + id);
        refresh();
    }
}
