package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application
{

  public static void main(String[] args)
  {
    Application.launch(Main.class, (java.lang.String[]) null);
  }

  @Override
  public void start(Stage primaryStage)
  {
    try
    {
      BorderPane borderPane = (BorderPane) FXMLLoader.load(Main.class
          .getResource("ModSorterFXML.fxml"));
      
      Scene scene = new Scene(borderPane);
      
      primaryStage.setScene(scene);
      primaryStage.setTitle("Mod Sorter");
      primaryStage.show();
      
     
      scene.getStylesheets().add
       (Main.class.getResource("Application.css").toExternalForm());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
