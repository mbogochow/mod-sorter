package me.mbogo.modsorter.reader;

import com.google.common.collect.Lists;
import me.mbogo.modsorter.message.MessageLogger;
import me.mbogo.modsorter.mod.MasterModList;
import me.mbogo.modsorter.mod.Mod;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

// TODO add entity conversion for Writer

public class ModXMLFileReader implements ModFileReader {
    @Override
    public List<Mod> readFile(final String fileName) throws FileNotFoundException {
        final MasterModList modList = new MasterModList();
        final XMLInputFactory factory = XMLInputFactory.newInstance();

        List<Mod> beforeModList = null;
        Mod currMod = null;
        String tagContent = null;
        XMLStreamReader reader = null;
    /*
     * Need to do two passes in order to add all of the mods and then go 
     * through their before mods lists on the second pass so that we already 
     * have a list of all of the valid mods.
     */
        try {
            reader = factory.createXMLStreamReader(new BufferedInputStream(
                    new FileInputStream(fileName)));

            while (reader.hasNext()) {
                final int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if ("mod".equals(reader.getLocalName())) {
                            currMod = new Mod();

                            final String enabled = reader.getAttributeValue(null, "enabled");//reader.getAttributeValue(0);
                            if (enabled != null)
                                currMod.setEnabled(Boolean.parseBoolean(enabled));

                            final String external = reader.getAttributeValue(null, "external");
                            if (external != null)
                                currMod.setExternal(Boolean.parseBoolean(external));
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch (reader.getLocalName()) {
                            case "mod":
                                assert currMod != null;
                                if (modList.contains(currMod))
                                    MessageLogger.error("Duplicate entries found for " + currMod.getName() + ". Sorting results may be unexpected.");
                                if (!modList.add(currMod))
                                    MessageLogger.error("Could not add mod " + currMod.getName());
                                break;
                            case "name":
                                assert currMod != null;
                                currMod.setName(tagContent);
                                break;
                        }
                        break;
                }
            }
        } catch (final XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (final XMLStreamException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        final List<String> invalidMods = Lists.newArrayList();

        try {
            reader = factory.createXMLStreamReader(new BufferedInputStream(
                    new FileInputStream(fileName)));

            while (reader.hasNext()) {
                final int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if ("before".equals(reader.getLocalName())) {
                            assert currMod != null;
                            beforeModList = currMod.getBeforeList();
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch (reader.getLocalName()) {
                            case "name":
                                assert currMod != null;
                                currMod = modList.getMod(tagContent);
                                break;
                            case "modName":
                                assert tagContent != null;
                                assert beforeModList != null;
                                if (tagContent.equals(Mod.AllString)) {
                                    beforeModList.add(Mod.AllMod);
                                    break;
                                }

                                final Mod mod = modList.getMod(tagContent);
                                if (mod != null)
                                    beforeModList.add(mod);
                                else {
                                    assert currMod != null;
                                    invalidMods.add(currMod.getName() + ": " + tagContent);
                                }
                                break;
                        }
                        break;
                }
            }
        } catch (final XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (final XMLStreamException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (!invalidMods.isEmpty()) {
            final StringBuilder invalidModMsg = new StringBuilder();

            invalidModMsg.append("The following are invalid mods").append(System.lineSeparator());

            for (final String msg : invalidMods)
                invalidModMsg.append(msg).append(System.lineSeparator());

            MessageLogger.error(invalidModMsg.toString());
        }

        return modList.getList();
    }
}
