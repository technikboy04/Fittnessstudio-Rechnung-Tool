package application;

import java.sql.*;

public class DBConnection {

    private static String url = "jdbc:oracle:thin:@oracle.s-atiw.de:1521/atiwora";
    private static String user = "FS192_ltroesch";
    private static String password = "leon";
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

    public static ResultSet dbExecuteCommand(String command){


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

//Methode die nach dem Suchen Button ausgeführt wird um alle Rechnungsnummern für die gewünschte Kundennummer besorgt
    public static ResultSet rechnungenSuchen(String Kunde_ID){
        String command ="select RECHNUNG_ID from FS192_ltroesch.Rechnung where Kunde_ID like '" + Kunde_ID + "'";

        return dbExecuteCommand(command);
    }

    public static ResultSet rechnungsinformationen(String Rechnung_ID){
        String command ="select Kunde_ID, Rechnungssumme, Rechnung_ID, Status_Bezahlung, Rechnungsdatum, Zahlungsfrist from FS192_ltroesch.Rechnung where Rechnung_ID like '" + Rechnung_ID + "'";

        return dbExecuteCommand(command);
    }

    public static ResultSet listViewRechnungspositionenEintraege(String Rechnung_ID){
        String command = "SELECT rp.RECHNUNG_ID, rp.PRODUKT_ID, p.PRODUKT_ID, p.PRODUKTNAME, rp.ANZAHL, rp.PREIS FROM  FS192_ltroesch.RECHNUNG_PRODUKTKATALOG rp,  FS192_ltroesch.PRODUKTKATALOG p WHERE rp.PRODUKT_ID=p.PRODUKT_ID AND rp.RECHNUNG_ID LIKE '"+Rechnung_ID + "'";

        return dbExecuteCommand(command);
    }


    public static void updateButtonQuarryExcludeListView(String rechnung_ID, String status, String rechnungsdatum, String zahlungsfrist){
        String command ="update Rechnung set Status = " + status + ", Rechnungsdatum=" + rechnungsdatum + ", zahlungsfrist=" + zahlungsfrist + "where rechnung_ID like '" + rechnung_ID + "'";
        dbExecuteCommand(command);
    }

}