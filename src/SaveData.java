import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public  class SaveData {
    /*
     * Save new user if user doesn't exits in db
     */
    public static void saveNewUser(User newUser) throws SQLException, NoSuchAlgorithmException {
        //Hash password 2nd times to save to db
        newUser.hashPassword(newUser.getHashedPassword());
        String insertUser = String.format("INSERT INTO users VALUES ('%s','%s','%s','%d','%d','%d','%d');",
                newUser.getUserName(), newUser.getHashedPassword2nd(), newUser.getSalt(),
                newUser.editUsersPermission(),newUser.editAllBillboardPermission(),
                newUser.createBillboardPermission(),newUser.scheduleBillboardPermission());
        Connection connection = DBConnection.getInstance();
        System.out.println(insertUser);
        try {
            Statement st = connection.createStatement();
            st.execute(insertUser);
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Change password with valid permissions
     * Permission:
     *    Set own password: None
     *    Set password of another user: Edit Users
     */
    public static void changeUserPassword(String userName,String editUserName, String newHashedPassword)
    {
        //Hash password 2nd times to save to db
        String hashedPassword2nd = User.hashPassword(GetData.getHashedPassword2nd(userName));
        String updateUser = String.format("UPDATE users SET hashedPassword = '%s' WHERE userName = '%s'",hashedPassword2nd,userName);
        Connection connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(updateUser);
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /*
     * Change edit users permission in db
     */
    public static void changeEditUsersPermission(String userName,Boolean permission)
    {
        Connection connection = DBConnection.getInstance();
        String changeEditUsersPermission = String.format("UPDATE users SET editUsers = '%d' WHERE userName = '%s'",permission,userName);
        try {
            Statement st = connection.createStatement();
            st.execute(changeEditUsersPermission);

            st.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /*
     * Change edit all billboard permission in db
     */
    public static void changeEditAllBillboardPermission(String userName,Boolean permission)
    {
        Connection connection = DBConnection.getInstance();
        String changeEditAllBillboardPermission = String.format("UPDATE users SET editAllBillboard = '%d' WHERE userName = '%s'",permission,userName);
        try {
            Statement st = connection.createStatement();
            st.execute(changeEditAllBillboardPermission);

            st.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /*
     * Change schedule billboard permission in db
     */
    public static void changeScheduleBillboardPermission(String userName,Boolean permission)
    {
        Connection connection = DBConnection.getInstance();
        String changeScheduleBillboardPermission = String.format("UPDATE users SET scheduleBillboard = '%d' WHERE userName = '%s'",permission,userName);
        try {
            Statement st = connection.createStatement();
            st.execute(changeScheduleBillboardPermission);

            st.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /*
     * Change create billboard permission in db
     */
    public static void changeCreateBillboardPermission(String userName,Boolean permission)
    {
        Connection connection = DBConnection.getInstance();
        String changeCreateBillboardPermission = String.format("UPDATE users SET createBillboard = '%d' WHERE userName = '%s'",permission,userName);
        try {
            Statement st = connection.createStatement();
            st.execute(changeCreateBillboardPermission);

            st.close();
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

}
