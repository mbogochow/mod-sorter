package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mod.Mod;
import mod.ModAlphabeticalComparator;
import reader.ModFileReader;
import reader.ModListFileReader;
import reader.ModXMLFileReader;
import sorter.ModSorter;
import util.FileUtil;
import writer.ModOrganizerModWriter;
import writer.ModWriter;
import writer.SorterXMLModWriter;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class ModSorterController
{
  private static final String ModOrganizerModListFileName = "modlist.txt";
  private static final String ModSorterDefaultXMLFileName = "mods.xml";
  private static final String ProfilesDirectoryName = "profiles";

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button runButton;
  @FXML
  private Button generateButton;
  @FXML
  private Button updateButton;
  @FXML
  private TextField xmlPathField;
  @FXML
  private TextField modOrganizerField;
  @FXML
  private TextField profileNameField;

  private String getProfileDir()
  {
    String modOrganizerFieldText = modOrganizerField.getText();
    String profileName = profileNameField.getText();
    String profileDir = "";

    if (!modOrganizerFieldText.isEmpty())
    {
      if (!profileName.isEmpty())
      {
        profileDir = modOrganizerFieldText + "/" + ProfilesDirectoryName + "/"
            + profileName + "/";
      }
    }

    return profileDir;
  }

  private void backupFile(String fileName) throws IOException
  {
    File from = new File(fileName);
    File to = new File(fileName
        + new SimpleDateFormat("yyyyMMddhhmmss'.txt'").format(new Date()));
    Files.copy(from, to);
  }

  /**
   * Initializes the controller class.
   */
  @FXML
  void initialize()
  {
    /*
     * Set the action for the runButton. The runButton performs a sort according
     * to the specified mods XML file and generates a modlist.xml file which Mod
     * Organizer can use.
     * 
     * If the Mod Organizer path and profile name are provided then the result
     * file will overwrite the old the modlist.txt for that profile (after
     * creating a backup). If they are not provided then a new modlist.txt is
     * created in the working directory.
     */
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
                String profileDir = getProfileDir();
                String outFileName;

                if (profileDir.isEmpty())
                {
                  outFileName = ModOrganizerModListFileName;
                }

                else
                {
                  outFileName = profileDir + ModOrganizerModListFileName;
                  backupFile(outFileName);
                }

                mods = fileReader.readFile(xmlPathField.getText());

                File outFile = new File(outFileName);
                ModWriter writer = new ModOrganizerModWriter(outFile);
                writer.writeHeader();
                mods = ModSorter.sort(mods);

                writer.writeMods(Lists.reverse(mods));
                writer.closeFile();
                FileUtil.openInEditor(outFile);
              }
              catch (FileNotFoundException e)
              {
                System.err.println("Could not find file " + e.getMessage());
                e.printStackTrace();
              }
              catch (IOException e)
              {
                e.printStackTrace();
              }
            } /* run */
          }); /* thread */
          thread.setDaemon(true);
          thread.start();
        } /* handle */
      }); /* setOnAction */
    }

    else
      System.err.println("runButton not defined");

    /*
     * Set the action for the generateButton. The generateButton creates a
     * correctly formatted mods XML file from the modlist.txt file for the
     * specified profile.
     */
    if (generateButton != null)
    {
      generateButton.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          Thread thread = new Thread(new Runnable()
          {
            @Override
            public void run()
            {
              ModFileReader fileReader = new ModListFileReader();
              List<Mod> mods;
              try
              {
                String profileDir = getProfileDir();
                String inputFileName;

                if (profileDir.isEmpty())
                { // temp
                  System.err.println("Could not read "
                      + ModOrganizerModListFileName);
                  return;
                }

                else
                {
                  inputFileName = profileDir + ModOrganizerModListFileName;
                }

                mods = fileReader.readFile(inputFileName);

                File outFile = new File(ModSorterDefaultXMLFileName);
                ModWriter writer = new SorterXMLModWriter(outFile);
                writer.writeHeader();
                mods = ModSorter.sort(mods);

                Collections.sort(mods, new ModAlphabeticalComparator());
                writer.writeMods(mods);
                writer.writeFooter();
                writer.closeFile();
                
                FileUtil.openInEditor(outFile);
              }
              catch (FileNotFoundException e)
              {
                System.err.println("Could not find file " + e.getMessage());
                e.printStackTrace();
              }
              catch (IOException e)
              {
                e.printStackTrace();
              }
            } /* run */
          }); /* thread */
          thread.setDaemon(true);
          thread.start();
        } /* handle */
      }); /* setOnAction */
    }

    else
      System.err.println("generateButton not defined");

    /*
     * Set the action for the updateButton. The updateButton updates enabled
     * attributes for each mod in the specified mods XML file based on the
     * modlist.txt file for the specified profile. It will also add in mods
     * contained in the modlist.txt which were not previously located in the
     * mods XML file.
     */
    if (updateButton != null)
    {
      updateButton.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          Thread thread = new Thread(new Runnable()
          {
            @Override
            public void run()
            {
              ModFileReader modXMLFileReader = new ModXMLFileReader();
              ModFileReader modListFileReader = new ModListFileReader();
              List<Mod> xmlMods;
              List<Mod> listMods;
              try
              {
                String profileDir = getProfileDir();
                String xmlFileName = xmlPathField.getText();
                String listFileName;

                if (profileDir.isEmpty())
                { // temp
                  System.err.println("Could not read "
                      + ModOrganizerModListFileName);
                  return;
                }

                else
                {
                  listFileName = profileDir + ModOrganizerModListFileName;
                }

                xmlMods = modXMLFileReader.readFile(xmlFileName);
                listMods = modListFileReader.readFile(listFileName);

                backupFile(xmlFileName);
                
                /** Should move this logic somewhere else **/
                for (Mod mod : listMods)
                {
                  int index;
                  if ((index = xmlMods.indexOf(mod)) != -1)
                  { // mod contained in both lists
                    Mod xmlMod = xmlMods.get(index);
                    xmlMod.setEnabled(mod.isEnabled());
                    
                    /** This code adds all of the before mods from the mod from 
                     * the modslist mod to the xml mod if they are not already 
                     * there.  Since mods read from a modslist will never have 
                     * any mods in their before mods list, this code would 
                     * never do anything but it was written to have a more 
                     * complete function for combining lists of mods.  It 
                     * should be uncommented when this section of code is moved 
                     * to a more appropriate place. **/
                    // Update the before mods of the destination with any new 
                    // entries from the source 
//                    List<Mod> xmlModBeforeMods = xmlMod.getBeforeList();
//                    List<Mod> modBeforeMods = xmlMod.getBeforeList();
//                    
//                    for (Mod beforeMod : modBeforeMods)
//                    {
//                      if (!xmlModBeforeMods.contains(beforeMod))
//                        xmlModBeforeMods.add(beforeMod);
//                    }
                  }
                  
                  else // Add the mod if it is not already there
                  {
                    System.err.println("Adding new mod: " + mod.getName());
                    xmlMods.add(mod);
                  }
                }
                /** End section of logic that should be moved **/
                
                File outFile = new File(xmlFileName);
                ModWriter writer = new SorterXMLModWriter(outFile);
                writer.writeHeader();
                xmlMods = ModSorter.sort(xmlMods);

                Collections.sort(xmlMods, new ModAlphabeticalComparator());
                writer.writeMods(xmlMods);
                writer.writeFooter();
                writer.closeFile();
                FileUtil.openInEditor(outFile);
              }
              catch (FileNotFoundException e)
              {
                System.err.println("Could not find file " + e.getMessage());
                e.printStackTrace();
              }
              catch (IOException e)
              {
                e.printStackTrace();
              }
            } /* run */
          }); /* thread */
          thread.setDaemon(true);
          thread.start();
        } /* handle */
      }); /* setOnAction */
    }

    else
      System.err.println("updateButton not defined");
  } /* initialize */
}
