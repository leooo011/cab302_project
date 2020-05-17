import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public  class SaveData {
    /*
     * Save new user if user doesn't exits in db
     */
    public static void saveNewUser(User newUser) throws SQLException  {
        //Hash password 2nd times to save to db
        int editUsersPermission = newUser.editUsersPermission()?1:0;
        int editAllBillboardPermission = newUser.editAllBillboardPermission()?1:0;
        int createBillboardPermission = newUser.createBillboardPermission()?1:0;
        int scheduleBillboardPermission = newUser.scheduleBillboardPermission()?1:0;
        String insertUser = String.format("INSERT INTO users VALUES ('%s','%s','%s','%d','%d','%d','%d');",
                newUser.getUserName(), newUser.getHashedPassword2nd(), newUser.getSalt(),editUsersPermission,
                editAllBillboardPermission,createBillboardPermission,scheduleBillboardPermission);
        Connection connection = DBConnection.getInstance();
        Statement st = connection.createStatement();
        st.execute(insertUser);
        st.close();
    }

    /*
     * Change password with valid permissions
     * Permission:
     *    Set own password: None
     *    Set password of another user: Edit Users
     */
    public static void changeUserPassword(String editUserName, String newHashedPassword) throws SQLException {
        //Hash password 2nd times to save to db
        String salt = GetData.getSalt(editUserName);
        String hashedPassword2nd = User.hashPassword(newHashedPassword,salt.getBytes());
        String updateUser = String.format("UPDATE users SET hashedPassword = '%s' WHERE userName = '%s'",hashedPassword2nd,editUserName);
        Connection connection = DBConnection.getInstance();
        Statement st = connection.createStatement();
        st.execute(updateUser);
        st.close();
    }
    /*
     * Change edit users permission in db
     */
    public static void changeEditUsersPermission(String userName,Boolean permission) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String changeEditUsersPermission = String.format("UPDATE users SET editUsers = '%d' WHERE userName = '%s'",permission,userName);
        Statement st = connection.createStatement();
        st.execute(changeEditUsersPermission);

        st.close();
    }

    /*
     * Change edit all billboard permission in db
     */
    public static void changeEditAllBillboardPermission(String userName,Boolean permission) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String changeEditAllBillboardPermission = String.format("UPDATE users SET editAllBillboard = '%d' WHERE userName = '%s'",permission,userName);
        Statement st = connection.createStatement();
        st.execute(changeEditAllBillboardPermission);

        st.close();
    }

    /*
     * Change schedule billboard permission in db
     */
    public static void changeScheduleBillboardPermission(String userName,Boolean permission) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String changeScheduleBillboardPermission = String.format("UPDATE users SET scheduleBillboard = '%d' WHERE userName = '%s'",permission,userName);
        Statement st = connection.createStatement();
        st.execute(changeScheduleBillboardPermission);

        st.close();
    }

    /*
     * Change create billboard permission in db
     */
    public static void changeCreateBillboardPermission(String userName,Boolean permission) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String changeCreateBillboardPermission = String.format("UPDATE users SET createBillboard = '%d' WHERE userName = '%s'",permission,userName);
        Statement st = connection.createStatement();
        st.execute(changeCreateBillboardPermission);

        st.close();
    }

    /*
     * Delete user in db by user name
     */
    public static void deleteUser(String userName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String deleteUser = String.format("DELETE FROM users WHERE userName = '%s'",userName);
        Statement st = connection.createStatement();
        st.execute(deleteUser);

        st.close();
    }

    /*
     * Save new billboard
     */
    public static void saveBillboard(Billboard billboard) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String saveBillboard = String.format("INSERT INTO billboards VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s');"
                                ,billboard.getAuthorName(),billboard.getBillboardName()
                                ,billboard.getBillboardBackground(),billboard.getMessageText(),billboard.getMessageColour()
                                ,billboard.getInfoText(),billboard.getInfoColor(),billboard.getPictureUrl(),billboard.getPictureData());
        Statement st = connection.createStatement();
        st.execute(saveBillboard);

        st.close();
    }

    /*
     * Delete billboard
     */
    public static void deleteBillboard(String userName,String billboardName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String deleteBillboard = String.format("DELETE from billboards WHERE (userName = '%s' AND billboardName = '%s'",userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(deleteBillboard);

        st.close();
    }

    /*
     * Update billboard Name in db in billboards table as well as schedules tables
     */
    public static void updateBillboardName(String userName, String billboardName, String newBillboardName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboardInBillboards = String.format("UPDATE billboards SET billboardName = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newBillboardName,userName,billboardName);
        String updateBillboardInSchedules = String.format("UPDATE schedules SET billboardName = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newBillboardName,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboardInBillboards);
        st.execute(updateBillboardInSchedules);

        st.close();
        updateScheduleBillboardName(userName,billboardName,newBillboardName);
    }

    /*
     * Update billboard background in db
     */
    public static void updateBillboardBackground(String userName, String billboardName, String newBackground) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard = String.format("UPDATE billboards SET billboardBackground = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newBackground,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Update billboard message text in db
     */
    public static void updateBillboardMessageText(String userName, String billboardName, String newMessageText) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard = String.format("UPDATE billboards SET messageText = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newMessageText,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Update billboard message colour in db
     */
    public static void updateBillboardMessageColour(String userName, String billboardName, String newMessageColour) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard= String.format("UPDATE billboards SET messageColour = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newMessageColour,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Update billboard info text in db
     */
    public static void updateBillboardInfoText(String userName, String billboardName, String newInfoText) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard = String.format("UPDATE billboards SET infoText = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newInfoText,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Update billboard info colour in db
     */
    public static void updateBillboardInfoColour(String userName, String billboardName, String newInfoColour) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard = String.format("UPDATE billboards SET infoColor = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newInfoColour,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Update billboard picture url in db
     */
    public static void updateBillboardPictureUrl(String userName, String billboardName, String newUrl) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard = String.format("UPDATE billboards SET pictureUrl = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newUrl,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Update billboard picture data in db
     */
    public static void updateBillboardPictureData(String userName, String billboardName, String newData) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateBillboard = String.format("UPDATE billboards SET pictureData = '%s' (WHERE userName = '%s' AND billboardName = '%s'",newData,userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(updateBillboard);

        st.close();
    }

    /*
     * Save new schedule
     */
    public static void saveSchedule(Schedule schedule) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String saveSchedule = String.format("INSERT INTO schedules VALUES ('%s','%s','%s','%s','%s','%s')",schedule.getAuthor(),schedule.getBillboardName(),Server.parseStringTime(schedule.getTime()),Server.parseStringDate(schedule.getDate()),Server.parseStringTime(schedule.getDuration()),Server.parseStringTime(schedule.getRecurTime()));
        Statement st = connection.createStatement();
        st.execute(saveSchedule);

        st.close();
    }

    /*
     * Delete schedule
     */
    public static void deleteSchedule(String userName, String billboardName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String deleteSchedule = String.format("DELETE from schedules (WHERE userName = '%s' AND billboardName = '%s'",userName,billboardName);
        Statement st = connection.createStatement();
        st.execute(deleteSchedule);

        st.close();
    }

    /*
     * Update schedule name
     */
    public static void updateScheduleBillboardName(String userName, String billboardName, String newBillboardName) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateSchedule = String.format("UPDATE schedules SET billboardName = '%s'",newBillboardName);
        Statement st = connection.createStatement();
        st.execute(updateSchedule);

        st.close();
    }

    /*
     * Update schedule date
     */
    public static void updateScheduleDate(String userName, String billboardName, String newDate) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateSchedule = String.format("UPDATE schedules SET date = '%s'",newDate);
        Statement st = connection.createStatement();
        st.execute(updateSchedule);

        st.close();
    }

    /*
     * Update schedule time
     */
    public static void updateScheduleTime(String userName, String billboardName, String newTime) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateSchedule = String.format("UPDATE schedules SET time = '%s'",newTime);
        Statement st = connection.createStatement();
        st.execute(updateSchedule);

        st.close();
    }

    /*
     * Update schedule duration
     */
    public static void updateScheduleDuration(String userName, String billboardName, String newDuration) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateSchedule = String.format("UPDATE schedules SET duration = '%s'",newDuration);
        Statement st = connection.createStatement();
        st.execute(updateSchedule);

        st.close();
    }

    /*
     * Update schedule recur time
     */
    public static void updateScheduleRecurTime(String userName, String billboardName, String newRecurTime) throws SQLException {
        Connection connection = DBConnection.getInstance();
        String updateSchedule = String.format("UPDATE schedules SET recurTime = '%s'",newRecurTime);
        Statement st = connection.createStatement();
        st.execute(updateSchedule);

        st.close();
    }
}
