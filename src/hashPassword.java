import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class hashPassword {

    public static String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        StringBuffer stringBuffer = new StringBuffer();
        byte[] hashedBytePassword = md.digest(password.getBytes());
        for (byte b : hashedBytePassword) {
            stringBuffer.append(String.format("%2x", b & 0xFF));
        }
        return stringBuffer.toString();
    }
}
