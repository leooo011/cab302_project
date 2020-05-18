import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static String currentBillboardName="";
    private static HashMap<String,String> token;
    private static ArrayList<Schedule> schedules;
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException {
        ServerSocket serverSocket = new ServerSocket(1);
        System.out.println("Waiting...");
        //Set up schedules to display billboards today
        try {
            setupSchedulesToday();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(;;) {
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            String command = ois.readUTF();
            //request with token
            if(token!=null){
                String tokenString = ois.readUTF();
                if(tokenString.equals(token.get("token"))) {
                    if (!isTokenExpired(token)) {
                        /*
                         *Create new user
                         * Client send: user object, token
                         * Server send: string result
                         */
                        if (command.equals("create user")) {
                            User user = (User) ois.readObject();
                            try {
                                SaveData.saveNewUser(user);
                                oos.writeUTF("Save user success");
                            } catch (SQLException ex) {
                                if (ex.getErrorCode() == 1062) {
                                    oos.writeUTF("User already exist");
                                } else {
                                    oos.writeUTF("Something wrong with database");
                                    ex.printStackTrace();
                                }
                            }
                            oos.flush();
                        }
                        /*
                         *Create new billboard
                         * Client send: billboard object
                         * Server send: string result
                         */
                        else if (command.equals("create billboard")) {
                            Billboard billboard = (Billboard) ois.readObject();
                            if (GetData.hasCreateBillboardPermission(currentUserName)) {
                                try {
                                    SaveData.saveBillboard(billboard);
                                    oos.writeUTF("Save billboard success");
                                } catch (SQLException ex) {
                                    oos.writeUTF("Fail to save billboard");
                                    ex.printStackTrace();
                                }
                                oos.flush();
                            } else {
                                oos.writeUTF("permission deny");
                                oos.flush();
                            }

                        }
                        /*
                         *Create new schedule
                         * Client send: schedule object
                         * Server send: string result
                         */
                        else if (command.equals("create schedule")) {
                            Schedule schedule = (Schedule) ois.readObject();
                            if (GetData.hasCreateBillboardPermission(currentUserName)) {
                                try {
                                    SaveData.saveSchedule(schedule);
                                    oos.writeUTF("Save schedule success");
                                } catch (SQLException ex) {
                                    oos.writeUTF("Fail to save schedule");
                                    ex.printStackTrace();
                                }
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        }
                        /*
                         *Logout: clear current user, clear token
                         * Server send: string result
                         */
                        else if (command.equals("logout")) {
                            currentUserName = null;
                            token = null;
                            oos.writeUTF("Logout success");
                            oos.flush();
                        }
                        /*
                         *Get all users to edit or display
                         * Server send: Array list of user object (if user has permission)
                         */
                        else if (command.equals("get all users")) {
                            if (GetData.hasEditUsersPermission(currentUserName)) {
                                ArrayList<User> users = GetData.getAllUser();
                                oos.writeObject(users);
                            } else {
                                oos.writeUTF("permission deny");
                                oos.flush();
                            }
                        }
                        /*
                         *Delete user (if user has permission)
                         */
                        else if (command.equals("delete user")) {
                            String deleteName = ois.readUTF();
                            if (GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.deleteUser(deleteName);
                            } else {
                                oos.writeUTF("permission deny");
                                oos.flush();
                            }
                        }
                        /*
                         *Get all users to edit or display
                         * Server send: Array list of billboard object
                         */
                        else if (command.equals("get all billboards")) {
                            ArrayList<Billboard> billboards = GetData.getAllBillboards();
                            oos.writeObject(billboards);
                            oos.flush();
                        }
                        /*
                         *Get billboards of user
                         * Client send: author name
                         * Server send: Array list of billboard object
                         */
                        else if (command.equals("get billboards")) {
                            String author = ois.readUTF();
                            ArrayList<Billboard> billboards = GetData.getBillboardsByUserName(author);
                            oos.writeObject(billboards);
                            oos.flush();
                        }
                        /*
                         *Change password
                         * Client send: editUser name, new hashed password
                         * Server send: string result
                         */
                        else if (command.equals("change password")) {
                            String editUserName = ois.readUTF();
                            String newHashedPassword = ois.readUTF();
                            if (editUserName.equals(currentUserName) || GetData.hasEditUsersPermission(editUserName)) {
                                SaveData.changeUserPassword(editUserName, newHashedPassword);
                                oos.writeUTF("Change password success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change edit users permission")) {
                            String editUserName = ois.readUTF();
                            Boolean b = ois.readBoolean();
                            if ((!currentUserName.equals(editUserName)) && GetData.hasEditUsersPermission(currentUserName)) {
                                SaveData.changeEditUsersPermission(editUserName, b);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change create billboard permission")) {
                            String editUserName = ois.readUTF();
                            Boolean b = ois.readBoolean();
                            if ((!currentUserName.equals(editUserName)) && GetData.hasEditUsersPermission(currentUserName)) {
                                SaveData.changeCreateBillboardPermission(editUserName, b);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");

                            }
                            oos.flush();
                        } else if (command.equals("change schedule billboard permission")) {
                            String editUserName = ois.readUTF();
                            Boolean b = ois.readBoolean();
                            if ((!currentUserName.equals(editUserName)) && GetData.hasEditUsersPermission(currentUserName)) {
                                SaveData.changeScheduleBillboardPermission(editUserName, b);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");

                            }
                            oos.flush();
                        } else if (command.equals("change edit all billboards permission")) {
                            String editUserName = ois.readUTF();
                            Boolean b = ois.readBoolean();
                            if ((!currentUserName.equals(editUserName)) && GetData.hasEditUsersPermission(currentUserName)) {
                                SaveData.changeEditAllBillboardPermission(editUserName, b);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        }
                        /*
                         *Delete billboard
                         * Client send: author, billboard name
                         * Server send: string result
                         */
                        else if (command.equals("delete billboard")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.deleteBillboard(author, billboardName);
                                oos.writeUTF("delete billboard success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change billboard background")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newBillboardBackground = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardBackground(author, billboardName, newBillboardBackground);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change message text")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newMessageText = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardMessageText(author, billboardName, newMessageText);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change message colour")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newMessageColour = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardMessageColour(author, billboardName, newMessageColour);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change info text")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newInfoText = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardInfoText(author, billboardName, newInfoText);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change info colour")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newInfoColour = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardInfoColour(author, billboardName, newInfoColour);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change picture url")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newPictureUrl = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardPictureUrl(author, billboardName, newPictureUrl);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change picture data")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newPictureData = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardPictureData(author, billboardName, newPictureData);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("change billboard name")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newBillboardName = ois.readUTF();
                            if (currentUserName.equals(author) || GetData.hasEditAllBillboardPermission(currentUserName)) {
                                SaveData.updateBillboardName(author, billboardName, newBillboardName);
                                oos.writeUTF("save success");
                            } else {
                                oos.writeUTF("permission deny");
                            }
                            oos.flush();
                        } else if (command.equals("delete schedule")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            if (!billboardName.equals(currentBillboardName)) {
                                if (currentUserName.equals(author) || GetData.hasScheduleBillboardPermission(currentUserName)) {
                                    SaveData.deleteSchedule(author, billboardName);
                                    oos.writeUTF("delete schedule success");
                                } else {
                                    oos.writeUTF("permission deny");
                                }
                            } else {
                                oos.writeUTF("billboard is showing");
                            }
                            oos.flush();
                        } else if (command.equals("change date schedule")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newDate = ois.readUTF();
                            if (!billboardName.equals(currentBillboardName)) {
                                if (GetData.hasScheduleBillboardPermission(currentUserName)) {
                                    SaveData.updateScheduleDate(author, billboardName, newDate);
                                    oos.writeUTF("save success");
                                } else {
                                    oos.writeUTF("permission deny");
                                }
                            } else {
                                oos.writeUTF("billboard is showing");
                            }
                            oos.flush();
                        } else if (command.equals("change recursive time schedule")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newRecurtime = ois.readUTF();
                            if (!billboardName.equals(currentBillboardName)) {
                                if (GetData.hasScheduleBillboardPermission(currentUserName)) {
                                    SaveData.updateScheduleRecurTime(author, billboardName, newRecurtime);
                                    oos.writeUTF("save success");
                                } else {
                                    oos.writeUTF("permission deny");
                                }
                            } else {
                                oos.writeUTF("billboard is showing");
                            }
                            oos.flush();
                        } else if (command.equals("change duration schedule")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newDuration = ois.readUTF();
                            if (!billboardName.equals(currentBillboardName)) {
                                if (GetData.hasScheduleBillboardPermission(currentUserName)) {
                                    SaveData.updateScheduleDuration(author, billboardName, newDuration);
                                    oos.writeUTF("save success");
                                } else {
                                    oos.writeUTF("permission deny");
                                }
                            } else {
                                oos.writeUTF("billboard is showing");
                            }
                            oos.flush();
                        } else if (command.equals("change time schedule")) {
                            String author = ois.readUTF();
                            String billboardName = ois.readUTF();
                            String newTime = ois.readUTF();
                            if (!billboardName.equals(currentBillboardName)) {
                                if (GetData.hasScheduleBillboardPermission(currentUserName)) {
                                    SaveData.updateScheduleTime(author, billboardName, newTime);
                                    oos.writeUTF("save success");
                                } else {
                                    oos.writeUTF("permission deny");
                                }
                            } else {
                                oos.writeUTF("billboard is showing");
                            }
                            oos.flush();
                        } else if (command.equals("get all schedules")) {
                            ArrayList<Schedule> schedules = GetData.getAllSchedules();
                            oos.writeObject(schedules);
                            oos.flush();
                        }
                        /*get user object by name
                         * Client send: user name
                         * Server send: user object
                         */
                        else if (command.equals("get user")) {
                            User user = GetData.getUser(ois.readUTF());
                            oos.writeObject(user);
                            oos.flush();
                        }
                        /*get billboard object by author name
                         * Client send: user name, billboardName
                         * Server send: billboard object
                         */
                        else if (command.equals("get billboard")) {
                            String authorName = ois.readUTF();
                            String billboardName = ois.readUTF();
                            Billboard billboard = GetData.getBillboard(authorName, billboardName);
                            oos.writeObject(billboard);
                            oos.flush();
                        }
                        /*get billboards object by author name
                         * Client send: author name
                         * Server send: array list of billboard object
                         */
                        else if (command.equals("get billboards ")) {
                            String authorName = ois.readUTF();
                            ArrayList<Billboard> billboards = GetData.getBillboardsByUserName(authorName);
                            oos.writeObject(billboards);
                            oos.flush();
                        }
                    }
                    else {
                        oos.writeUTF("token is expired");
                    }
                }
                else {
                    oos.writeUTF("invalid token");
                }
            }
            //request without token
            else {
                /*
                 * Get today schedules
                 * Return null if there is nothing to show
                 */
                if(command.equals("get schedule now")){
                    Date dateNow = new Date();
                    if(schedules!=null) {
                        for (Schedule schedule : schedules) {
                            Date start = schedule.getTime();
                            Date duration = schedule.getDuration();
                            Calendar end = Calendar.getInstance();
                            end.setTime(duration);
                            String[] timeDurationData = duration.toString().split(":");
                            int S = Integer.parseInt(timeDurationData[2]);
                            int M = Integer.parseInt(timeDurationData[1]);
                            int H = Integer.parseInt(timeDurationData[0]);
                            end.add(Calendar.HOUR, H);
                            end.add(Calendar.MINUTE, M);
                            end.add(Calendar.SECOND, S);
                            if (start.before(parseTime(dateNow.toString())) && end.after(parseTime(dateNow.toString()))) {
                                Billboard currentBillboard = GetData.getBillboard(schedule.getAuthor(), schedule.getBillboardName());
                                currentBillboardName = currentBillboard.getBillboardName();
                                oos.writeObject(currentBillboard);
                            } else {
                                oos.writeObject(null);
                            }
                            oos.flush();
                        }
                    }
                    else
                    {
                        oos.writeObject(null);
                    }
                }/*
                 *Login: access to control app
                 * Client send: user name, hashed pass
                 * Server send: string result, byte token
                 */
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
            }
            ois.close();
            oos.close();
            socket.close();
        }
    }
    /*
     *Set up schedule today to display billboards
     */
    private static void setupSchedulesToday() throws SQLException, ParseException {
        ArrayList<Schedule> todaySchedules = GetData.getTodaySchedules();
        for(Schedule schedule:todaySchedules){
            Date recurTime = schedule.getRecurTime();
            Date date = schedule.getDate();
            Date time = schedule.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            Schedule scheduleToDisplay = schedule;
            if(!recurTime.equals(parseTime("00:00:00"))){
                String[] timeRecurData = recurTime.toString().split(":");
                int recurS =Integer.parseInt(timeRecurData[2]);
                int recurM =Integer.parseInt(timeRecurData[1]);
                int recurH =Integer.parseInt(timeRecurData[0]);
                do {
                    scheduleToDisplay.setTime(parseTime(parseStringTime(calendar.getTime())));
                    schedules.add(scheduleToDisplay);
                    calendar.add(Calendar.SECOND,recurS);
                    calendar.add(Calendar.MINUTE,recurM);
                    calendar.add(Calendar.HOUR,recurH);
                }while (calendar.after(date));
            }else {
                schedules.add(scheduleToDisplay);
            }
            //update date and time in db for schedule which have recursive
            SaveData.updateScheduleDate(schedule.getAuthor(),schedule.getBillboardName(),parseStringDate(calendar.getTime()));
            SaveData.updateScheduleDate(schedule.getAuthor(),schedule.getBillboardName(),parseStringTime(calendar.getTime()));
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
