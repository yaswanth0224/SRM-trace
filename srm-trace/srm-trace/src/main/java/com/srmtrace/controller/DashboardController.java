package com.srmtrace.controller;

import com.srmtrace.model.Item;
import com.srmtrace.model.User;
import com.srmtrace.service.ItemService;
import com.srmtrace.service.MatchService;
import com.srmtrace.util.QRUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DashboardController {
    @FXML public Label lblUser;
    @FXML public TextField titleField;
    @FXML public TextArea descField;
    @FXML public ChoiceBox<String> typeChoice;
    @FXML public TextField locationField;
    @FXML public Button pickImageBtn;
    @FXML public Button postBtn;
    @FXML public ListView<String> listView;
    @FXML public Label matchLabel;
    @FXML public ImageView preview;

    private User user;
    private File chosenImage;

    @FXML
    public void initialize() {
        typeChoice.setItems(FXCollections.observableArrayList("lost","found"));
        typeChoice.setValue("lost");
        refreshList();
    }

    public void setUser(User u) {
        this.user = u;
        lblUser.setText("Hi, " + (u.displayName==null?u.username:u.displayName) + " (rep: "+u.reputation+")");
        if (u.isAdmin) {
            // show admin button
        }
        refreshList();
    }

    @FXML
    public void onPickImage() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));
        File f = fc.showOpenDialog(lblUser.getScene().getWindow());
        if (f != null) {
            chosenImage = f;
            preview.setImage(new Image(f.toURI().toString(),200,200,true,true));
        }
    }

    @FXML
    public void onPost() {
        Item it = new Item();
        it.title = titleField.getText().trim();
        it.description = descField.getText().trim();
        it.type = typeChoice.getValue();
        it.location = locationField.getText().trim();
        it.userId = user.id;
        // save image to data/images
        if (chosenImage != null) {
            try {
                File out = new File("data/images");
                out.mkdirs();
                File dest = new File(out, System.currentTimeMillis() + "_" + chosenImage.getName());
                java.nio.file.Files.copy(chosenImage.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                it.imagePath = dest.getPath();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        // create QR
        String qr = QRUtil.createQRCode("item:" + System.currentTimeMillis(), "qrcode_" + System.currentTimeMillis() + ".png");
        it.qrCode = qr;
        it.verified = false;
        ItemService.add(it);
        clearForm();
        refreshList();
    }

    private void clearForm() { titleField.clear(); descField.clear(); locationField.clear(); preview.setImage(null); chosenImage = null; }

    public void refreshList() {
        listView.getItems().clear();
        List<Item> all = ItemService.all();
        for (Item it : all) {
            String s = "[" + it.type.toUpperCase() + "] " + it.title + " (" + (it.verified ? "VER" : "PEND") + ") - " + it.location;
            listView.getItems().add(s + " || id:" + it.id);
        }
    }

    @FXML
    public void onSelectItem() {
        String sel = listView.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        // parse id
        int id = Integer.parseInt(sel.substring(sel.indexOf("id:")+3));
        List<Item> all = ItemService.all();
        Item chosen = null;
        for (Item it : all) if (it.id == id) chosen = it;
        if (chosen == null) return;
        // compute matches
        List<Map.Entry<Item, Double>> ranked = MatchService.rankMatches(chosen, all);
        StringBuilder sb = new StringBuilder();
        int top = Math.min(5, ranked.size());
        for (int i=0;i<top;i++) {
            Map.Entry<Item, Double> e = ranked.get(i);
            sb.append(String.format("%d) %s — %.2f%%\n", i+1, e.getKey().title, e.getValue()));
        }
        matchLabel.setText(sb.length()==0? "No matches" : sb.toString());
    }

    @FXML
    public void openAdmin() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            javafx.stage.Stage st = new javafx.stage.Stage();
            st.setScene(scene);
            st.setTitle("Admin - SRM Trace");
            ((AdminController)loader.getController()).refresh();
            st.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
