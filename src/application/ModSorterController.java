package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mod.Mod;
import reader.ModFileReader;
import reader.ModXMLFileReader;
import sorter.ModSorter;
import util.FileUtil;
import writer.ModOrganizerModWriter;
import writer.ModWriter;

import com.google.common.collect.Lists;

public class ModSorterController
{
  private static final String outFileName = "modlist.txt";

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button runButton;
  @FXML
  private TextField xmlPathField;

  /**
   * Initializes the controller class.
   */
  @FXML
  void initialize()
  {
    if (runButton != null)
    {
      runButton.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        { 
          Thread thread = new Thread(new Runnable()
          {
            @Override
            public void run()
            {
              ModFileReader fileReader = new ModXMLFileReader();
              List<Mod> mods;
              try
              {
                mods = fileReader.readFile(xmlPathField.getText());

                File outFile = new File(outFileName);
                ModWriter writer = new ModOrganizerModWriter(outFile);
                writer.initWrite();
                mods = ModSorter.sort(mods);
                
                writer.writeMods(Lists.reverse(mods));
                FileUtil.openInEditor(outFile);
              }
              catch (IOException e)
              {
                e.printStackTrace();
              }
            }
          });
          thread.setDaemon(true);
          thread.start();
        }
      });
    }

    else
      System.err.println("runButton not defined");
  }
}
