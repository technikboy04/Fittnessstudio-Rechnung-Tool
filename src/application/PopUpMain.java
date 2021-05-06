package application;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class PopUpMain extends Application {

    private static String kundennummer;
    private static String rechnungsnummer;
    private static ObservableList list;

    public PopUpMain(String kundennummer, String rechnungsnummer, ObservableList list) {
        PopUpMain.list = list;
        PopUpMain.setKundennummer(kundennummer);
        PopUpMain.setRechnungsnummer(rechnungsnummer);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("ChangePopUp.fxml"));
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.setHeight(730);
        primaryStage.setWidth(550);
        primaryStage.setTitle("Rechnung bearbeiten");
        primaryStage.setOnCloseRequest(event -> Main.controller.rechungAuswaehlen());
        primaryStage.show();
    }

    //Getter und Setter
    public static String getKundennummer() {
        return kundennummer;
    }

    public static void setKundennummer(String kundennummer) {
        PopUpMain.kundennummer = kundennummer;
    }

    public static String getRechnungsnummer() {
        return rechnungsnummer;
    }

    public static void setRechnungsnummer(String rechnungsnummer) {
        PopUpMain.rechnungsnummer = rechnungsnummer;
    }

    public static ObservableList getList() {
        return list;
    }

    public static void setList(ObservableList list) {
        PopUpMain.list = list;
    }
}
