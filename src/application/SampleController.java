package application;

import com.itextpdf.text.DocumentException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private Label textfield_rechnungssumme;

    @FXML
    private Label textfield_rechnungsnummer;

    @FXML
    private Label textfield_status;

    @FXML
    private Label textfield_datum;

    @FXML
    private Label textfield_zahlungsfrist;

    @FXML
    private TextField textfield_sucheingabe;

    @FXML
    private Label textfield_kundennummer;

    @FXML
    public ListView<String> listview_rechnungsbrowser;

    @FXML
    private TableView tableview_rechnungspositionen;

    @FXML
    private TableColumn column_produktname;

    @FXML
    private TableColumn column_anzahl;

    @FXML
    private TableColumn column_preis;

    @FXML
    private Button button_pdferstellen;

    @FXML
    private Button button_bearbeiten;

    @FXML
    private Button button_stornieren;

    @FXML
    private ImageView imageview_suchicon;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    //Damit die Daten aus der Datenbank in das passende Format konvertiert werden können
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Sucht die Rechnungen zu der Kundennummer im Suchfeld
     * @throws SQLException
     */
    @FXML
    private void suchen() throws SQLException {
        listview_rechnungsbrowser.getItems().clear();
        try (ResultSet rechnungen = DBConnection.rechnungenSuchen(textfield_sucheingabe.getText())) {
            String ergStr = "";
            while (rechnungen.next()) {
                ergStr = ergStr.concat(rechnungen.getString("Rechnung_id"));
                listview_rechnungsbrowser.getItems().add(ergStr);
                ergStr = "";

            }
        }

    }

    /**
     * Lädt die Daten der ausgewählten Rechnung in die Informationsfelder
     */
    @FXML
    public void rechungAuswaehlen() {
        try {
            ResultSet res = DBConnection.rechnungsinformationen(listview_rechnungsbrowser.getSelectionModel().getSelectedItem());
            while (res.next()) {
                textfield_kundennummer.setText(res.getString("Kunde_ID"));
                textfield_rechnungssumme.setText(res.getString("Rechnungssumme"));
                textfield_rechnungsnummer.setText(res.getString("Rechnung_ID"));
                textfield_status.setText(res.getString("Status_Bezahlung"));


                textfield_datum.setText(DE_DATE_FORMAT.format(DATE_FORMAT.parse(res.getString("Rechnungsdatum").split(" ")[0])));

                textfield_zahlungsfrist.setText(DE_DATE_FORMAT.format(DATE_FORMAT.parse(res.getString("Zahlungsfrist"))));

                res = DBConnection.listViewRechnungspositionenEintraege(textfield_rechnungsnummer.getText());

                tableview_rechnungspositionen.getItems().clear();
                while (res.next()) {

                    tableview_rechnungspositionen.getItems().add(new Rechnungsposition(res.getString("Produktname"), res.getString("Anzahl"), res.getString("Preis")));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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

    }

    /**
     * Sammelt alle Daten der Rechnung und erstellt mit diesen eine PDF
     * @throws URISyntaxException
     * @throws DocumentException
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void toPDF() throws URISyntaxException, DocumentException, IOException, SQLException {
        List<String> data = Arrays.asList(
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
        new PDFErstellen().createPdf(dateipfadPath.toString(), data, tableview_rechnungspositionen.getItems(), sb.toString());

        alert.setTitle("Erfolgreich");
        alert.setHeaderText("Die PDF wurde erfolgreich erstellt!");
        alert.setContentText("Die PDF " + sb.toString() + ".pdf wurde erfolgreich erstellt!");
        alert.showAndWait();

    }

    /**
     * Setzt den Status der ausgewählten Rechnung auf "storniert
     */
    @FXML
    private void stornierenButton() {
        DBConnection.updateButtonStornieren(textfield_rechnungsnummer.getText());
        rechungAuswaehlen();
    }

}
