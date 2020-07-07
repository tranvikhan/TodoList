
package Models;

import java.sql.*;

public class SQLConnect {
    private Connection con;
    private Statement s;
    private PreparedStatement ps;

    private String SeverName;
    private String Port;
    private String DatabaseName;
    private String Username;
    private String Password;


    public SQLConnect(String serverName,String portNum,String databaseName,String user,String password) {
        this.SeverName = serverName;
        this.Port = portNum;
        this.DatabaseName = databaseName;
        this.Username = user;
        this.Password = password;
        this.s = null;
        this.con = null;

    }
    public void Connect(){
        try{
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            String connectionSTR = "jdbc:sqlserver://"+SeverName+":"+Port+";databaseName="+DatabaseName+";user="+Username+";password="+Password;
            con = DriverManager.getConnection(connectionSTR);
            s= con.createStatement();
            System.out.println("Connect!");

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void Disconnect(){
        if(CheckConnet()){
            try {
                con.close();
                System.out.println("Disconnect!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Connection GetConnection(){
        return con;
    }
    public Statement GetStatement(){
        return s;
    }
    public boolean CheckConnet(){
        if(con ==null)
            return false;
        else{
            try{
                if(con.isClosed())
                    return false;
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
        return true;
    }
    public ResultSet ExcuteQuery(String query){
        ResultSet result = null;
        try{
            if(CheckConnet())
                result = s.executeQuery(query);
            else
                System.err.println("Connection is close");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }
    public int ExcuteUpdate(String query){
        int result = 0;
        try{
            if(CheckConnet())
                result = s.executeUpdate(query);
            else
                System.err.println("Connection is close");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return result;
    }
}
