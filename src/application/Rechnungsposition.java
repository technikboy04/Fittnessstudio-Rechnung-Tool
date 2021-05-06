package application;

import javafx.beans.property.SimpleStringProperty;

/**
 * Klasse, um die Daten in die TableView zu packen
 * Speichert nur den Produktnamen, die Anzahl und den Preis
 */
public class Rechnungsposition {

    private SimpleStringProperty produktname;
    private SimpleStringProperty anzahl;
    private SimpleStringProperty preis;

    public Rechnungsposition(String produktname, String anzahl, String preis){
        this.produktname = new SimpleStringProperty(produktname);
        this.anzahl = new SimpleStringProperty(anzahl);
        this.preis = new SimpleStringProperty(preis);
    }

    public String getProduktname() {
        return produktname.get();
    }

    public SimpleStringProperty produktnameProperty() {
        return produktname;
    }

    public void setProduktname(String produktname) {
        this.produktname.set(produktname);
    }

    public String getAnzahl() {
        return anzahl.get();
    }

    public SimpleStringProperty anzahlProperty() {
        return anzahl;
    }

    public void setAnzahl(String anzahl) {
        this.anzahl.set(anzahl);
    }

    public String getPreis() {
        return preis.get();
    }

    public SimpleStringProperty preisProperty() {
        return preis;
    }

    public void setPreis(String preis) {
        this.preis.set(preis);
    }
}
