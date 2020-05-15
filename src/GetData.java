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

    /*
     * Get list of billboard by user name
     */
    public static List<Billboard> getBillboardsByUserName(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getBillboard = String.format("SELECT * from billboard WHERE userName = '%s'",userName);
        List<Billboard> billboards = new ArrayList<Billboard>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getBillboard);
        while (rs.next()){
            billboards.add(getBillboard(userName,rs.getString(2)));
        }

        st.close();
        rs.close();
        connection.close();
        return billboards;
    }

    /*
     * Get billboard props in db
     */
    public static Billboard getBillboard(String userName,String billboardName) throws SQLException {
        String billboardBackground,messageText,messageColour,infoText,infoColor,pictureUrl, pictureData;
        String getBillboardData = String.format("SELECT * from billboard WHERE userName = '%s' AND billboardName = '%s'",userName,billboardName);
        Connection connection = DBConnection.getInstance();

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getBillboardData);
        billboardBackground = rs.getString(3);
        messageText = rs.getString(4);
        messageColour = rs.getString(5);
        infoText = rs.getString(6);
        infoColor = rs.getString(7);
        pictureUrl = rs.getString(8);
        pictureData = rs.getBlob(9).toString();
        Billboard billboard = new Billboard(userName,billboardName);
        billboard.changeProperties("billboard","background",billboardBackground);
        billboard.changeProperties("info","text",infoText);
        billboard.changeProperties("info","colour",infoColor);
        billboard.changeProperties("message","text",messageText);
        billboard.changeProperties("message","colour",messageColour);
        billboard.changeProperties("picture","url",pictureUrl);
        billboard.changeProperties("picture","pictureData",pictureData);

        st.close();
        rs.close();
        connection.close();
        return billboard;
    }

    /*
     * Get all billboard available in db
     */
    public static List<Billboard> getAllBillboards() throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getBillboard = "SELECT * from billboard";
        List<Billboard> billboards = new ArrayList<Billboard>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getBillboard);
        String userName,billboardName;
        while (rs.next()){
            userName = rs.getString(1);
            billboardName = rs.getString(2);
            Billboard billboard = getBillboard(userName,billboardName);
            billboards.add(billboard);
        }
        st.close();
        rs.close();
        connection.close();
        return billboards;
    }

    /*
     *Get suitable schedule of all billboard to display
     */
    public static ArrayList<Schedule> getSchedulesList() throws SQLException, ParseException {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        Date date,time,duration,recurTime;
        String userName, billboardName;
        Connection connection = DBConnection.getInstance();
        SimpleDateFormat dateFormatter = new  SimpleDateFormat("yyyy-mm-dd");
        String getSuitableSchedule = String.format("SELECT* from schedule WHERE date = '%s'",dateFormatter.parse((new Date()).toString()),dateFormatter.parse(new Date().toString()));
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getSuitableSchedule);

        while (rs.next()){
            userName = rs.getString(1);
            billboardName = rs.getString(2);
            time = rs.getTime(3);
            date = rs.getDate(4);
            duration = rs.getDate(5);
            recurTime = rs.getDate(6);
            Schedule schedule = new Schedule(getUser(userName),getBillboard(userName,billboardName),date,time,recurTime,duration);
            schedules.add(schedule);
        }

        st.close();
        rs.close();
        connection.close();
        return schedules;
    }
}
