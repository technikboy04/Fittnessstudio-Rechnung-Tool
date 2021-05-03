package application;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SampleController implements Initializable {

    @FXML
    TextField textfield_kundenField;

    @FXML
    TextField textfield_rechnungssumme;

    @FXML
    TextField textfield_rechnungsnummer;

    @FXML
    TextField textfield_status;

    @FXML
    TextField textfield_datum;

    @FXML
    TextField textfield_zahlungsfrist;

    @FXML
    TextField textfield_sucheingabe;

    @FXML
    ListView<String> listview_rechnungsbrowser;

    @FXML
    ListView<String> listview_rechnunspositionen;

    @FXML
    Button button_pdferstellen;

    @FXML
    Button button_bearbeiten;

    @FXML
    Button button_stornieren;

    @FXML
    ImageView imageview_suchicon;


    @FXML
    private void suchen() throws SQLException {
        listview_rechnungsbrowser.getItems().clear();
        try (ResultSet rechungen = DBConnection.rechnungenSuchen(textfield_sucheingabe.getText())) {
            String ergStr = "";
            while (rechungen.next()) {
                ergStr = ergStr.concat(rechungen.getString("Rechnung_id"));
                listview_rechnungsbrowser.getItems().add(ergStr);

                //ergStr = ergStr.concat("\n" + res.getString("PERSONAL_NR") + "\n");
            }
        }

    }

    @FXML
    private void rechungAuswaehlen(){
        System.out.println(listview_rechnungsbrowser.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_bearbeiten.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                Parent root;
                try {
                    root = FXMLLoader.load(Main.class.getClassLoader().getResource("ChangePopUo.fxml"), resources);
                    Stage stage = new Stage();
                    stage.setTitle("My New Stage Title");
                    stage.setScene(new Scene(root, 450, 450));
                    stage.show();
                    // Hide this current window (if this is what you want)
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
