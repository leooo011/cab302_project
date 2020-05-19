import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class CreateBillboardsPage extends JFrame implements ActionListener, FocusListener {
    private Billboard billboard;
    private CreateBillboardsRequest createBillboardsRequest;
    private String billboardName;
    private String billboardAuthorName = "";
    private static Color defaultBackground = Color.WHITE;
    private static Color defaultTextColor = Color.BLACK;
    private Color billboardBackground;
    private String billboardMessageText;
    private Color billboardMessageColour;
    private String billboardInfoText;
    private Color billboardInfoColor;
    private String billboardPictureUrl;
    private String billboardPictureData;
    private String sessionToken;

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private JPanel importExportPanel;
    private JButton importButton;
    private JButton exportButton;

    private JPanel nameBackgroundPanel;
    private JLabel billboardNameLabel;
    private JTextField nameText;
    private JLabel backgroundLabel;
    private JButton backgroundButton;

    private JScrollPane messageScrollPane;
    private JPanel messagePanel;
    private JLabel messageLabel;
    private JTextArea messageText;
    private JLabel messageTextColorLabel;
    private JButton messageTextColorButton;

    private JScrollPane pictureURLScrollPane;
    private JLabel pictureLabel;
    private JTextArea pictureURLText;
    private static String hintText;
    private JLabel pictureDivision;
    private JButton pictureButton;

    private JScrollPane inforScrollPane;
    private JPanel inforPanel;
    private JLabel inforLabel;
    private JTextArea inforText;
    private JLabel inforTextColorLabel;
    private JButton inforTextColorButton;

    private JPanel viewSubmitPanel;
    private JButton previewButton;
    private JButton uploadButton;
    private JPanel bigPanel;
    public CreateBillboardsPage (String billboardAuthorName, String billboardName,
                                 Color billboardBackground, String billboardMessageText,
                                 Color billboardMessageColour, String billboardPictureUrl,
                                 String billboardPictureData, String billboardInfoText,
                                 Color billboardInfoColor
                                 ) {
        this.billboardAuthorName = billboardAuthorName;
        this.billboardName = billboardName;
        this.billboardBackground = billboardBackground;
        this.billboardMessageText = billboardMessageText;
        this.billboardMessageColour = billboardMessageColour;
        this.billboardPictureUrl = billboardPictureUrl;
        this.billboardPictureData = billboardPictureData;
        this.billboardInfoText = billboardInfoText;
        this.billboardInfoColor = billboardInfoColor;


        setPreferredSize(new Dimension(800, 550));
        setLocation(250, 100);

        bigPanel = new JPanel();
        bigPanel.setLayout(null);
        importExportPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(80);
        importExportPanel.setLayout(flowLayout);
        importExportPanel.setBounds(125, 20, 600, 50);
        importButton = new JButton("Import a billboard from XML file");
        importButton.addActionListener(this);
        exportButton = new JButton("Export a billboard to XML file");
        exportButton.addActionListener(this);
        importExportPanel.add(importButton);
        importExportPanel.add(exportButton);

        nameBackgroundPanel = new JPanel();
        nameBackgroundPanel.setBounds(57, 90, 700, 30);
        billboardNameLabel = new JLabel("Set Billboard Name: ");
        nameText = new JTextField(15);
        nameText.setText(billboardName);
        nameText.setBorder(BorderFactory.createLineBorder(Color.black));
        backgroundLabel = new JLabel("Set Background Color: ");
        backgroundButton = new JButton();
        backgroundButton.setBackground(billboardBackground);
        backgroundButton.setPreferredSize(new Dimension(30,20));
        backgroundButton.addActionListener(this);
        nameBackgroundPanel.add(billboardNameLabel);
        nameBackgroundPanel.add(nameText);
        nameBackgroundPanel.add(backgroundLabel);
        nameBackgroundPanel.add(backgroundButton);

        messagePanel = new JPanel();
        messagePanel.setBounds(100, 140, 700, 150);
        messageLabel = new JLabel("Set Message Text: ");
        messageText = new JTextArea();
        messageText.setText(billboardMessageText);
        messageText.setBorder(BorderFactory.createLineBorder(Color.black));
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageScrollPane = new JScrollPane(messageText);
        messageScrollPane.setPreferredSize(new Dimension(300, 80));
        messageTextColorLabel = new JLabel("Set Text Color: ");
        messageTextColorButton = new JButton();
        messageTextColorButton.setBackground(billboardMessageColour);
        messageTextColorButton.setPreferredSize(new Dimension(30,20));
        messageTextColorButton.addActionListener(this);
        messagePanel.add(messageLabel);
        messagePanel.add(messageScrollPane);
        messagePanel.add(messageTextColorLabel);
        messagePanel.add(messageTextColorButton);

        JPanel picturePanel = new JPanel();
        picturePanel.setBounds(105, 250, 700, 60);
        pictureLabel = new JLabel("Set Image URL: ");
        pictureURLText = new JTextArea();
        pictureURLText.setLineWrap(true);
        pictureURLText.setText(billboardPictureUrl);
        pictureURLText.setBorder(BorderFactory.createLineBorder(Color.black));
        pictureURLText.addFocusListener(this);
        hintText = "  Input a URL to an image...";
        pictureURLText.setText(hintText);
        pictureURLScrollPane = new JScrollPane(pictureURLText);
        pictureURLScrollPane.setPreferredSize(new Dimension(300,50));
        pictureDivision = new JLabel("OR");
        pictureButton = new JButton("Import an Image");
        pictureButton.addActionListener(this);
        picturePanel.add(pictureLabel);
        picturePanel.add(pictureURLScrollPane);
        picturePanel.add(pictureDivision);
        picturePanel.add(pictureButton);

        inforPanel = new JPanel();
        inforPanel.setBounds(105, 325, 700, 150);
        inforLabel = new JLabel("Set Information Text: ");
        inforText = new JTextArea();
        inforText.setText(billboardInfoText);
        inforText.setBorder(BorderFactory.createLineBorder(Color.black));
        inforText.setLineWrap(true);
        inforText.setWrapStyleWord(true);
        inforScrollPane = new JScrollPane(inforText);
        inforScrollPane.setPreferredSize(new Dimension(300, 80));
        inforTextColorLabel = new JLabel("Set Text color: ");
        inforTextColorButton = new JButton();
        inforTextColorButton.setBackground(billboardInfoColor);
        inforTextColorButton.setPreferredSize(new Dimension(30,20));
        inforTextColorButton.addActionListener(this);
        inforPanel.add(inforLabel);
        inforPanel.add(inforScrollPane);
        inforPanel.add(inforTextColorLabel);
        inforPanel.add(inforTextColorButton);

        viewSubmitPanel = new JPanel();
        viewSubmitPanel.setBounds(60, 455, 700, 30);
        FlowLayout flowLayout2 = new FlowLayout();
        flowLayout2.setHgap(80);
        viewSubmitPanel.setLayout(flowLayout2);
        previewButton = new JButton("Preview Billboard");
        previewButton.addActionListener(this);
        uploadButton = new JButton("Upload Billboard");
        uploadButton.addActionListener(this);
        viewSubmitPanel.add(previewButton);
        viewSubmitPanel.add(uploadButton);

        bigPanel.add(viewSubmitPanel);
        bigPanel.add(inforPanel);
        bigPanel.add(importExportPanel);
        bigPanel.add(picturePanel);
        bigPanel.add(nameBackgroundPanel);
        bigPanel.add(messagePanel);
        add(bigPanel);
        setVisible(true);
        pack();

    }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == importButton) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    XMLFileParser xmlFileParser = new XMLFileParser();
                    xmlFileParser.importXmlFile(file);
                    backgroundButton.setBackground(xmlFileParser.getBillboardBackground());
                    messageText.setText(xmlFileParser.getBillboardMessageText());
                    messageTextColorButton.setBackground(xmlFileParser.getBillboardMessageColour());
                    pictureURLText.setText(xmlFileParser.getBillboardPictureUrl());
                    billboardPictureData = xmlFileParser.getBillboardPictureData();
                    inforText.setText(xmlFileParser.getBillboardInfoText());
                    inforTextColorButton.setBackground(xmlFileParser.getBillboardInfoColor());

                } else if (returnVal == JFileChooser.CANCEL_OPTION) {

                }
            } else if (e.getSource() == exportButton) {
                if (!pictureURLText.getText().isEmpty() && !billboardPictureData.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You cannot set two images in a billboard",
                            "image error", JOptionPane.YES_NO_OPTION);
                } else {
                    JFileChooser chooser = new JFileChooser();
                    int result = chooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            System.out.println(backgroundButton.getBackground());
                            CreateXMLFile createXMLFile = new CreateXMLFile(backgroundButton.getBackground(), messageText.getText(),
                                    messageTextColorButton.getBackground(), pictureURLText.getText(), billboardPictureData,
                                    inforText.getText(), inforTextColorButton.getBackground());
                            String XMLString = createXMLFile.XMLFileCreator();
                            File file = chooser.getSelectedFile();
                            FileWriter writer = new FileWriter(file);
                            writer.write(XMLString);
                            writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (TransformerException ex) {
                            ex.printStackTrace();
                        } catch (ParserConfigurationException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else if (e.getSource() == backgroundButton) {
                Color color = JColorChooser.showDialog(this, "Choose", Color.cyan);
                backgroundButton.setBackground(color);
            } else if (e.getSource() == messageTextColorButton) {
                Color color = JColorChooser.showDialog(this, "Choose", Color.cyan);
                messageTextColorButton.setBackground(color);
                messageText.setForeground(color);
            } else if (e.getSource() == pictureButton) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try (FileInputStream imageInFile = new FileInputStream(file)) {
                        // Reading a Image file from file system
                        byte imageData[] = new byte[(int) file.length()];
                        imageInFile.read(imageData);
                        billboardPictureData = Base64.getEncoder().encodeToString(imageData);
                    } catch (FileNotFoundException fnfe) {
                        System.out.println("Image not found" + e);
                    } catch (IOException ioe) {
                        System.out.println("Exception while reading the Image " + ioe);
                    }
                }
            } else if (e.getSource() == inforTextColorButton) {
                Color color = JColorChooser.showDialog(this, "Choose", Color.cyan);
                inforTextColorButton.setBackground(color);
                inforTextColorButton.setForeground(color);
            } else if (e.getSource() == uploadButton) {
                if (!pictureURLText.getText().isEmpty() && !billboardPictureData.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You cannot set two images in a billboard",
                            "image error", JOptionPane.YES_NO_OPTION);
                } else {
                    try {
                        socket = new Socket();
                        outputStream = socket.getOutputStream();
                        inputStream = socket.getInputStream();
                        objectOutputStream = new ObjectOutputStream(outputStream);
                        objectInputStream = new ObjectInputStream(inputStream);
                        if (billboardAuthorName.isEmpty()) {
                            objectOutputStream.writeUTF("create billboard");
                            //Author = LoginPage.getUserName();
                            //objectOutputStream.writeObject();
                        } else if (!billboardAuthorName.isEmpty()) {
                            if (!billboardBackground.equals(backgroundButton.getBackground())) {
                                objectOutputStream.writeUTF("change billboard background");
                                objectOutputStream.writeUTF(billboardAuthorName);
                                objectOutputStream.writeUTF(billboardName);
                                objectOutputStream.writeUTF(ColorParser.rgbTohex(backgroundButton.getBackground()));
                                objectOutputStream.flush();
                                String response = objectInputStream.readUTF();
                                if (response.equals("save success")) {
                                    objectInputStream.close();
                                    objectOutputStream.close();
                                    socket.close();
                                }
                            }
                            if (!billboardMessageText.equals(messageText.getText())) {
                                socket = new Socket();
                                outputStream = socket.getOutputStream();
                                inputStream = socket.getInputStream();
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectInputStream = new ObjectInputStream(inputStream);
                                objectOutputStream.writeUTF("change message text");
                                objectOutputStream.writeUTF(billboardAuthorName);
                                objectOutputStream.writeUTF(billboardName);
                                objectOutputStream.writeUTF(messageText.getText());
                                objectOutputStream.flush();
                                String response = objectInputStream.readUTF();
                                if (response.equals("save success")) {
                                    objectInputStream.close();
                                    objectOutputStream.close();
                                    socket.close();
                                }
                            }
                            if (!billboardMessageColour.equals(messageTextColorButton.getBackground())) {
                                socket = new Socket();
                                outputStream = socket.getOutputStream();
                                inputStream = socket.getInputStream();
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectInputStream = new ObjectInputStream(inputStream);
                                objectOutputStream.writeUTF("change message colour");
                                objectOutputStream.writeUTF(billboardAuthorName);
                                objectOutputStream.writeUTF(billboardName);
                                objectOutputStream.writeUTF(ColorParser.rgbTohex(messageTextColorButton.getBackground()));
                                objectOutputStream.flush();
                                String response = objectInputStream.readUTF();
                                if (response.equals("save success")) {
                                    objectInputStream.close();
                                    objectOutputStream.close();
                                    socket.close();
                                }
                            }
                            if (!billboardInfoText.equals(inforText.getText())) {
                                socket = new Socket();
                                outputStream = socket.getOutputStream();
                                inputStream = socket.getInputStream();
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectInputStream = new ObjectInputStream(inputStream);
                                objectOutputStream.writeUTF("change info text");
                                objectOutputStream.writeUTF(billboardAuthorName);
                                objectOutputStream.writeUTF(billboardName);
                                objectOutputStream.writeUTF(inforText.getText());
                                objectOutputStream.flush();
                                String response = objectInputStream.readUTF();
                                if (response.equals("save success")) {
                                    objectInputStream.close();
                                    objectOutputStream.close();
                                    socket.close();
                                }
                            }
                            if (!billboardInfoColor.equals(inforTextColorButton.getBackground())) {
                                socket = new Socket();
                                outputStream = socket.getOutputStream();
                                inputStream = socket.getInputStream();
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectInputStream = new ObjectInputStream(inputStream);
                                objectOutputStream.writeUTF("change info colour");
                                objectOutputStream.writeUTF(billboardAuthorName);
                                objectOutputStream.writeUTF(billboardName);
                                objectOutputStream.writeUTF(ColorParser.rgbTohex(inforTextColorButton.getBackground()));
                                objectOutputStream.flush();
                                String response = objectInputStream.readUTF();
                                if (response.equals("save success")) {
                                    objectInputStream.close();
                                    objectOutputStream.close();
                                    socket.close();
                                }
                            }
                            if (!billboardPictureUrl.equals(pictureURLText.getText())) {
                                socket = new Socket();
                                outputStream = socket.getOutputStream();
                                inputStream = socket.getInputStream();
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectInputStream = new ObjectInputStream(inputStream);
                                objectOutputStream.writeUTF("change picture url");
                                objectOutputStream.writeUTF(billboardAuthorName);
                                objectOutputStream.writeUTF(billboardName);
                                objectOutputStream.writeUTF(pictureURLText.getText());
                                objectOutputStream.flush();
                                String response = objectInputStream.readUTF();
                                if (response.equals("save success")) {
                                    objectInputStream.close();
                                    objectOutputStream.close();
                                    socket.close();
                                }
                            }
//                            if (!billboardPictureData.equals(...getText())) {
//                                socket = new Socket();
//                                outputStream = socket.getOutputStream();
//                                inputStream = socket.getInputStream();
//                                objectOutputStream = new ObjectOutputStream(outputStream);
//                                objectInputStream = new ObjectInputStream(inputStream);
//                                objectOutputStream.writeUTF("change picture data");
//                                objectOutputStream.writeUTF(billboardAuthorName);
//                                objectOutputStream.writeUTF(billboardName);
//                                objectOutputStream.writeUTF(...getText());
//                                objectOutputStream.flush();
//                                String response = objectInputStream.readUTF();
//                                if (response.equals("save success")) {
//                                    objectInputStream.close();
//                                    objectOutputStream.close();
//                                    socket.close();
//                                }
                            }
                        if (!billboardName.equals(nameText.getText())) {
                            socket = new Socket();
                            outputStream = socket.getOutputStream();
                            inputStream = socket.getInputStream();
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectInputStream = new ObjectInputStream(inputStream);
                            objectOutputStream.writeUTF("change billboard name");
                            objectOutputStream.writeUTF(billboardAuthorName);
                            objectOutputStream.writeUTF(billboardName);
                            objectOutputStream.writeUTF(nameText.getText());
                            objectOutputStream.flush();
                            String response = objectInputStream.readUTF();
                            if (response.equals("save success")) {
                                objectInputStream.close();
                                objectOutputStream.close();
                                socket.close();
                            }
                        }


                        } catch (IOException ex) {
                        ex.printStackTrace();
                    }
//                    CreateBillboardsRequest cbdr = new CreateBillboardsRequest(LoginPage.getUserName(), billboardNameText.getText(),
//                                backgroundButton.getBackground(), messageText.getText(), messageTextColorButton.getBackground(),
//                                pictureURLText.getText(), billboardPictureData, inforText.getText(), inforTextColorButton.getBackground(),
//                                LoginPage.getSessionToken());
//                        objectOutputStream.writeObject(cbdr);
//                        objectOutputStream.flush();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }

        }
        public static void main(String[] args) {
            new CreateBillboardsPage( "", "", defaultBackground, "", defaultTextColor,
                    "", "", "", defaultTextColor
                    );
        }
        public static String getHintText() {
            return hintText;
        }

        @Override
        public void focusGained(FocusEvent e) {
            String temp = pictureURLText.getText();
            if(temp.equals(hintText)) {
                pictureURLText.setText("");
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            String temp = pictureURLText.getText();
            if(temp.equals("")) {
                pictureURLText.setForeground(Color.GRAY);
                pictureURLText.setText(hintText);
        }


    }


    public static Color getDefaultBackground() {
        return defaultBackground;
    }

    public static Color getDefaultTextColor() {
        return defaultTextColor;
    }


}
