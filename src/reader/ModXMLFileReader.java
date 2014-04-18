package reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import mod.MasterModList;
import mod.Mod;

import com.google.common.collect.Lists;

public class ModXMLFileReader implements ModFileReader
{
  private MasterModList modList = new MasterModList();
  private XMLInputFactory factory = XMLInputFactory.newInstance();

  @Override
  public List<Mod> readFile(String fileName) throws FileNotFoundException
  {
    List<Mod> beforeModList = null;
    Mod currMod = null;
    String tagContent = null;
    /*
     * Need to do two passes
     */
    try
    {
      XMLStreamReader reader = factory
          .createXMLStreamReader(new BufferedInputStream(new FileInputStream(
              fileName)));

      while (reader.hasNext())
      {
        int event = reader.next();

        switch (event)
        {
        case XMLStreamConstants.START_ELEMENT:
          if ("mod".equals(reader.getLocalName()))
          {
            currMod = new Mod();
            String enabled = reader.getAttributeValue(0);
            if (enabled != null)
              currMod.setEnabled(Boolean.parseBoolean(enabled));
          }
          break;

        case XMLStreamConstants.CHARACTERS:
          tagContent = reader.getText().trim();
          break;

        case XMLStreamConstants.END_ELEMENT:
          switch (reader.getLocalName())
          {
          case "mod":
            if (!modList.add(currMod))
              System.err.println("Could not add mod " + currMod.getName());
            break;
          case "name":
            currMod.setName(tagContent);
            break;
          }
          break;
        }
      }
    }
    catch (XMLStreamException e)
    {
      e.printStackTrace();
      return null;
    }

    List<String> invalidMods = Lists.newArrayList();

    try
    {
      XMLStreamReader reader = factory
          .createXMLStreamReader(new BufferedInputStream(new FileInputStream(
              fileName)));

      while (reader.hasNext())
      {
        int event = reader.next();

        switch (event)
        {
        case XMLStreamConstants.START_ELEMENT:
          if ("before".equals(reader.getLocalName()))
            beforeModList = currMod.getBeforeList();
          break;

        case XMLStreamConstants.CHARACTERS:
          tagContent = reader.getText().trim();
          break;

        case XMLStreamConstants.END_ELEMENT:
          switch (reader.getLocalName())
          {
          case "name":
            currMod = modList.getMod(tagContent);
            break;
          case "modName":
            if (tagContent.equals(Mod.AllString))
            {
              beforeModList.add(Mod.AllMod);
              break;
            }
            Mod mod = modList.getMod(tagContent);
            if (mod != null)
              beforeModList.add(mod);
            else
              invalidMods.add(currMod.getName() + ": " + tagContent);
            break;
          }
          break;
        }
      }
    }
    catch (XMLStreamException e)
    {
      e.printStackTrace();
      return null;
    }

    if (!invalidMods.isEmpty())
    {
      String invalidModMsg = "The following are invalid mods"
          + System.lineSeparator();
      for (String msg : invalidMods)
        invalidModMsg += msg + System.lineSeparator();
      System.err.println(invalidModMsg);
    }

    return modList.getList();
  }
}
