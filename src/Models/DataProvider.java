package Models;

import javafx.collections.FXCollections;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataProvider {
    private static DataProvider instance;
    private SQLConnect db;

    public static DataProvider getInstance() {
        if(instance==null) instance = new DataProvider();
        return instance;
    }
    private static void setInstance(DataProvider instance) {
        DataProvider.instance = instance;
    }
    private DataProvider(){
        db = new SQLConnect("VI_KHAN_PC\\SQLEXPRESS","1433","TodoList","AdminTVK","admin");
    }

    public TypeTask getTypeTask(String name) throws SQLException {
        TypeTask typetask =  new TypeTask();
        db.Connect();
        if(db.CheckConnet()){
            ResultSet result = db.ExcuteQuery("SELECT * FROM TypeList WHERE name = N'"+name+"'");
            result.next();
            typetask.setName(result.getString(1));
            typetask.setIcon(result.getString(2));
            typetask.setColor(result.getString(3));
            db.Disconnect();
        }
        return typetask ;
    }
    public boolean checklogin(String username,String password){

        db.Connect();
        if(db.CheckConnet()){
            ResultSet result = db.ExcuteQuery("SELECT * FROM Account WHERE username='"+username+"' and password ='"+password+"'");
            try {
                if(result.next()){
                    db.Disconnect();
                    return true;
                }else{
                    db.Disconnect();
                    return false;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }
    public ArrayList<TypeTask> getListType() throws SQLException {
        ArrayList<TypeTask> list = new ArrayList<>();


        db.Connect();
        if(db.CheckConnet()){
            ResultSet result = db.ExcuteQuery("SELECT * FROM TypeList");
            while (result.next()){
                TypeTask typetask =  new TypeTask();
                typetask.setName(result.getString(1));
                typetask.setIcon(result.getString(2));
                typetask.setColor(result.getString(3));
                list.add(typetask);
            }
            db.Disconnect();
        }
        return list;
    }
    public boolean AddTask(Task task) throws SQLException {
        int result =0;

        db.Connect();
        if(db.CheckConnet()){
            String date = task.getDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = task.getTime().format(DateTimeFormatter.ISO_TIME);
            String query = "Insert into TodoList(title,typeList,thisDay,thisTime,content) values(N'"+task.getTitle()+"',N'"+task.getType().getName()+"','"+date+"','"+time+"',N'"+task.getContent()+"')";
            result = db.ExcuteUpdate(query);
            db.Disconnect();
        }
        return result>0;
    }
    public ArrayList<Task> getListTask(int SortDay,int SortTime) throws SQLException {
        ArrayList<Task> list = new ArrayList<>();

        db.Connect();
        if(db.CheckConnet()){
            ResultSet result = db.ExcuteQuery("SELECT * FROM TodoList ORDER BY thisDay "+((SortDay==0)?"":" DESC ")+",thisTime"+((SortTime==0)?"":" DESC "));
            while (result.next()){
                Task myTask = new Task();
                myTask.setId(result.getInt(1));
                myTask.setTitle(result.getString(2));
                myTask.setType(DataProvider.getInstance().getTypeTask(result.getString(3)));
                myTask.setContent(result.getString(6));
                Date myDate = result.getDate(4);
                Time myTime = result.getTime(5);
                myTask.setDay(myDate.toLocalDate());
                myTask.setTime(myTime.toLocalTime());
                list.add(myTask);
            }
            db.Disconnect();
        }

        return list;
    }
    public boolean DelTask(String id) throws SQLException {
        int result =0;

        db.Connect();
        if(db.CheckConnet()){
            String query = "DELETE FROM TodoList WHERE id ="+id;
            result = db.ExcuteUpdate(query);
            db.Disconnect();
        }
        return result>0;
    }
    public Task getTask(String id) throws SQLException {

        db.Connect();
        Task myTask = new Task();
        if(db.CheckConnet()){
            ResultSet result = db.ExcuteQuery("SELECT * FROM TodoList WHERE id="+id);
            result.next();

                myTask.setId(result.getInt(1));
                myTask.setTitle(result.getString(2));
                myTask.setType(DataProvider.getInstance().getTypeTask(result.getString(3)));
                myTask.setContent(result.getString(6));
                Date myDate = result.getDate(4);
                Time myTime = result.getTime(5);
                myTask.setDay(myDate.toLocalDate());
                myTask.setTime(myTime.toLocalTime());
            }
            db.Disconnect();
        return myTask;
    }
    public boolean EditTask(Task task) throws SQLException {
        int result =0;

        db.Connect();
        if(db.CheckConnet()){
            String date = task.getDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = task.getTime().format(DateTimeFormatter.ISO_TIME);
            String query = "UPDATE TodoList SET title=N'"+task.getTitle()+"',typeList= N'"+task.getType().getName()+"',thisDay= '"+date+"', thisTime='"+time+"',content=N'"+task.getContent()+"' WHERE id="+task.getId();
            result = db.ExcuteUpdate(query);
            db.Disconnect();
        }
        return result>0;
    }

    public boolean checkTypeName(String newValue) {
        db.Connect();
        ResultSet result = null;
        if(db.CheckConnet()){
            String query = "SELECT * FROM TypeList WHERE name= '"+newValue+"'";
            result = db.ExcuteQuery(query);
            try {
                if(result.next()) return false;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            db.Disconnect();
        }
        return true;
    }

    public boolean InsertTypeTask(TypeTask newType) {
        int result =0;
        db.Connect();
        if(db.CheckConnet()){
            String query = "Insert into TypeList values(N'"+newType.getName()+"','"+newType.getIcon()+"',N'"+newType.getColor()+"')";
            result = db.ExcuteUpdate(query);
            db.Disconnect();
        }
        return result>0;
    }
    public boolean checkDelType(String newValue) {
        db.Connect();
        ResultSet result = null;
        if(db.CheckConnet()){
            String query = "SELECT COUNT(id) FROM TodoList WHERE typeList= N'"+newValue+"'";
            result = db.ExcuteQuery(query);
            try {
                if(result.next()){
                    return result.getInt(1)==0;
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            db.Disconnect();
        }
        return true;
    }

    public boolean DelType(String text) {
        int result =0;

        db.Connect();
        if(db.CheckConnet()){
            String query = "DELETE FROM TypeList WHERE name =N'"+text+"'";
            result = db.ExcuteUpdate(query);
            db.Disconnect();
        }
        return result>0;
    }
}
