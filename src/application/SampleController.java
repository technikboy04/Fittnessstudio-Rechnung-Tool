package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class SampleController {

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
	
}
