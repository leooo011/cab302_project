import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GetData {
    /*
     * Get all users props
     */
    public static ArrayList<User> getAllUser() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        Connection connection = DBConnection.getInstance();
        String getAllUsersName = "SELECT userName from users;";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getAllUsersName);
        while (rs.next()){
            String userName = rs.getString(1);
            users.add(getUser(userName));
        }
        st.close();
        rs.close();
        return users;
    }

    /*
     * Get user by user name
     */
    public static User getUser(String userName) throws SQLException {
        Boolean editUsers = hasEditUsersPermission(userName);
        Boolean editAllBillboard = hasEditAllBillboardPermission(userName);
        Boolean scheduleBillboard = hasScheduleBillboardPermission(userName);
        Boolean createBillboard = hasCreateBillboardPermission(userName);
        User user = new User(userName,editUsers,editAllBillboard,createBillboard,scheduleBillboard);
        return user;
    }

    /*
     * Get hash password 2nd from db
     * Return null if cant connect to db
     */
    public static String getHashedPassword2nd(String userName) throws SQLException {
        String hashedPassword2nd =null;
        Connection connection = DBConnection.getInstance();
        String getUserHashedPassword2nd = String.format("SELECT hashedPassword2nd from users WHERE userName = '%s';",userName);
        Statement st = connection.createStatement();
        ResultSet rs =  st.executeQuery(getUserHashedPassword2nd);
        if(rs.next()) {
            hashedPassword2nd = rs.getString(1);
        }
        st.close();
        rs.close();
        return hashedPassword2nd;
    }

    /*
     * Get salt from db
     * Return null if cant connect to db
     */
    public static String getSalt(String userName) throws SQLException {
        String getSalt = String.format("SELECT salt from users WHERE userName = '%s';",userName);
        String salt = null;
        Connection connection = DBConnection.getInstance();
        Statement st = connection.createStatement();
        ResultSet rs =  st.executeQuery(getSalt);
        if(rs.next()) {
            salt = rs.getString(1);
        }
        st.close();
        rs.close();
        return salt;
    }

    /*
     * Get edit user permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasEditUsersPermission(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getEditUsersPermission = String.format("SELECT editUsers from users WHERE userName = '%s'",userName);
        Boolean editUsersPermission = null;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getEditUsersPermission);
        if (rs.next()) {
            editUsersPermission = rs.getBoolean(1);
        }

        st.close();
        rs.close();
        return editUsersPermission;
    }

    /*
     * Get edit all billboard permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasEditAllBillboardPermission(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getEditAllBillboardPermission = String.format("SELECT editAllBillboard from users WHERE userName = '%s'",userName);
        Boolean editAllBillboardPermission = null;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getEditAllBillboardPermission);
        if(rs.next()) {
            editAllBillboardPermission = rs.getBoolean(1);
        }

        st.close();
        rs.close();
        return editAllBillboardPermission;
    }

    /*
     * Get create billboard permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasCreateBillboardPermission(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getCreateBillboardPermission = String.format("SELECT createBillboard from users WHERE userName = '%s'",userName);
        Boolean editCreateBillboardPermission = null;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getCreateBillboardPermission);
        if(rs.next()) {
            editCreateBillboardPermission = rs.getBoolean(1);
        }

        st.close();
        rs.close();
        return editCreateBillboardPermission;
    }

    /*
     * Get schedule billboard permission from fb
     * Return null if cant connect to db
     */
    public static Boolean hasScheduleBillboardPermission(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getScheduleBillboardPermission = String.format("SELECT scheduleBillboard from users WHERE userName = '%s'",userName);
        Boolean editScheduleBillboardPermission = null;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getScheduleBillboardPermission);
        if(rs.next()) {
            editScheduleBillboardPermission = rs.getBoolean(1);
        }

        st.close();
        rs.close();
        connection.close();
        return editScheduleBillboardPermission;
    }

    /*
     * Get list of billboard by user name
     */
    public static ArrayList<Billboard> getBillboardsByUserName(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getBillboard = String.format("SELECT * from billboards WHERE userName = '%s'",userName);
        ArrayList<Billboard> billboards = new ArrayList<Billboard>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getBillboard);
        while (rs.next()){
            billboards.add(getBillboard(userName,rs.getString(2)));
        }

        st.close();
        rs.close();
        return billboards;
    }

    /*
     * Get billboard props in db
     */
    public static Billboard getBillboard(String userName,String billboardName) throws SQLException {
        String billboardBackground,messageText,messageColour,infoText,infoColor,pictureUrl, pictureData;
        String getBillboardData = String.format("SELECT * from billboards WHERE userName = '%s' AND billboardName = '%s'",userName,billboardName);
        Billboard billboard = new Billboard(userName, billboardName);
        Connection connection = DBConnection.getInstance();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getBillboardData);
        if(rs.next()) {
            billboardBackground = rs.getString(3);
            messageText = rs.getString(4);
            messageColour = rs.getString(5);
            infoText = rs.getString(6);
            infoColor = rs.getString(7);
            pictureUrl = rs.getString(8);
            pictureData = rs.getBlob(9).toString();
            billboard.changeProperties("billboard", "background", billboardBackground);
            billboard.changeProperties("info", "text", infoText);
            billboard.changeProperties("info", "colour", infoColor);
            billboard.changeProperties("message", "text", messageText);
            billboard.changeProperties("message", "colour", messageColour);
            billboard.changeProperties("picture", "url", pictureUrl);
            billboard.changeProperties("picture", "pictureData", pictureData);
        }

        st.close();
        rs.close();
        return billboard;
    }

    /*
     * Get all billboard available in db
     */
    public static ArrayList<Billboard> getAllBillboards() throws SQLException {
        Connection connection = DBConnection.getInstance();
        String getBillboard = "SELECT * from billboards";
        ArrayList<Billboard> billboards = new ArrayList<Billboard>();
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
        return billboards;
    }

    /*
     *Get suitable schedule of all billboard to display
     */
    public static ArrayList<Schedule> getTodaySchedules() throws SQLException, ParseException {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        Date date,time,duration,recurTime;
        String userName, billboardName;
        Connection connection = DBConnection.getInstance();
        String getSuitableSchedule = String.format("SELECT* from schedules WHERE date = '%s';",Server.parseStringDate(new Date()));
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getSuitableSchedule);

        while (rs.next()){
            userName = rs.getString(1);
            billboardName = rs.getString(2);
            time = rs.getTime(3);
            date = rs.getDate(4);
            duration = rs.getTime(5);
            recurTime = rs.getDate(6);
            Schedule schedule = new Schedule(getUser(userName),getBillboard(userName,billboardName),date,time,recurTime,duration);
            schedules.add(schedule);
        }

        st.close();
        rs.close();
        return schedules;
    }

    /*
     *Get all schedules
     */
    public static ArrayList<Schedule> getAllSchedules() throws SQLException {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        Date date,time,duration,recurTime;
        String userName, billboardName;
        Connection connection = DBConnection.getInstance();
        String getSuitableSchedule = String.format("SELECT* from schedules ");
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getSuitableSchedule);

        while (rs.next()){
            userName = rs.getString(1);
            billboardName = rs.getString(2);
            time = rs.getTime(3);
            date = rs.getDate(4);
            duration = rs.getTime(5);
            recurTime = rs.getDate(6);
            Schedule schedule = new Schedule(getUser(userName),getBillboard(userName,billboardName),date,time,recurTime,duration);
            schedules.add(schedule);
        }

        st.close();
        rs.close();
        return schedules;
    }
}
