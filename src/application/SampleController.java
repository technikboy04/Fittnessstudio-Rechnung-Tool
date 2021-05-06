package application;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SampleController implements Initializable {

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
    TextField textfield_kundennummer;

    @FXML
    public ListView<String> listview_rechnungsbrowser;

    @FXML
    TableView tableview_rechnungspositionen;

    @FXML
    TableColumn column_produktname;

    @FXML
    TableColumn column_anzahl;

    @FXML
    TableColumn column_preis;

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
                ergStr ="";


                //ergStr = ergStr.concat("\n" + res.getString("PERSONAL_NR") + "\n");
            }
        }

    }

    @FXML
    private void rechungAuswaehlen() throws SQLException, ParseException {
        ResultSet res = DBConnection.rechnungsinformationen(listview_rechnungsbrowser.getSelectionModel().getSelectedItem());
        while (res.next()){
            textfield_kundennummer.setText(res.getString("Kunde_ID"));

            textfield_rechnungsnummer.setText(res.getString("Rechnung_ID"));
            textfield_status.setText(res.getString("Status_Bezahlung"));
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat DE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

            textfield_datum.setText(DE_DATE_FORMAT.format(DATE_FORMAT.parse(res.getString("Rechnungsdatum").split(" ")[0])));

            textfield_zahlungsfrist.setText(DE_DATE_FORMAT.format(DATE_FORMAT.parse(res.getString("Zahlungsfrist"))));

            rechnungAktualisieren();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_bearbeiten.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {


                Stage newStage = new Stage();
                PopUpMain PopUp = new PopUpMain(textfield_sucheingabe.getText(), textfield_rechnungsnummer.getText(), tableview_rechnungspositionen.getItems());
                try {
                    PopUp.start(newStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        column_produktname.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("produktname"));
        column_anzahl.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("anzahl"));
        column_preis.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("preis"));

        textfield_kundennummer.setDisable(true);

    }

    @FXML
    private void toPDF() throws URISyntaxException, DocumentException, IOException, SQLException {
        List<String> data =  Arrays.asList(
                textfield_kundennummer.getText(),
                textfield_datum.getText(),
                textfield_rechnungsnummer.getText(),
                textfield_zahlungsfrist.getText(),
                textfield_rechnungssumme.getText(),
                textfield_status.getText()
        );

        Path dateipfadPath = Paths.get(Main.class.getResource("Rechnung.pdf").toURI());

        StringBuilder sb = new StringBuilder();
        sb.append(textfield_rechnungsnummer.getText() + "_");
        sb.append(textfield_datum.getText() + "_");
        ResultSet res = DBConnection.getNachname(textfield_kundennummer.getText());
        res.next();
        sb.append(res.getString("Nachname"));
        new PDFErstellen().createPdf(dateipfadPath.toString(),data, tableview_rechnungspositionen.getItems(), sb.toString());

    }

    public void rechnungAktualisieren(){

        try {
            ResultSet res = DBConnection.rechnungsinformationen(listview_rechnungsbrowser.getSelectionModel().getSelectedItem());
            while (res.next()){
                textfield_rechnungssumme.setText(res.getString("Rechnungssumme"));
            }

            res = DBConnection.listViewRechnungspositionenEintraege(textfield_rechnungsnummer.getText());

            tableview_rechnungspositionen.getItems().clear();
            while (res.next()){

                tableview_rechnungspositionen.getItems().add(new Rechnungsposition(res.getString("Produktname"), res.getString("Anzahl"), res.getString("Preis")));

            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

}
