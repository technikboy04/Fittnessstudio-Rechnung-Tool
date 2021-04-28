package application;
	
import com.itextpdf.text.DocumentException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Test
			HBox root = (HBox)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setWidth(600);
			primaryStage.setHeight(600);
			primaryStage.setTitle("Rechnungstool");
			primaryStage.getIcons().add(new Image("application/icon.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	DBConnection db = new DBConnection("sd");
	
	public static void main(String[] args) throws IOException, DocumentException {


		//Erstellen der PDF und angabe der Vorlagendatei für PDF Creator
		PDFErstellen ps = new PDFErstellen();
		ps.createPdf("C:\\Fraps\\Rechnung.pdf");
		ps.printMeasures();
		System.out.println("done");
		launch(args);
	}
}
