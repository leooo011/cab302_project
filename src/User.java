import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class User implements Serializable {
    private String userName;
    // hashedPassword will send through control panel
    private String hashedPassword;
    // hasdPassword2nd will save to db
    private String hashedPassword2nd;
    private static byte[] salt = new byte[16];
    private Boolean editUsers;
    private Boolean editAllBillboard;
    private Boolean createBillboard;
    private Boolean scheduleBillboard;

    //Create user from control panel
    public User(String userName,String hashedPassword, Boolean editUsers,Boolean editAllBillboard,Boolean createBillboard,Boolean scheduleBillboard) throws NoSuchAlgorithmException {
        this.editUsers = editUsers;
        this.editAllBillboard = editAllBillboard;
        this.createBillboard = createBillboard;
        this.scheduleBillboard = scheduleBillboard;
        this.userName = userName;
        this.hashedPassword = hashedPassword;
    }

    //Create user from db
    public User(String userName, Boolean editUsers,Boolean editAllBillboard,Boolean createBillboard,Boolean scheduleBillboard)
    {
        this.editUsers = editUsers;
        this.editAllBillboard = editAllBillboard;
        this.createBillboard = createBillboard;
        this.scheduleBillboard = scheduleBillboard;
        this.userName = userName;
    }
    public String getUserName(){
        return userName;
    }
    public String getHashedPassword(){return hashedPassword;}
    public String getHashedPassword2nd() {
        hashedPassword2nd = hashPassword(hashedPassword);
        return hashedPassword2nd.toString(); }
    public String getSalt(){
        return salt.toString();
    }
    public void changeUserName(String newUserName){
            userName = newUserName;
    }
    public void changePassword(String newHashedPassword)
    {
            hashedPassword = newHashedPassword;
    }

    /*
     *Hash password using SHA-256 algorithm with random salt
     */
    public static String hashPassword(String hashedPassword)
    {
        SecureRandom random =new SecureRandom();
        random.nextBytes(salt);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.toString().getBytes());
            byte[] hash = md.digest(hashedPassword.getBytes());
            StringBuffer sb = new StringBuffer();
            for(byte b:hash){
                sb.append(String.format("%2x",b&0xFF));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /*
     *Hash password using SHA-256 algorithm with given salt
     */
    public static String hashPassword(String hashedPassword, byte[] salt)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(hashedPassword.getBytes());
            StringBuffer sb = new StringBuffer();
            for(byte b:hash){
                sb.append(String.format("%2x",b&0xFF));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    public boolean editUsersPermission()
    {
        return editUsers;
    }
    public boolean editAllBillboardPermission() { return editAllBillboard; }
    public boolean createBillboardPermission()
    {
        return createBillboard;
    }
    public boolean scheduleBillboardPermission()
    {
        return scheduleBillboard;
    }
}
