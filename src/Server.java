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
    private static HashMap<String,String> token;
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        ServerSocket serverSocket = new ServerSocket(1);
        System.out.println("Waiting...");
        for(;;) {
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String command = ois.readUTF();
            if(command.equals("createUser")){
                User user = (User) ois.readObject();
                try {
                    SaveData.saveNewUser(user);
                    oos.writeUTF("Save user success!");
                } catch (SQLException ex) {
                    if(ex.getErrorCode() == 1062 ) {
                        oos.writeUTF("User already exist!");
                    }else
                    {
                        oos.writeUTF("Something wrong with database");
                        ex.printStackTrace();
                    }
                }
                oos.flush();
            }
            else if(command.equals("createBillboard")){
                Billboard billboard = (Billboard) ois.readObject();
                try{
                    SaveData.saveBillboard(billboard);
                    oos.writeUTF("Save billboard success!");
                }catch (SQLException ex){
                    oos.writeUTF("Fail to save billboard!");
                    ex.printStackTrace();
                }
                oos.flush();
            }
            else if(command.equals("createSchedule")){
                Schedule schedule = (Schedule) ois.readObject();
                try{
                    SaveData.saveSchedule(schedule);
                    oos.writeUTF("Save schedule success!");
                }catch (SQLException ex){
                    oos.writeUTF("Fail to save schedule!");
                    ex.printStackTrace();
                }
                oos.flush();
            }
            else if(command.equals("login")){
                String userName = ois.readUTF();
                String pass = ois.readUTF();
                if (!isUserExist(userName)) {
                    oos.writeUTF("Invalid user");
                } else {
                    currentUserName = userName;
                    if (!isUserAuthentication(currentUserName, pass)) {
                        oos.writeUTF("Invalid password");
                    } else {
                        oos.writeUTF("Login success");
                        token = CreateToken();
                        oos.writeUTF(token.get("token"));
                    }
                }
                oos.flush();
            }
            ois.close();
            oos.close();
            socket.close();
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

    private static List<String> getUserNames() throws SQLException {
        List<String> userNames = new ArrayList<String>();
        Connection connection = DBConnection.getInstance();
        String getUserNames = "SELECT * FROM users;";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getUserNames);
        while (rs.next())
        {
            userNames.add(rs.getString(1));
        }
        st.close();
        rs.close();
        return userNames;
    }

    /*
     * compare hashed password in db to hashed password sent by control panel
     */
    private static Boolean isUserAuthentication(String userName, String hashedPassword) throws SQLException {
        String salt = GetData.getSalt(userName);
        String hashedPassword2ndDb = GetData.getHashedPassword2nd(userName);
        String hashedPassword2nd = User.hashPassword(hashedPassword,salt.getBytes());
        System.out.println(hashedPassword2ndDb);
        System.out.println(hashedPassword2nd);
        if(hashedPassword2nd.equals(hashedPassword2ndDb)){
            return true;
        }else {
            return false;
        }
    }

    /*
     * Create token for logging success to access tasks
     */
    private static HashMap<String,String> CreateToken() {
        SecureRandom random = new SecureRandom();
        HashMap<String,String> token = new HashMap<String,String>();

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

    private static boolean isTokenExpired(HashMap<String,String> token) throws ParseException {
        Date currentDate = new Date();
        Date expiryDate = parseDateTime(token.get("expiryDate"));
        if(currentDate.after(expiryDate)){
            return false;
        }else {
            return true;
        }
    }
    Date date = new Date();

    public static String parseStringDate(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        }
        return null;
    }

    public static String parseStringTime(Date time) {
        if (time != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            return formatter.format(time);
        }
        return null;
    }
    public static String parseStringDateTime(Date dateTime) {
        if (dateTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(dateTime);
        }
        return null;
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseTime(String date) {
        try {
            return new SimpleDateFormat("HH:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateTime(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
