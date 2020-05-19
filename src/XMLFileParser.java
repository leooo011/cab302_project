import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class XMLFileParser {
    private Color billboardBackground = CreateBillboardsPage.getDefaultBackground();
    private String billboardMessageText = "";
    private Color billboardMessageColour = CreateBillboardsPage.getDefaultTextColor();
    private String billboardInfoText = "";
    private Color billboardInfoColor = CreateBillboardsPage.getDefaultTextColor();
    private String billboardPictureUrl = "";
    private String billboardPictureData = "";
    public void importXmlFile(File file) {
        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            Node node = doc.getDocumentElement();
            Element element = (Element) node;
            if (element.hasAttribute("background")) {
                this.billboardBackground = stringToColor(node.getAttributes().getNamedItem("background").getNodeValue());
            }
            if (element.getElementsByTagName("message").item(0)!=null) {
                this.billboardMessageText = element.getElementsByTagName("message").item(0).getChildNodes().item(0).getNodeValue();
                if (element.getElementsByTagName("message").item(0).hasAttributes()) {
                    this.billboardMessageColour = stringToColor(element.getElementsByTagName("message").item(0).getAttributes().getNamedItem("colour").getNodeValue());
                }
            }
            if (element.getElementsByTagName("information").item(0)!=null) {
                this.billboardInfoText = element.getElementsByTagName("information").item(0).getChildNodes().item(0).getNodeValue();
                if (element.getElementsByTagName("information").item(0).hasAttributes()) {
                    this.billboardInfoColor = stringToColor(element.getElementsByTagName("information").item(0).getAttributes().getNamedItem("colour").getNodeValue());
                }
            }
            if (element.getElementsByTagName("picture").item(0)!=null&&element.getElementsByTagName("picture").item(0).hasAttributes()) {
                if (element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url") != null) {
                    this.billboardPictureUrl = element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url").getNodeValue();
                }
                if (element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("data") != null) {
                    this.billboardPictureData = element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("data").getNodeValue();
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    public static Color stringToColor(final String value) {
        try {
            // get color by hex or octal value
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            // if we can't decode lets try to get it by name
            try {
                // try to get a color by name using reflection
                final Field f = Color.class.getField(value);

                return (Color) f.get(null);
            } catch (Exception ce) {
                // if we can't get any color return black
                return Color.black;
            }
        }
    }

    public Color getBillboardBackground() {
        return billboardBackground;
    }

    public String getBillboardMessageText() {
        return billboardMessageText;
    }

    public Color getBillboardMessageColour() {
        return billboardMessageColour;
    }

    public String getBillboardInfoText() {
        return billboardInfoText;
    }

    public Color getBillboardInfoColor() {
        return billboardInfoColor;
    }

    public String getBillboardPictureUrl() {
        return billboardPictureUrl;
    }

    public String getBillboardPictureData() {
        return billboardPictureData;
    }

}
