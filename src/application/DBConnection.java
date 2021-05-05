package application;

import java.sql.*;

public class DBConnection {

    private static String url = "jdbc:oracle:thin:@oracle.s-atiw.de:1521/atiwora";
    private static String user = "FS192_jmorast";
    private static String password = "jan";
    private static String db = "atiwora.FS192_ltroesch";
    private static String command;

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public static String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public static ResultSet dbExecuteCommand(String command) {


        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Connection con = DriverManager.getConnection(getUrl(), getUser(), getPassword());


            Statement stt = con.createStatement();
            ResultSet result = stt.executeQuery(command);

            // stt.close();
            // con.close();

            return result;

//            String ergStr = "";
//            while(res.next()){
//                ergStr = ergStr.concat("\n" + res.getString("PERSONAL_NR") + "\n");
//                System.out.println(ergStr);
//            }


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


    public static void dbExecuteUpdate(String command) {


        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Connection con = DriverManager.getConnection(getUrl(), getUser(), getPassword());


            Statement stt = con.createStatement();
            stt.executeUpdate(command);


//            String ergStr = "";
//            while(res.next()){
//                ergStr = ergStr.concat("\n" + res.getString("PERSONAL_NR") + "\n");
//                System.out.println(ergStr);
//            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //Methode die nach dem Suchen Button ausgeführt wird um alle Rechnungsnummern für die gewünschte Kundennummer besorgt
    public static ResultSet rechnungenSuchen(String Kunde_ID) {
        String command = "select RECHNUNG_ID from FS192_ltroesch.Rechnung where Kunde_ID like '" + Kunde_ID + "'";

        return dbExecuteCommand(command);
    }

    public static ResultSet rechnungsinformationen(String Rechnung_ID) {
        String command = "select Kunde_ID, Rechnungssumme, Rechnung_ID, Status_Bezahlung, Rechnungsdatum, Zahlungsfrist from FS192_ltroesch.Rechnung where Rechnung_ID like '" + Rechnung_ID + "'";

        return dbExecuteCommand(command);
    }

    public static ResultSet listViewRechnungspositionenEintraege(String Rechnung_ID) {
        String command = "SELECT p.PRODUKTNAME, rp.ANZAHL, rp.PREIS FROM  FS192_ltroesch.RECHNUNG_PRODUKTKATALOG rp,  FS192_ltroesch.PRODUKTKATALOG p WHERE rp.PRODUKT_ID=p.PRODUKT_ID AND rp.RECHNUNG_ID LIKE '" + Rechnung_ID + "'";

        return dbExecuteCommand(command);
    }

    public static void updateButtonQuarryExcludeListView(String rechnung_ID, String status, String rechnungsdatum, String zahlungsfrist) {
        String command = "update FS192_ltroesch.Rechnung set STATUS_BEZAHLUNG = '" + status + "', Rechnungsdatum=" + rechnungsdatum + ", zahlungsfrist=" + zahlungsfrist + " where rechnung_ID like '" + rechnung_ID + "'";
        dbExecuteUpdate(command);
    }

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

    public static ResultSet getProduktkatalogItems() {
        String command = "Select PRODUKTNAME from FS192_ltroesch.PRODUKTKATALOG";
        return dbExecuteCommand(command);
    }

    public static ResultSet getNachname(String kunden_id){
        String command = "Select Nachname from FS192_ltroesch.Kunde where Kunde_ID like'" + kunden_id + "'";
        return dbExecuteCommand(command);
    }

    private static void updateButtonStornieren(String rechnung_id) {
        String command = "update FS192_ltroesch.RECHNUNG set STATUS_BEZAHLUNG = 'storniert' WHERE Rechnung_ID LIKE '" + rechnung_id  +"')";
        dbExecuteUpdate(command);
    }
}