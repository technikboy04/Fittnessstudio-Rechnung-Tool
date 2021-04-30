package application;

import java.sql.*;

public class DBConnection {

    private final String url = "jdbc:oracle:thin:@oracle.s-atiw.de:1521/atiwora";
    private String user = "FS192_ltroesch";
    private String password = "leon";
    private String db = "atiwora.FS192_ltroesch";
    private String command;

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public DBConnection(String text){
        setCommand("SELECT ");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Connection con = DriverManager.getConnection(getUrl(), getUser(), getPassword());

            Statement stt = con.createStatement();
            //stt.execute("Use"+" " + getDb());
            //stt.execute("Use"+" " + getDb());

            ResultSet res = stt.executeQuery(getCommand());
            String ergStr = "";
            while(res.next()){
                ergStr = ergStr.concat("\n" + res.getString("PERSONAL_NR") + "\n");
                System.out.println(ergStr);
            }


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

}