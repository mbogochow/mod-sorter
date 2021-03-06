package me.mbogo.modsorter.reader;

import com.google.common.collect.Lists;
import me.mbogo.modsorter.mod.MasterModList;
import me.mbogo.modsorter.mod.Mod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ModDOMParserFileReader implements ModFileReader {
    private MasterModList modList = new MasterModList();

    @Override
    public List<Mod> readFile(String fileName) throws FileNotFoundException {
        // Get the DOM Builder Factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Get the DOM Builder
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        // Load and Parse the XML document
        // document contains the complete XML as a Tree.
        Document document = null;
        try {
            document = builder.parse(new BufferedInputStream(new FileInputStream(
                    fileName)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        // Iterating through the nodes and extracting the data.
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            // We have encountered an <Mod> tag.
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Mod mod = new Mod();

                String enabled = node.getAttributes().getNamedItem("enabled")
                        .getNodeValue();
                if (enabled != null)
                    mod.setEnabled(Boolean.parseBoolean(enabled));

                String external = node.getAttributes().getNamedItem("external")
                        .getNodeValue();
                if (external != null)
                    mod.setExternal(Boolean.parseBoolean(external));

                mod.setPriority(count++);

                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);

                    // Identifying the child tag of Mod encountered.
                    if (cNode instanceof Element) {
                        Node child = cNode.getLastChild();
                        if (child == null)
                            continue;
                        String content = child.getTextContent().trim();
                        switch (cNode.getNodeName()) {
                            case "name":
                                mod.setName(content);
                                break;
                            case "before":
                                List<Mod> beforeMods = Lists.newArrayList();
                                NodeList beforeNodes = cNode.getChildNodes();
                                for (int k = 0; k < beforeNodes.getLength(); k++) {
                                    Node bNode = childNodes.item(k);
                                    if (bNode instanceof Element) {
                                        Node bChild = bNode.getLastChild();
                                        if (child == null)
                                            continue;
                                        String tagContent = bChild.getTextContent().trim();
                                        switch (bNode.getNodeName()) {
                                            case "modName":

                                                if (tagContent.equals(Mod.AllString)) {
                                                    beforeMods.add(Mod.AllMod);
                                                    break;
                                                }

                                                Mod mod1 = modList.getMod(tagContent);
                                                if (mod1 == null) {
                                                    mod1 = new Mod(tagContent);
                                                    modList.add(mod1);
                                                }
                                                beforeMods.add(mod1);
                                                break;
                                        }
                                    }
                                }
                                mod.addAllMods(beforeMods);
                                break;
                        }
                    }
                }
                modList.add(mod);
            }
        }

        return modList.getList();
    }
}
