import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.StringWriter;

public class CreateXMLFile {
    private Color billboardBackground ;
    private String billboardMessageText;
    private Color billboardMessageColour;
    private String billboardInfoText;
    private Color billboardInfoColor;
    private String billboardPictureUrl;
    private String billboardPictureData;

    public CreateXMLFile (Color billboardBackground, String billboardMessageText,
                          Color billboardMessageColour, String billboardPictureUrl,
                          String billboardPictureData, String billboardInfoText,
                          Color billboardInfoColor) {

        this.billboardBackground = billboardBackground;
        this.billboardMessageText = billboardMessageText;
        this.billboardMessageColour = billboardMessageColour;
        this.billboardInfoText = billboardInfoText;
        this.billboardInfoColor = billboardInfoColor;
        this.billboardPictureUrl = billboardPictureUrl;
        this.billboardPictureData = billboardPictureData;
        System.out.println(billboardBackground);
    }

    public String XMLFileCreator() throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // root element
        Element root = document.createElement("billboard");
        document.appendChild(root);

        if (billboardBackground != CreateBillboardsPage.getDefaultBackground()) {
            System.out.println(ColorParser.rgbTohex(billboardBackground));
            Attr attr = document.createAttribute("background");
            attr.setValue(billboardBackground.toString());
            root.setAttributeNode(attr);
        }

        if (!billboardMessageText.isEmpty()) {
            Element message = document.createElement("message");
            root.appendChild(message);
            message.setTextContent(billboardMessageText);
            if (billboardMessageColour != CreateBillboardsPage.getDefaultTextColor()) {
                Attr attr = document.createAttribute("colour");
                attr.setValue(ColorParser.rgbTohex(billboardMessageColour));
                message.setAttributeNode(attr);
            }
        }
        if (!billboardPictureUrl.isEmpty() && !billboardPictureUrl.equals(CreateBillboardsPage.getHintText())) {
            Element picture = document.createElement("picture");
            root.appendChild(picture);
            Attr attr = document.createAttribute("url");
            attr.setValue(billboardPictureUrl);
            picture.setAttributeNode(attr);
        }
        if (!billboardPictureData.isEmpty()) {
            Element picture = document.createElement("picture");
            root.appendChild(picture);
            Attr attr = document.createAttribute("data");
            attr.setValue(billboardPictureData);
            picture.setAttributeNode(attr);
        }
        if (!billboardInfoText.isEmpty()) {
            Element information = document.createElement("information");
            root.appendChild(information);
            information.setTextContent(billboardInfoText);
            if (billboardInfoColor != CreateBillboardsPage.getDefaultTextColor()) {
                Attr attr = document.createAttribute("colour");
                attr.setValue(ColorParser.rgbTohex(billboardInfoColor));
                information.setAttributeNode(attr);
            }
        }

        //transform the DOM Object to an String
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        transformer.transform(domSource, new StreamResult(stringWriter));
        return stringWriter.toString();
    }

}




