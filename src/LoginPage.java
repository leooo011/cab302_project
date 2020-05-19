import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginPage extends JFrame implements ActionListener {
    private JPanel userNamePanel;
    private JPanel passwordPanel;
    private JLabel userNameLabel, passwordLabel, loginLabel, errorLabel;
    private JTextField userName_text;
    private JPasswordField password_text;
    private JButton submit;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private static String sessionToken;
    private static ArrayList<String> permissions = new ArrayList<>();
    private static String userName;
    public LoginPage () {
        super("Login");
        setSize(460, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(20);
        setLayout(null);
        JPanel messagePanel = new JPanel(flowLayout);
        messagePanel.setBounds(120,8,200,120);
        loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Monospaced", Font.BOLD, 30));

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        messagePanel.add(loginLabel);
        messagePanel.add(errorLabel);
        add(messagePanel);
        userNamePanel = new JPanel(new GridLayout(2,1));
        userNamePanel.setBounds(70, 150, 300, 50);
        // Username Label
        userNameLabel = new JLabel("User Name :");
        userName_text = new JTextField();
        userName_text.setPreferredSize(new Dimension(300, 30));

        userNamePanel.add(userNameLabel);
        userNamePanel.add(userName_text);
        add(userNamePanel);

        passwordPanel = new JPanel(new GridLayout(2,1));
        passwordPanel.setBounds(70, 210, 300, 50);
        // Password Label
        passwordLabel = new JLabel("Password  : ");
        password_text = new JPasswordField();
        password_text.setPreferredSize(new Dimension(200, 30));

        passwordPanel.add(passwordLabel);
        passwordPanel.add(password_text);
        add(passwordPanel);
        // Submit
        submit = new JButton("SUBMIT");
        submit.setBounds(180, 310, 80,40);
        add(submit);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Adding the listeners to components..
        submit.addActionListener(this);


        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage();
    }

    public static ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            socket = new Socket();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            userName = userName_text.getText();
            String password = String.valueOf(password_text.getPassword());
            String hashedPassword = hashPassword.hash(password);
            objectOutputStream.writeUTF("Login");
            objectOutputStream.writeUTF(userName);
            objectOutputStream.writeUTF(hashedPassword);
            objectOutputStream.flush();
            String response = objectInputStream.readUTF();
            if (response.equals("Invalid user")) {
                errorLabel.setText("User doesn't exist!");
            } else if (response.equals("Invalid password")) {
                errorLabel.setText("Invalid password!");
            } else if (response.equals("Login success")) {
                dispose();
                new HomePage();
            }
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();

        } catch (IOException | NoSuchAlgorithmException  ex) {
            ex.printStackTrace();
        }

    }

    public static String getSessionToken() {
        return sessionToken;
    }
    public static String getUserName() { return userName; };
}
