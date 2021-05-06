package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {


    @FXML
    private TextField textfield_rechnungsnummer;

    @FXML
    private TextField textfield_kundennummer;

    @FXML
    private ChoiceBox choicebox_status;

    @FXML
    private TableView tableview_rechnungspositionen;

    @FXML
    private TableColumn column_produktname;

    @FXML
    private TableColumn column_anzahl;

    @FXML
    private TableColumn column_preis;

    @FXML
    private ChoiceBox choicebox_produkte;

    @FXML
    private TextField textfield_anzahl;

    @FXML
    private DatePicker datepicker_datum;

    @FXML
    private DatePicker datepicker_zahlungsfrist;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Mit der CellValueFactory werden die Daten aus der Rechnungsposition passend in die TableView geladen
        column_produktname.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("produktname"));
        column_anzahl.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("anzahl"));
        column_preis.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("preis"));


        // Befüllen aller Felder
        textfield_kundennummer.setText(PopUpMain.getKundennummer());
        textfield_kundennummer.setDisable(true);
        textfield_rechnungsnummer.setText(PopUpMain.getRechnungsnummer());
        textfield_rechnungsnummer.setDisable(true);

        ResultSet res = DBConnection.rechnungsinformationen(textfield_rechnungsnummer.getText());
        try {
            while (res.next()) {


                choicebox_status.getItems().addAll("bezahlt", "offen", "storniert");
                switch (res.getString("Status_Bezahlung")) {
                    case "bezahlt":
                        choicebox_status.getSelectionModel().select(0);
                        break;
                    case "offen":
                        choicebox_status.getSelectionModel().select(1);
                        break;
                    case "storniert":
                        choicebox_status.getSelectionModel().select(2);
                        break;
                }

                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat DE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
                datepicker_datum.setValue((DATE_FORMAT.parse(res.getString("Rechnungsdatum"))).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                datepicker_zahlungsfrist.setValue((DATE_FORMAT.parse(res.getString("Zahlungsfrist"))).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());


                fillTableview();

                res = DBConnection.getProduktkatalogItems();

                while (res.next()) {
                    choicebox_produkte.getItems().add(res.getString("Produktname"));
                }

            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        tableview_rechnungspositionen.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Rechnungsposition rechnungsposition = (Rechnungsposition) tableview_rechnungspositionen.getSelectionModel().getSelectedItem();
            try {
                textfield_anzahl.setText(rechnungsposition.getAnzahl());
                choicebox_produkte.getSelectionModel().select(rechnungsposition.getProduktname());
            } catch (NullPointerException e) {

            }

        });

    }

    @FXML
    private void updateRechnungsposition() {
        Rechnungsposition rechnungsposition = (Rechnungsposition) tableview_rechnungspositionen.getSelectionModel().getSelectedItem();
        if (choicebox_produkte.getSelectionModel().getSelectedItem() != null && tableview_rechnungspositionen.getSelectionModel().getSelectedItem() != null) {
            DBConnection.updateButtonQuarryAenderDerRechnungspositionen(textfield_rechnungsnummer.getText(), textfield_anzahl.getText(), choicebox_produkte.getSelectionModel().getSelectedItem().toString(), rechnungsposition.getProduktname());

        }
        if (textfield_anzahl.getText() != null && choicebox_produkte.getSelectionModel().getSelectedItem() != null && tableview_rechnungspositionen.getSelectionModel().getSelectedItem() == null) {
            DBConnection.artikelHinzufuegen(textfield_rechnungsnummer.getText(), choicebox_produkte.getSelectionModel().getSelectedItem().toString(), textfield_anzahl.getText());
        }
        DBConnection.updateButtonQuarryExcludeListView(textfield_rechnungsnummer.getText(), choicebox_status.getSelectionModel().getSelectedItem().toString(), datepicker_datum.getValue().toString(), datepicker_zahlungsfrist.getValue().toString());

        try {

            fillTableview();
            textfield_anzahl.setText("");
            choicebox_produkte.setValue(null);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void fillTableview() throws SQLException {

        ResultSet res = DBConnection.listViewRechnungspositionenEintraege(textfield_rechnungsnummer.getText());

        if (tableview_rechnungspositionen.getItems().size() != 0) {

            tableview_rechnungspositionen.getItems().clear();
        }


        while (res.next()) {

            tableview_rechnungspositionen.getItems().add(new Rechnungsposition(res.getString("Produktname"), res.getString("Anzahl"), res.getString("Preis")));

        }
    }

    @FXML
    private void loescheRechnungsposition(){

    }


}
