package application;

import javafx.scene.control.Alert;

import java.sql.*;

public class DBConnection {

    private static String url = "jdbc:oracle:thin:@oracle.s-atiw.de:1521/atiwora";
    private static String user = "FS192_jmorast";
    private static String password = "jan";
    private static String db = "atiwora.FS192_ltroesch";
    private static String command;

    private static String getUrl() {
        return url;
    }

    private static String getUser() {
        return user;
    }

    private void setUser(String user) {
        this.user = user;
    }

    private static String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private String getDb() {
        return db;
    }

    private void setDb(String db) {
        this.db = db;
    }

    private static String getCommand() {
        return command;
    }

    private void setCommand(String command) {
        this.command = command;
    }


    /**
     * Führt das übergebene SQL-Statement aus und liefert die Ergebnisse als ResultSet
     *
     * @param command Auszuführende SQL-Query
     * @return ResultSet mit den Ergebnissen
     */
    public static ResultSet dbExecuteCommand(String command) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

            Connection con = DriverManager.getConnection(getUrl(), getUser(), getPassword());


            Statement stt = con.createStatement();
            ResultSet result = stt.executeQuery(command);

            return result;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    private static void dbExecuteUpdate(String command) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Connection con = DriverManager.getConnection(getUrl(), getUser(), getPassword());


            Statement stt = con.createStatement();
            stt.executeUpdate(command);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehlerhafter Eintrag oder Eintrag schon vorhanden!");
            alert.setContentText("Das Produkt ist entweder schon vorhanden oder es wurde eine ungültige Eingabe getätigt!");
            alert.showAndWait();
        }

    }

    /**
     * Sucht nach Rechnungen, die zur übergenenen Kundennummer gehören
     *
     * @param Kunde_ID Kundennummer, zu der die Rechnungen gesuch werden
     * @return Rechnungsnummern des Kunden mit der Kunden_ID
     */
    public static ResultSet rechnungenSuchen(String Kunde_ID) {
        String command = "select RECHNUNG_ID from FS192_ltroesch.Rechnung where Kunde_ID like '" + Kunde_ID + "'";

        return dbExecuteCommand(command);
    }

    /**
     * Sucht die Rechnungsdaten zu der übergebenen Rechnunungsnummer
     *
     * @param Rechnung_ID Rechnungsnummer, deren Informationen benötigt werden
     * @return ResultSet mit den Rechnungsinformationen
     */
    public static ResultSet rechnungsinformationen(String Rechnung_ID) {
        String command = "select Kunde_ID, Rechnungssumme, Rechnung_ID, Status_Bezahlung, Rechnungsdatum, Zahlungsfrist from FS192_ltroesch.Rechnung where Rechnung_ID like '" + Rechnung_ID + "'";

        return dbExecuteCommand(command);
    }

    /**
     * Sucht die ganzen Rechnungspositionen zu der Rechnungsnummer
     *
     * @param Rechnung_ID Rechnungsnummer, deren Rechnungspositionen benötigt werden
     * @return Rechnungspositionen
     */
    public static ResultSet listViewRechnungspositionenEintraege(String Rechnung_ID) {
        String command = "SELECT p.PRODUKTNAME, rp.ANZAHL, rp.PREIS FROM  FS192_ltroesch.RECHNUNG_PRODUKTKATALOG rp,  FS192_ltroesch.PRODUKTKATALOG p WHERE rp.PRODUKT_ID=p.PRODUKT_ID AND rp.RECHNUNG_ID LIKE '" + Rechnung_ID + "'";

        return dbExecuteCommand(command);
    }

    /**
     * Ändert den Status, das Rechnungsdatum und die Zahlungsfrist
     *
     * @param rechnung_ID    Rechnungsnummer, die aktualisiert werden soll
     * @param status         Neuer Status
     * @param rechnungsdatum Neues Rechnungsdatum
     * @param zahlungsfrist  Neue Zahlungsfrist
     */
    public static void updateButtonQuarryExcludeListView(String rechnung_ID, String status, String rechnungsdatum, String zahlungsfrist) {
        String command = "update FS192_ltroesch.Rechnung set STATUS_BEZAHLUNG = '" + status + "', Rechnungsdatum= DATE'" + rechnungsdatum + "', zahlungsfrist= DATE'" + zahlungsfrist + "' where rechnung_ID like '" + rechnung_ID + "'";
        dbExecuteUpdate(command);
    }

    /**
     * Ändert die Rechnungspositionen zu der übergebenen Rechnungsnummer
     *
     * @param rechnung_ID     Rechnugsnummer, deren Rechnungsposition geändert werden soll
     * @param anzahl          Anzahl des Produktes
     * @param Produktname     Name des Produktes
     * @param Produktname_old Name des vorherigen Produktes
     */
    public static void updateButtonQuarryAenderDerRechnungspositionen(String rechnung_ID, String anzahl, String Produktname, String Produktname_old) {
        String command = "UPDATE FS192_ltroesch.RECHNUNG_PRODUKTKATALOG SET Anzahl=" + anzahl + ", Produkt_ID=(SELECT produkt_id FROM FS192_ltroesch.PRODUKTKATALOG WHERE PRODUKTNAME LIKE '" + Produktname + "') WHERE Rechnung_ID LIKE '" + rechnung_ID + "' and Produkt_ID like (select produkt_id from FS192_ltroesch.PRODUKTKATALOG where PRODUKTNAME LIKE '" + Produktname_old + "')";
        dbExecuteUpdate(command);

        updateGesamtPreisjeRechnungsPosition(rechnung_ID, Produktname);
        updateRechnungssumme(rechnung_ID);
    }

    private static void updateGesamtPreisjeRechnungsPosition(String rechnung_id, String produktname) {
        String command = "update FS192_ltroesch.RECHNUNG_PRODUKTKATALOG set preis=(SELECT p.PREIS*rp.ANZAHL FROM FS192_ltroesch.RECHNUNG_PRODUKTKATALOG rp ,FS192_ltroesch.PRODUKTKATALOG p WHERE rp.PRODUKT_ID=p.PRODUKT_ID and rp.RECHNUNG_ID LIKE '"
                + rechnung_id + "' AND p.PRODUKT_ID like (select produkt_id from FS192_ltroesch.PRODUKTKATALOG  WHERE PRODUKTNAME LIKE '" + produktname + "')) where RECHNUNG_ID like '" + rechnung_id +
                "' and PRODUKT_ID like (select produkt_id from FS192_ltroesch.PRODUKTKATALOG where  PRODUKTNAME LIKE '" + produktname + "')";

        dbExecuteUpdate(command);
    }

    private static void updateRechnungssumme(String rechnung_id) {
        String command = "update FS192_ltroesch.RECHNUNG set RECHNUNGSSUMME=(select sum(Preis) from FS192_ltroesch.RECHNUNG_PRODUKTKATALOG where RECHNUNG_ID like '" + rechnung_id + "') where RECHNUNG_ID like '" + rechnung_id + "'";
        dbExecuteUpdate(command);
    }

    /**
     * Liefert alle Produktnamen aus dem Produktkatalog
     *
     * @return Produktnamen
     */
    public static ResultSet getProduktkatalogItems() {
        String command = "Select PRODUKTNAME from FS192_ltroesch.PRODUKTKATALOG";
        return dbExecuteCommand(command);
    }

    /**
     * Liefert den Nachmanen zu der Kundennummer
     *
     * @param kunden_id Kundennummer
     * @return Nachname
     */
    public static ResultSet getNachname(String kunden_id) {
        String command = "Select Nachname from FS192_ltroesch.Kunde where Kunde_ID like'" + kunden_id + "'";
        return dbExecuteCommand(command);
    }

    /**
     * Setzt den Status der Rechnung auf "storniert"
     *
     * @param rechnung_id Rechnungsnummer
     */
    public static void updateButtonStornieren(String rechnung_id) {
        String command = "update FS192_ltroesch.RECHNUNG set STATUS_BEZAHLUNG = 'storniert' WHERE Rechnung_ID LIKE '" + rechnung_id + "'";
        dbExecuteUpdate(command);
    }

    /**
     * Fügt der Rechnungsposition einen neuen Artikel hinzu
     *
     * @param rechnung_id Rechnungsnummer
     * @param produktname Produktname
     * @param anzahl      Anzahl
     */

    public static void artikelHinzufuegen(String rechnung_id, String produktname, String anzahl) {
        String command = "INSERT INTO FS192_LTROESCH.RECHNUNG_PRODUKTKATALOG VALUES ('" + rechnung_id + "', " +
                "(SELECT PRODUKT_ID FROM FS192_LTROESCH.PRODUKTKATALOG p WHERE PRODUKTNAME LIKE '" + produktname + "'), (SELECT PREIS FROM FS192_LTROESCH.PRODUKTKATALOG WHERE PRODUKT_ID LIKE (SELECT PRODUKT_ID FROM FS192_LTROESCH.PRODUKTKATALOG p WHERE PRODUKTNAME LIKE '" + produktname + "')), " + anzahl + ")";
        dbExecuteUpdate(command);
    }


    /**
     * SQL Quarry zum löschen von Rechnungspositionen in der Datenabnk
     * @param rechnung_id Rechnungsnummer
     * @param produktname Produktname
     */
    public static void deleteRechnungsposition(String rechnung_id, String produktname){
       String command =  "DELETE FROM FS192_ltroesch.RECHNUNG_PRODUKTKATALOG WHERE RECHNUNG_ID LIKE '" + rechnung_id + "' AND PRODUKT_ID LIKE (SELECT PRODUKT_ID FROM FS192_LTROESCH.PRODUKTKATALOG WHERE PRODUKTNAME LIKE '" + produktname + "')" ;
        dbExecuteUpdate(command);
    }
}