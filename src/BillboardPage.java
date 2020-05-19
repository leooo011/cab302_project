import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class BillboardPage extends JFrame implements ActionListener, MenuListener {
    private JMenuBar jMenuBar;
    private JMenu homeMenu;
    private JMenu billboardMenu;
    private JMenu userMenu;
    private JMenuItem createBillboard;
    private JMenuItem previewBillboard;
    private JMenuItem editBillboard;
    private JMenuItem deleteBillboard;
    private JMenuItem scheduleBillboard;
    private JMenuItem viewScheduledBillboards;
    private JMenuItem removeBillboardFromSchedule;
    private String [] billboardNames = new String[15];
    private String [] authorNames = new String[15];
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private JScrollPane jScrollPane;
    private JPanel filterPanel;
    private JTable jTable;
    private TableModel model;

    public BillboardPage() {
        //        socket = new Socket();
//        outputStream = socket.getOutputStream();
//        inputStream = socket.getInputStream();
//        objectOutputStream = new ObjectOutputStream(outputStream);
//        objectInputStream = new ObjectInputStream(inputStream);
//        Object o = objectInputStream.readObject();
        jMenuBar = new JMenuBar();
        homeMenu = new JMenu("Home");
        homeMenu.addMenuListener(this);
        billboardMenu = new JMenu("Billboards Management");
        userMenu = new JMenu("Users");
        userMenu.addMenuListener(this);
        createBillboard = new JMenuItem("Create Billboard");
        createBillboard.addActionListener(this);
        previewBillboard = new JMenuItem("Preview Billboard");
        previewBillboard.addActionListener(this);
        editBillboard = new JMenuItem("Edit Billboard");
        editBillboard.addActionListener(this);
        deleteBillboard = new JMenuItem("Delete Billboard");
        deleteBillboard.addActionListener(this);
        scheduleBillboard = new JMenuItem("Schedule Billboard");
        scheduleBillboard.addActionListener(this);
        viewScheduledBillboards = new JMenuItem("View Scheduled Billboards");
        viewScheduledBillboards.addActionListener(this);
        removeBillboardFromSchedule = new JMenuItem("Remove Billboard From Schedule");
        removeBillboardFromSchedule.addActionListener(this);
        ArrayList<String> permissions = LoginPage.getPermissions();
        permissions.add("createBillboards");
        permissions.add("scheduleBillboards");
        permissions.add("editUsers");
        jMenuBar.add(homeMenu);
        jMenuBar.add(billboardMenu);
        billboardMenu.add(previewBillboard);
        if (permissions.contains("createBillboards")) {
           billboardMenu.add(createBillboard);
           billboardMenu.add(editBillboard);
           billboardMenu.add(deleteBillboard);
        }

        if (permissions.contains("scheduleBillboards")) {
            billboardMenu.add(scheduleBillboard);
            billboardMenu.add(viewScheduledBillboards);
            billboardMenu.add(removeBillboardFromSchedule);
        }
        if (permissions.contains("editUsers")) {
            jMenuBar.add(userMenu);
        }

        String[] columnNames = {"BillboardName", "AuthorName"};
        for (int i = 0; i < 15; i++) {
            billboardNames[i] = "H";
            authorNames[i] = "G";
        }
        Object[][] rowData = new String[authorNames.length][2];
        for (int i = 0; i < authorNames.length; i ++) {
            rowData[i][0] = i + billboardNames[i];
            rowData[i][1] = i + authorNames[i];
        }
        setPreferredSize(new Dimension(800, 550));
        setLocation(250, 100);
        setLayout(null);
        model = new DefaultTableModel(rowData, columnNames) {

            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        jTable = new JTable(model);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        jTable.setRowSorter(sorter);
        jTable.setRowHeight(36);
        filterPanel = new JPanel();
        JTextField filterText =  new JTextField(13);
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = filterText.getText();
                if(text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter(text));
                    } catch(PatternSyntaxException pse) {
                        System.out.println("Bad regex pattern");
                    }
                }
            }
        });
        filterPanel.add(filterText);
        filterPanel.add(filterButton);
        filterPanel.setBounds(100, 20, 600, 30 );
        jScrollPane = new JScrollPane(jTable);
        jScrollPane.setBounds(100,60,600,300);
        setJMenuBar(jMenuBar);
        add(filterPanel);
        add(jScrollPane);
        setVisible(true);
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == previewBillboard) {
            String billboardName = JOptionPane.showInputDialog("Input the billboard's name");
        } else if (e.getSource() == createBillboard) {
            dispose();
            new CreateBillboardsPage( "", "", CreateBillboardsPage.getDefaultBackground(), "", CreateBillboardsPage.getDefaultBackground(),
                    "", "", "", CreateBillboardsPage.getDefaultTextColor()
            );
//            socket = new Socket();
//            try {
//                outputStream = socket.getOutputStream();
//                inputStream = socket.getInputStream();
//                objectOutputStream = new ObjectOutputStream(outputStream);
//                objectInputStream = new ObjectInputStream(inputStream);
//                objectOutputStream.writeObject();
//                Object o = objectInputStream.readObject();
//            } catch (IOException | ClassNotFoundException ex) {
//                ex.printStackTrace();
//            }
        } else if (e.getSource() == editBillboard) {
            String billboardName = JOptionPane.showInputDialog("Input the billboard's name");
        } else if (e.getSource() == deleteBillboard) {
            String billboardName = JOptionPane.showInputDialog("Input the billboard's name");
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new BillboardPage();
    }

    @Override
    public void menuSelected(MenuEvent e) {
        if (e.getSource() == homeMenu) {
            dispose();
            new HomePage();
        } else if (e.getSource() == userMenu) {
            dispose();

        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
