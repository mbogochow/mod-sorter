package me.mbogo.modsorter.writer;

import com.google.common.collect.Maps;
import me.mbogo.modsorter.mod.Mod;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SorterXMLModWriter extends SimpleModWriter {
    private static final String RootElementName = "mods";
    private static final String MainElementsName = "mod";
    private static final String EnabledAttrName = "enabled";
    private static final String ExternalAttrName = "external";
    private static final String NameElementName = "name";
    private static final String BeforeListElementName = "before";
    private static final String BeforeListItemElementName = "modName";
    private static final String IndentString = "  ";

    private final Map<String, String> modAttributeMap = Maps.newHashMap();

    private int indentLevel = 0;

    public SorterXMLModWriter(final File file) throws IOException {
        super(file);
    }

    public SorterXMLModWriter(final String filePath) throws IOException {
        super(filePath);
    }

    private void writeXMLOpenTag(final String name, final boolean hasClosingTag) throws IOException {
        writer.write("<" + name);
        if (hasClosingTag)
            writer.write(">");
        else
            writer.write("/>");
    }

    private void writeXMLOpenTag(final String name, final boolean hasClosingTag, final Map<String, String> attributes)
            throws IOException {
        final StringBuilder sb = new StringBuilder();

        sb.append("<").append(name);

        final Set<String> attributeNames = attributes.keySet();

        for (final String attrName : attributeNames) {
            String attrValue = attributes.get(attrName);

            sb.append(" ").append(attrName).append("=\"").append(attrValue).append("\"");
        }

        if (hasClosingTag)
            sb.append(">");
        else
            sb.append("/>");

        writer.write(sb.toString());
    }

    private void writeXMLCloseTag(final String name) throws IOException {
        writer.write("</" + name + ">");
    }

    private void indent() throws IOException {
        for (int i = 0; i < indentLevel; i++)
            writer.write(IndentString);
    }

    private void newLine() throws IOException {
        writer.write(System.lineSeparator());
    }

    @Override
    public void writeHeader() throws IOException {
        writeXMLOpenTag(RootElementName, true);
        newLine();
        indentLevel += 1;
    }

    @Override
    public void writeFooter() throws IOException {
        writeXMLCloseTag(RootElementName);
        newLine();
        indentLevel -= 1;
    }

    @Override
    public void writeMod(final Mod mod) throws IOException {
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
        final List<Mod> beforeMods = mod.getBeforeList();
        for (final Mod beforeMod : beforeMods) {
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
