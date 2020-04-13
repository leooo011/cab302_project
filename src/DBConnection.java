import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    /**
     * The singleton instance of the database connection.
     */
    private static Connection instance = null;

    /**
     * Constructor intializes the connection.
     */
    private DBConnection()
    {
        Properties properties = new Properties();
        try
        {
            //File input stream to load properties
            FileInputStream fileInputStream = new  FileInputStream("./dp.props");
            properties.load(fileInputStream);
            fileInputStream.close();

            //Load data from source
            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jbdc.username");
            String password = properties.getProperty("jbdc.password");
            String schema = properties.getProperty("jbdc.schema");

            //Connect to db
            instance = DriverManager.getConnection(url+"/"+schema,username,password);
        }
        catch (SQLException sqle)
        {
            System.err.println(sqle);
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println(fnfe);
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    /**
     * Provides global access to the singleton instance of the UrlSet.
     *
     * @return a handle to the singleton instance of the UrlSet.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DBConnection();
        }
        return instance;
    }
}
