import javafx.scene.chart.PieChart;

import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    private static String currentUserName;
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        ServerSocket serverViewer = new ServerSocket(1);
        //ServerSocket serverControl = new ServerSocket(2);
        System.out.println("Waitting...");
        for(;;)
        {
            Socket socketViewer = serverViewer.accept();
            //Socket socketControl = serverControl.accept();
            ObjectInputStream oisViewer = new ObjectInputStream(socketViewer.getInputStream());
            //ObjectInputStream oisControl = new ObjectInputStream(socketControl.getInputStream());
            ObjectOutputStream oosViewer = new ObjectOutputStream(socketViewer.getOutputStream());
            //ObjectOutputStream oosControl = new ObjectOutputStream(socketControl.getOutputStream());
            String command = oisViewer.readUTF();
            //Login section
            if(command == "login")
            {
                command = oisViewer.readUTF();
                if(!isUserExist(command)) {
                    oosViewer.writeUTF("Invalid user");
                }else
                {
                    currentUserName = command;
                    command = oisViewer.readUTF();
                    if(!isUserAuthentication(currentUserName,command)){
                        oosViewer.writeUTF("Invalid password");
                    }else {
                        oosViewer.writeUTF("Login success");
                    }
                    oosViewer.flush();
                }
                oosViewer.flush();
            }

            //Create new user section
            if(command == "create") {
                User user = (User) oisViewer.readObject();
                SaveData.saveNewUser(user);
                oosViewer.writeUTF("Save success!");
                oosViewer.flush();
            }

            //oisControl.close();
            oisViewer.close();
            //socketControl.close();
            socketViewer.close();
        }
    }

    /*
     * Check user in db
     */
    private static boolean isUserExist(String userName) throws SQLException {
        List<String> userNames = getUserNames();
        if(userNames.contains(userName)){return true;}
        else {return false;}
    }

    private static List<String> getUserNames()
    {
        List<String> userNames = new ArrayList<String>();
        Connection connection = DBConnection.getInstance();
        String getUserNames = "SELECT * FROM users";
        try
        {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(getUserNames);
            while (resultSet.next())
            {
                userNames.add(resultSet.getString(1));
            }
            st.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return userNames;
    }

    /*
     * compare hashed password in db to hashed password sent by control panel
     */
    private static Boolean isUserAuthentication(String userName, String hashedPassword)
    {
        String salt = GetData.getSalt(userName);
        String hashedPassword2ndDb = GetData.getHashedPassword2nd(userName);
        String hashedPassword2nd = User.hashPassword(hashedPassword,salt.getBytes());
        if(hashedPassword2nd == hashedPassword2ndDb){
            return true;
        }else {
            return false;
        }
    }

    /*
     * Create token for logging success to access tasks
     */
    private static Map<String,String> CreateToken() {
        SecureRandom random = new SecureRandom();
        Map<String,String> token = new HashMap<String,String>();

        //Create random token
        byte[] tokenString = new byte[16];
        random.nextBytes(tokenString);

        //Set token valid time
        Date currentDate = new Date();
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.setTime(currentDate);
        expiryDate.add(Calendar.DATE, 1);// Expired after 1 day

        token.put("token",tokenString.toString());
        token.put("expiryDate",expiryDate.toString());
        return token;
    }

    private static boolean isTokenExpired(Map<String,String> token) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentDate = new Date();
        Date expiryDate = formatter.parse(token.get("expiryDate"));
        if(currentDate.after(expiryDate)){
            return false;
        }else {
            return true;
        }
    }
}
