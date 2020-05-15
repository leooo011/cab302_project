import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class GetData {
    /*
     * Get all users props
     */
    public static List<User> getAllUser(){
        List<User> users = new ArrayList<User>();
        Connection connection = DBConnection.getInstance();
        String getAllUsersName = "SELECT userName from users";
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getAllUsersName);
            while (rs.next()){
                String userName = rs.getString(1);
                users.add(getUser(userName));
            }
            st.close();
            rs.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return users;
    }

    /*
     * Get user by user name
     */
    public static User getUser(String userName){
        Boolean editUsers = hasEditUsersPermission(userName);
        Boolean editAllBillboard = hasEditAllBillboardPermission(userName);
        Boolean scheduleBillboard = hasScheduleBillboardPermission(userName);
        Boolean createBillboard = hasCreateBillPermission(userName);
        User user = new User(userName,editUsers,editAllBillboard,createBillboard,scheduleBillboard);
        return user;
    }

    /*
     * Get hash password 2nd from db
     * Return null if cant connect to db
     */
    public static String getHashedPassword2nd(String userName){
        String hashedPassword2nd =null;
        Connection connection = DBConnection.getInstance();
        String getUserHashedPassword2nd = String.format("SELECT hashedPassword2nd from users WHERE userName = '%s'",userName);
        try{
            Statement st = connection.createStatement();
            ResultSet rs =  st.executeQuery(getUserHashedPassword2nd);
            hashedPassword2nd =  rs.getString(1);

            st.close();
            rs.close();
            connection.close();
        }catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return hashedPassword2nd;
    }

    /*
     * Get salt from db
     * Return null if cant connect to db
     */
    public static String getSalt(String userName){
        Connection connection = DBConnection.getInstance();
        String getSalt = String.format("SELECT salt from users WHERE userName = '%s'",userName);
        String salt = null;
        try{
            Statement st = connection.createStatement();
            ResultSet rs =  st.executeQuery(getSalt);
            salt =  rs.getString(1);

            st.close();
            rs.close();
            connection.close();
        }catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return salt;
    }

    /*
     * Get edit user permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasEditUsersPermission(String userName){
        Connection connection = DBConnection.getInstance();
        String getEditUsersPermission = String.format("SELECT editUsers from users WHERE userName = '%s'",userName);
        Boolean editUsersPermission = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getEditUsersPermission);
            editUsersPermission = rs.getBoolean(1);

            st.close();
            rs.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return editUsersPermission;
    }

    /*
     * Get edit all billboard permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasEditAllBillboardPermission(String userName){
        Connection connection = DBConnection.getInstance();
        String getEditAllBillboardPermission = String.format("SELECT editAllBillboard from users WHERE userName = '%s'",userName);
        Boolean editAllBillboardPermission = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getEditAllBillboardPermission);
            editAllBillboardPermission = rs.getBoolean(1);

            st.close();
            rs.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return editAllBillboardPermission;
    }

    /*
     * Get create billboard permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasCreateBillPermission(String userName){
        Connection connection = DBConnection.getInstance();
        String getCreateBillboardPermission = String.format("SELECT createBillboard from users WHERE userName = '%s'",userName);
        Boolean editCreateBillboardPermission = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getCreateBillboardPermission);
            editCreateBillboardPermission = rs.getBoolean(1);

            st.close();
            rs.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return editCreateBillboardPermission;
    }

    /*
     * Get schedule billboard permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasScheduleBillboardPermission(String userName){
        Connection connection = DBConnection.getInstance();
        String getScheduleBillboardPermission = String.format("SELECT scheduleBillboard from users WHERE userName = '%s'",userName);
        Boolean editScheduleBillboardPermission = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getScheduleBillboardPermission);
            editScheduleBillboardPermission = rs.getBoolean(1);

            st.close();
            rs.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return editScheduleBillboardPermission;
    }
}
