import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;


public class HomePage extends JFrame implements MenuListener {
    private JMenuBar jMenuBar;
    private JMenu homeMenu;
    private JMenu billboardMenu;
    private JMenu userMenu;
    private JMenuItem createBillboard;
    private JMenuItem editBillboard;
    private JMenuItem removeBillboard;
    private JMenuItem scheduleBillboard;
    private JMenuItem removeBillboardFromSchedule;
    private JMenuItem viewAllUsers;
    private JMenuItem createUser;
    private JPanel functionMenu;
    private JButton createBillboardsButton;
    private JButton listAllBillboardsButton;
    private JButton scheduleBillboardsButton;
    private JButton listAllUsersButton;
    private JButton createUsersButton;
    private JLabel billboardLabel;
    private JLabel userLabel;
    private HashMap<String, String> allBillboards;
    private ArrayList<String> allUserNames;
    public HomePage () {
        super("Home");
        setPreferredSize(new Dimension(800, 550));
        setLocation(250, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jMenuBar = new JMenuBar();
        homeMenu = new JMenu("Home");
        homeMenu.addMenuListener(this);
        billboardMenu = new JMenu("Billboards");
        billboardMenu.addMenuListener(this);
        userMenu = new JMenu("Users");
        userMenu.addMenuListener(this);
        ArrayList<String> permissions = LoginPage.getPermissions();
        permissions.add("createBillboards");
        permissions.add("scheduleBillboards");
        permissions.add("editUsers");
        jMenuBar.add(homeMenu);
        jMenuBar.add(billboardMenu);
        if (permissions.contains("editUsers")) {
            jMenuBar.add(userMenu);
        }
        setJMenuBar(jMenuBar);

        pack();
        setVisible(true);
    }

    @Override
    public void menuSelected(MenuEvent e) {
        if (e.getSource() == homeMenu) {
            dispose();
            new HomePage();
        } else if (e.getSource() == billboardMenu) {
            dispose();
            new BillboardPage();
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
