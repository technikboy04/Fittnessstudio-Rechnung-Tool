package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {


    @FXML
    TextField textfield_rechnungsnummer;

    @FXML
    TextField textfield_kundennummer;

    @FXML
    TextField textfield_datum;

    @FXML
    TextField textfield_zahlungsfrist;

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



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        column_produktname.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("produktname"));
        column_anzahl.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("anzahl"));
        column_preis.setCellValueFactory(new PropertyValueFactory<Rechnungsposition, String>("preis"));

        textfield_kundennummer.setText(PopUpMain.getKundennummer());
        textfield_rechnungsnummer.setText(PopUpMain.getRechnungsnummer());

        ResultSet res = DBConnection.rechnungsinformationen(textfield_rechnungsnummer.getText());
        try{
            while (res.next()) {


                choicebox_status.getItems().add(res.getString("Status_Bezahlung"));
                choicebox_status.getItems().add("Storniert");
                choicebox_status.getSelectionModel().select(0);
              //  textfield_datum.setText(res.getString("Rechnungsdatum"));
               // textfield_zahlungsfrist.setText(res.getString("Zahlungsfrist"));

                res = DBConnection.listViewRechnungspositionenEintraege(textfield_rechnungsnummer.getText());

                while (res.next()){

                    tableview_rechnungspositionen.getItems().add(new Rechnungsposition(res.getString("Produktname"), res.getString("Anzahl"), res.getString("Preis")));

                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }


    }

}
