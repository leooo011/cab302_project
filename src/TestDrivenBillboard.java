import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//Test driven for billboard class
class TestDrivenBillboard {
    /* Test 0: Declaring Billboard objects
     */
    Billboard billboard = new Billboard("userName","billboardName");

    /*Test 1: Constructing a Billboard object
     */
    @BeforeEach
    @Test
    public void setupBillboard() {
        billboard = new Billboard("userName","billboardName");
    }

    /*Test 2: Get author Name
     */
    @Test
    public void getAuthorName() {
        assertEquals("userName",billboard.getAuthorName());
    }

    /*Test 3: Get properties of billboard tag
     */
    @Test
    public void getBillboardProperties() {
        File file = new File("./13.xml");
        billboard.importXmlFile(file);
        Map<String, String> billboardProps = new HashMap<String, String>();
        billboardProps = billboard.billboardProps();
        assertEquals("#555555", billboardProps.get("background"));
    }

    /*Test 4: Get properties of message tag
     */
    @Test
    public void getMessageProperties() {
        File file = new File("./13.xml");
        billboard.importXmlFile(file);
        Map<String, String> messageProps = new HashMap<String, String>();
        messageProps = billboard.messageProps();
        assertEquals("The information text is always smaller than the message text", messageProps.get("text"));
        assertEquals("#FFFFFF", messageProps.get("colour"));
    }

    /*Test 5: Get properties of information tag
     */
    @Test
    public void getInformationProperties() {
        File file = new File("./13.xml");
        billboard.importXmlFile(file);
        Map<String, String> informationProps = new HashMap<String, String>();
        informationProps = billboard.informationProps();
        assertEquals("The information text is always smaller than the message text", informationProps.get("text"));
        assertEquals("#DDDDDD", informationProps.get("colour"));
    }

    /*Test 6: Get properties of picture tag
     */
    @Test
    public void getPictureProperties() {
        File file = new File("./4.xml");
        billboard.importXmlFile(file);
        Map<String, String> pictureProps = new HashMap<String, String>();
        pictureProps = billboard.pirctureProps();
        assertEquals(null, pictureProps.get("url"));
        assertEquals("iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwOTowMjoxNyArMDkwMHlQ1XMAAAAHdElNRQfkAxkAAyQ8nibjAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAS5JREFUeNq1kb9KxEAQxmcgcGhhJ4cnFwP6CIIiPoZwD+ALXGFxj6BgYeU7BO4tToSDFHYWZxFipeksbMf5s26WnAkJki2+/c03OzPZDRJNYcgVwfsU42cmKi5YjS1s4p4DCrkBPc0wTlkdX6bsG4hZQOj3HRDLHqh08U4Adb/zgEMtq5RuH3Axd45PbftdB2wO5OsWc7pOYaOeOk63wYfdFtL5qldB34W094ZfJ+4RlFldTrmW/ZNbn2g0of1vLHdZq77qSDCaSAsLf9kXh9w44PNoR/YSPHycEmbIOs5QzBJsmDHrWLPeF24ZkCe6ZxDCOqHcmxmsr+hsicahss+n8vYb8NHZPTJxi/RGC5IqbRwqH6uxVTX+5LvHtvT/V/R6PGh/iF4GHoBAwz7RD26spwq6Amh/AAAAAElFTkSuQmCC", pictureProps.get("data"));
    }

    /*Test 7: Get billboard Name
     */
    @Test
    public void getBillboardName(){
        assertEquals("billboardName",billboard.getBillboardName());
    }

    /*Test 8: Change billboard Name
     */
    @Test
    public void changeBillboardName(){
        billboard.setBillboardName("Billboard1");
        assertEquals("Billboard1",billboard.getBillboardName());
    }

    /*Test 9: Set properties
     */
    public void setProperties(){
        billboard.changeProperties("picture","url","https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download");
        Map<String, String> pictureProps = new HashMap<String, String>();
        pictureProps = billboard.pirctureProps();
        assertEquals("https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download", pictureProps.get("url"));
        assertEquals(null, pictureProps.get("data"));

    }
}