package application;

import com.itextpdf.text.DocumentException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main extends Application {

    //Wird benötigt, um die Informationen Stage-übergreifend zu aktualisieren
    public static SampleController controller;

    @Override
    public void start(Stage primaryStage) {
        try {
            //HBox root = (HBox)FXMLLoader.load(getClass().getResource("Sample.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
            HBox root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setWidth(600);
            primaryStage.setHeight(600);
            primaryStage.setTitle("Rechnungstool");
            primaryStage.getIcons().add(new Image("application/icon.png"));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, DocumentException, URISyntaxException {
        launch(args);
    }
}
