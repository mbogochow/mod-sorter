package me.mbogo.modsorter.application;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import me.mbogo.modsorter.message.Message;
import me.mbogo.modsorter.message.MessageBuffer;
import me.mbogo.modsorter.message.MessageLogger;
import me.mbogo.modsorter.mod.Mod;
import me.mbogo.modsorter.mod.ModAlphabeticalComparator;
import me.mbogo.modsorter.reader.ModFileReader;
import me.mbogo.modsorter.reader.ModListFileReader;
import me.mbogo.modsorter.reader.ModXMLFileReader;
import me.mbogo.modsorter.sorter.ModSorter;
import me.mbogo.modsorter.util.FileUtil;
import me.mbogo.modsorter.writer.ModOrganizerModWriter;
import me.mbogo.modsorter.writer.ModWriter;
import me.mbogo.modsorter.writer.SorterXMLModWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModSorterController implements Observer {
    private static final String ModOrganizerModListFileName = "modlist.txt";
    private static final String ModSorterDefaultXMLFileName = "mods.xml";
    private static final String ProfilesDirectoryName = "profiles";

    @FXML
    private Button runButton;
    @FXML
    private Button generateButton;
    @FXML
    private Button updateButton;
    @FXML
    private CheckBox openResultCheckBox;
    @FXML
    private TextField xmlPathField;
    @FXML
    private TextField modOrganizerField;
    @FXML
    private TextField profileNameField;
    @FXML
    private TextArea outputTextArea;

    private String getProfileDir() {
        String modOrganizerFieldText = modOrganizerField.getText();
        String profileName = profileNameField.getText();
        String profileDir = "";

        if (!modOrganizerFieldText.isEmpty()) {
            if (!profileName.isEmpty()) {
                profileDir = modOrganizerFieldText + "/" + ProfilesDirectoryName + "/"
                        + profileName + "/";
            }
        }

        return profileDir;
    }

    private void backupFile(String fileName) throws IOException {
        File from = new File(fileName);

        String[] fileNameParts = fileName.split("\\.(?=[^\\.]+$)");
        String namePart = fileNameParts[0];
        String extension = fileNameParts[1];

        File to = new File(namePart
                + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "."
                + extension);

        Files.copy(from, to);
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    void initialize() {
        MessageLogger.addObserver(this);
    /*
     * TODO: since the code for each button action is pretty similar should try
     * and merge some of it together
     */

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
        if (runButton != null) {
            runButton.setOnAction(event -> {
                Thread thread = new Thread(() -> {
                    ModFileReader fileReader = new ModXMLFileReader();
                    List<Mod> mods;
                    try {
                        String profileDir = getProfileDir();
                        String outFileName;

                        if (profileDir.isEmpty())
                            outFileName = ModOrganizerModListFileName;

                        else
                            outFileName = profileDir + ModOrganizerModListFileName;

                        mods = fileReader.readFile(xmlPathField.getText());

                        mods = ModSorter.sort(mods);

                        if (mods != null) {
                            // Backup the file before overwriting it
                            backupFile(outFileName);

                            File outFile = new File(outFileName);
                            ModWriter writer = new ModOrganizerModWriter(outFile);

                            writer.writeHeader();
                            writer.writeMods(Lists.reverse(mods));
                            writer.closeFile();

                            if (openResultCheckBox.isSelected())
                                FileUtil.openInEditor(outFile);

                            MessageLogger.log("Successfully sorted.  Output written to "
                                    + outFileName);
                        } else
                            MessageLogger.error("Failed to sort mods");
                    } catch (FileNotFoundException e) {
                        MessageLogger.error("Could not find file " + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }); /* thread */
                thread.setDaemon(true);
                thread.start();
            }); /* setOnAction */
        } else
            MessageLogger.bug("runButton not defined");

    /*
     * Set the action for the generateButton. The generateButton creates a
     * correctly formatted mods XML file from the modlist.txt file for the
     * specified profile.
     */
        if (generateButton != null) {
            generateButton.setOnAction(event -> {
                Thread thread = new Thread(() -> {
                    ModFileReader fileReader = new ModListFileReader();
                    List<Mod> mods;
                    try {
                        String profileDir = getProfileDir();
                        String inputFileName;

                        if (profileDir.isEmpty()) { // temp
                            MessageLogger.error("Could not read "
                                    + ModOrganizerModListFileName);
                            return;
                        } else {
                            inputFileName = profileDir + ModOrganizerModListFileName;
                        }

                        mods = fileReader.readFile(inputFileName);

                        // Sort the XML entries alphabetically
                        Collections.sort(mods, new ModAlphabeticalComparator());

                        File outFile = new File(ModSorterDefaultXMLFileName);
                        ModWriter writer = new SorterXMLModWriter(outFile);

                        writer.writeHeader();
                        writer.writeMods(mods);
                        writer.writeFooter();
                        writer.closeFile();

                        if (openResultCheckBox.isSelected())
                            FileUtil.openInEditor(outFile);

                        MessageLogger.log("Successfully generated XML file from "
                                + inputFileName + ". Output written to "
                                + ModSorterDefaultXMLFileName);
                    } catch (FileNotFoundException e) {
                        MessageLogger.error("Could not find file " + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }); /* thread */
                thread.setDaemon(true);
                thread.start();
            }); /* setOnAction */
        } else
            MessageLogger.bug("generateButton not defined");

    /*
     * Set the action for the updateButton. The updateButton updates enabled
     * attributes for each me.mbogo.modsorter.mod in the specified mods XML file based on the
     * modlist.txt file for the specified profile. It will also add in mods
     * contained in the modlist.txt which were not previously located in the
     * mods XML file.
     */
        if (updateButton != null) {
            updateButton.setOnAction(event -> {
                Thread thread = new Thread(() -> {
                    ModFileReader modXMLFileReader = new ModXMLFileReader();
                    ModFileReader modListFileReader = new ModListFileReader();
                    List<Mod> xmlMods;
                    List<Mod> listMods;
                    try {
                        String profileDir = getProfileDir();
                        String xmlFileName = xmlPathField.getText();
                        String listFileName;

                        if (profileDir.isEmpty()) { // temp
                            MessageLogger.error("Could not read "
                                    + ModOrganizerModListFileName);
                            return;
                        } else {
                            listFileName = profileDir + ModOrganizerModListFileName;
                        }

                        xmlMods = modXMLFileReader.readFile(xmlFileName);
                        listMods = modListFileReader.readFile(listFileName);

                        /** Should move this logic somewhere else **/
                        // Update the xmlMods with date from listMods
                        for (Mod mod : listMods) {
                            int index;
                            if ((index = xmlMods.indexOf(mod)) != -1) { // me.mbogo.modsorter.mod contained in both lists
                                Mod xmlMod = xmlMods.get(index);

                                boolean xmlEnabled = xmlMod.isEnabled();
                                boolean listEnabled = mod.isEnabled();

                                if (xmlEnabled != listEnabled) {
                                    MessageLogger.log("Switching " + mod.getName()
                                            + " Enabled state from " + xmlEnabled + " to "
                                            + listEnabled);
                                    xmlMod.setEnabled(mod.isEnabled());
                                }

                                boolean xmlExternal = xmlMod.isExternal();
                                boolean listExternal = mod.isExternal();

                                if (xmlExternal != listExternal) {
                                    MessageLogger.log("Switching " + mod.getName()
                                            + " External state from " + xmlExternal + " to "
                                            + listExternal);
                                    xmlMod.setExternal(mod.isExternal());
                                }

                                /**
                                 * This code adds all of the before mods from the me.mbogo.modsorter.mod from
                                 * the modslist me.mbogo.modsorter.mod to the xml me.mbogo.modsorter.mod if they are not already
                                 * there. Since mods read from a modslist will never have
                                 * any mods in their before mods list, this code would never
                                 * do anything but it was written to have a more complete
                                 * function for combining lists of mods. It should be
                                 * uncommented when this section of code is moved to a more
                                 * appropriate place.
                                 **/
                                // Update the before mods of the destination with any new
                                // entries from the source
                                // List<Mod> xmlModBeforeMods = xmlMod.getBeforeList();
                                // List<Mod> modBeforeMods = xmlMod.getBeforeList();
                                //
                                // for (Mod beforeMod : modBeforeMods)
                                // {
                                // if (!xmlModBeforeMods.contains(beforeMod))
                                // xmlModBeforeMods.add(beforeMod);
                                // }
                            } else
                            // Add the me.mbogo.modsorter.mod if it is not already there
                            {
                                MessageLogger.log("Adding new me.mbogo.modsorter.mod: " + mod.getName());
                                xmlMods.add(mod);
                            }
                        }
                        /** End section of logic that should be moved **/

                        // xmlMods = ModSorter.sort(xmlMods);

                        // Sort the XML entries alphabetically in case any were added
                        Collections.sort(xmlMods, new ModAlphabeticalComparator());

                        // Backup the old file before overwriting it
                        backupFile(xmlFileName);

                        File outFile = new File(xmlFileName);
                        ModWriter writer = new SorterXMLModWriter(outFile);

                        writer.writeHeader();
                        writer.writeMods(xmlMods);
                        writer.writeFooter();
                        writer.closeFile();

                        if (openResultCheckBox.isSelected())
                            FileUtil.openInEditor(outFile);

                        MessageLogger.log("Successfully updated " + xmlFileName
                                + " with the data from " + listFileName);
                    } catch (FileNotFoundException e) {
                        MessageLogger.error("Could not find file " + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }); /* thread */
                thread.setDaemon(true);
                thread.start();
            }); /* setOnAction */
        } else
            MessageLogger.bug("updateButton not defined");
    } /* initialize */

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof MessageBuffer) {
            writeMessageList(((MessageBuffer) arg0).getMessages());
        } else if (arg1 instanceof List<?>) {
            List<?> list = (List<?>) arg1;

            // Check if the list has Messages. This is the wrong way of doing this
            // since it could be a list of Objects.
            if (list.size() > 0 && list.get(0) instanceof Message) {
                writeMessageList((List<Message>) list);
            } else
                throw new IllegalArgumentException();
        } else if (arg1 instanceof Message) {
            writeMessage((Message) arg1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void writeMessageList(final List<Message> msgBuf) {
        for (Message msg : msgBuf) {
            writeMessage(msg);
        }
    }

    private void writeMessage(final Message msg) {
        if (msg != null) {
            switch (msg.messageType()) {
                case LOG:
                    appendMessageText("LOG: " + msg.message());
                    break;
                case ERR:
                    appendMessageText("ERR: " + msg.message());
                    break;
                case BUG:
                    appendMessageText("BUG: " + msg.message());
                    break;
            }
        }
    }

    private void appendMessageText(final String msg) {
        if (outputTextArea != null) {
            Platform.runLater(() -> outputTextArea.appendText(msg + System.lineSeparator()));
        } else
            System.err.println("outputTextArea is null");
    }
}
