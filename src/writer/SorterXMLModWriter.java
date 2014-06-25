package writer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import mod.Mod;

public class SorterXMLModWriter extends SimpleModWriter
{
  private static final String RootElementName = "mods";
  private static final String MainElementsName = "mod";
  private static final String EnabledAttrName = "enabled";
  private static final String ExternalAttrName = "external";
  private static final String NameElementName = "name";
  private static final String BeforeListElementName = "before";
  private static final String BeforeListItemElementName = "modName";
  private static final String IndentString = "  ";
  
  private Map<String, String> modAttributeMap = Maps.newHashMap();
  
  private int indentLevel = 0;

  public SorterXMLModWriter(File file) throws IOException
  {
    super(file);
  }

  public SorterXMLModWriter(String filePath) throws IOException
  {
    super(filePath);
  }

  private void writeXMLOpenTag(String name, boolean hasClosingTag) throws IOException
  {
    writer.write("<" + name);
    if (hasClosingTag)
      writer.write(">");
    else
      writer.write("/>");
  }

  private void writeXMLOpenTag(String name, boolean hasClosingTag, Map<String, String> attributes)
      throws IOException
  {
    StringBuilder sb = new StringBuilder();

    sb.append("<" + name);

    Set<String> attributeNames = attributes.keySet();
    Iterator<String> iter = attributeNames.iterator();

    while (iter.hasNext())
    {
      String attrName = iter.next();
      String attrValue = attributes.get(attrName);

      sb.append(" " + attrName + "=\"" + attrValue + "\"");
    }

    if (hasClosingTag)
      sb.append(">");
    else
      sb.append("/>");

    writer.write(sb.toString());
  }
  
  private void writeXMLCloseTag(String name) throws IOException
  {
    writer.write("</" + name + ">");
  }
  
  private void indent() throws IOException
  {
    for (int i = 0; i < indentLevel; i++)
      writer.write(IndentString);
  }
  
  private void newLine() throws IOException
  {
    writer.write(System.lineSeparator());
  }

  @Override
  public void writeHeader() throws IOException
  {
    writeXMLOpenTag(RootElementName, true);
    newLine();
    indentLevel += 1;
  }

  @Override
  public void writeFooter() throws IOException
  {
    writeXMLCloseTag(RootElementName);
    newLine();
    indentLevel -= 1;
  }
  
  @Override
  public void writeMod(Mod mod) throws IOException
  {
    // Start out with opening the main element tag with its attributes
    indent();
    modAttributeMap.put(ExternalAttrName, Boolean.toString(mod.isExternal()));
    modAttributeMap.put(EnabledAttrName, Boolean.toString(mod.isEnabled()));
    writeXMLOpenTag(MainElementsName, true, modAttributeMap);
    newLine();
    indentLevel += 1;
    
    // Write the name element for the mod
    indent();
    writeXMLOpenTag(NameElementName, true);
    writer.write(mod.getName());
    writeXMLCloseTag(NameElementName);
    newLine();
    
    // Start the before mods list by opening the before list tag
    indent();
    writeXMLOpenTag(BeforeListElementName, true);
    indentLevel += 1;
    
    // Write a before mods list item for each of the mod's before mods
    List<Mod> beforeMods = mod.getBeforeList();
    for (Mod beforeMod : beforeMods)
    {
      newLine();
      indent();
      writeXMLOpenTag(BeforeListItemElementName, true);
      writer.write(beforeMod.getName());
      writeXMLCloseTag(BeforeListItemElementName);
    }
    
    // Close the before mods list
    indentLevel -= 1;
    newLine();
    indent();
    writeXMLCloseTag(BeforeListElementName);
    
    // Close the main element tag
    indentLevel -= 1;
    newLine();
    indent();
    writeXMLCloseTag(MainElementsName);
    newLine();
  }
}
