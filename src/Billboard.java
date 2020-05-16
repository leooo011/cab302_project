import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Billboard implements Serializable {
    private String userName;
    private String billboardName;
    private String billboardBackground;
    private String messageText;
    private String messageColour;
    private String infoText;
    private String infoColor;
    private String pictureUrl;
    private String pictureData;
    public Billboard(String userName, String billboardName){
        this.userName =userName;
        this.billboardName = billboardName;
    }

    public String getAuthorName() {
        return userName;
    }

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
                this.billboardBackground = node.getAttributes().getNamedItem("background").getNodeValue();
            }
            if (element.getElementsByTagName("message").item(0)!=null) {
                this.messageText = element.getElementsByTagName("message").item(0).getChildNodes().item(0).getNodeValue();
                if (element.getElementsByTagName("message").item(0).hasAttributes()) {
                    this.messageColour = element.getElementsByTagName("message").item(0).getAttributes().getNamedItem("colour").getNodeValue();
                }
            }
            if (element.getElementsByTagName("information").item(0)!=null) {
                this.infoText = element.getElementsByTagName("information").item(0).getChildNodes().item(0).getNodeValue();
                if (element.getElementsByTagName("information").item(0).hasAttributes()) {
                    this.infoColor = element.getElementsByTagName("information").item(0).getAttributes().getNamedItem("colour").getNodeValue();
                }
            }
            if (element.getElementsByTagName("picture").item(0)!=null&&element.getElementsByTagName("picture").item(0).hasAttributes()) {
                if (element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url") != null) {
                    this.pictureUrl = element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url").getNodeValue();
                }
                if (element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("data") != null) {
                    this.pictureData = element.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("data").getNodeValue();
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> billboardProps() {
        HashMap<String,String> billboardProps = new HashMap<String,String>();
        billboardProps.put("background",this.billboardBackground);
        return billboardProps;
    }

    public HashMap<String, String> messageProps() {
        HashMap<String,String> messageProps = new HashMap<String,String>();
        messageProps.put("text",this.messageText);
        messageProps.put("colour",this.messageColour);
        return messageProps;
    }

    public HashMap<String, String> informationProps() {
        HashMap<String,String> infoProps = new HashMap<String,String>();
        infoProps.put("text",this.infoText);
        infoProps.put("colour",this.infoColor);
        return infoProps;
    }

    public HashMap<String, String> pictureProps() {
        HashMap<String,String> pictureProps = new HashMap<String,String>();
        pictureProps.put("url",this.pictureUrl);
        pictureProps.put("data",this.pictureData);
        return pictureProps;
    }

    public void setBillboardName(String newBillboardName) {
        this.billboardName=newBillboardName;
    }

    public String getBillboardName() {
        return this.billboardName;
    }

    public void changeProperties(String element, String prop, String newValue) {
        switch (element){
            case "billboard":
                setBillboardBackground(newValue);
                break;
            case "info":
                switch (prop){
                    case "text":
                        setInfoText(newValue);
                        break;
                    case "colour":
                        setInfoColor(newValue);
                }
                break;
            case "message":
                switch (prop){
                    case "text":
                        setMessageText(newValue);
                        break;
                    case "colour":
                        setMessageColour(newValue);
                        break;
                }
                break;
            case "picture":
                switch (prop){
                    case "data":
                        setPictureData(newValue);
                        break;
                    case "url":
                        setPictureUrl(newValue);
                        break;
                }
                break;
        }
    }

    private void setBillboardBackground(String newBillboardBackground){
        this.billboardBackground = newBillboardBackground;
    }

    private void setMessageText(String newMessageText){
        this.messageText = newMessageText;
    }

    private void setMessageColour(String newMessageColour){
        this.messageColour = newMessageColour;
    }

    private void setInfoText(String newInfoText){
        this.infoText = newInfoText;
    }

    private void setPictureUrl(String newPictureUrl){
        this.pictureUrl = newPictureUrl;
    }

    private void setPictureData(String newPictureData){
        this.pictureData = newPictureData;
    }

    private void setInfoColor(String newInfoColour){
        this.infoColor = newInfoColour;
    }

    public String getBillboardBackground(){
        return billboardBackground;
    }

    public String getMessageText(){
        return messageText;
    }

    public String getMessageColour(){
        return messageColour;
    }

    public String getInfoText(){
        return infoText;
    }

    public String getInfoColor(){
        return infoColor;
    }

    public String getPictureUrl(){
        return pictureUrl;
    }

    public String getPictureData(){
        return  pictureData;
    }
}
