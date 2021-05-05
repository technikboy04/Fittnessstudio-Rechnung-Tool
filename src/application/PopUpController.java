package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {


    @FXML
    TextField textfield_rechnungsnummer;

    @FXML
    TextField textfield_kundennummer;

    @FXML
    ChoiceBox choicebox_status;

    @FXML
    TableView tableview_rechnungspositionen;

    @FXML
    TableColumn column_produktname;

    @FXML
    TableColumn column_anzahl;

    @FXML
    TableColumn column_preis;

    @FXML
    ChoiceBox choicebox_produkte;

    @FXML
    TextField textfield_anzahl;

    @FXML
    DatePicker datepicker_datum;

    @FXML
    DatePicker datepicker_zahlungsfrist;

    private static Boolean isClosed = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isClosed =false;

        column_produktname.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("produktname"));
        column_anzahl.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("anzahl"));
        column_preis.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("preis"));

        textfield_kundennummer.setText(PopUpMain.getKundennummer());
        textfield_rechnungsnummer.setText(PopUpMain.getRechnungsnummer());

        ResultSet res = DBConnection.rechnungsinformationen(textfield_rechnungsnummer.getText());
        try {
            while (res.next()) {


                choicebox_status.getItems().addAll("bezahlt", "offen", "storniert");
                switch (res.getString("Status_Bezahlung")){
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
            try{
                textfield_anzahl.setText(rechnungsposition.getAnzahl());
                choicebox_produkte.getSelectionModel().select(rechnungsposition.getProduktname());
            }catch (NullPointerException e){

            }

        });

    }

    @FXML
    private void updateRechnungsposition() {
        Rechnungsposition rechnungsposition = (Rechnungsposition) tableview_rechnungspositionen.getSelectionModel().getSelectedItem();
        if(choicebox_produkte.getSelectionModel().getSelectedItem() != null){
            DBConnection.updateButtonQuarryAenderDerRechnungspositionen(textfield_rechnungsnummer.getText(), textfield_anzahl.getText(), choicebox_produkte.getSelectionModel().getSelectedItem().toString(), rechnungsposition.getProduktname());

        }
        DBConnection.updateButtonQuarryExcludeListView(textfield_rechnungsnummer.getText(), choicebox_status.getSelectionModel().getSelectedItem().toString(), datepicker_datum.getValue().toString() , datepicker_zahlungsfrist.getValue().toString());

        try {

            fillTableview();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void fillTableview() throws SQLException {

        ResultSet res = DBConnection.listViewRechnungspositionenEintraege(textfield_rechnungsnummer.getText());

        if(tableview_rechnungspositionen.getItems().size() != 0){
            System.out.println(tableview_rechnungspositionen.getItems().size());
            tableview_rechnungspositionen.getItems().clear();
        }


        while (res.next()) {

            tableview_rechnungspositionen.getItems().add(new Rechnungsposition(res.getString("Produktname"), res.getString("Anzahl"), res.getString("Preis")));

        }
    }

    public static boolean isClosed(){
        return isClosed;
    }

    public static void  setIsClosed(){
        isClosed = true;
    }

}
