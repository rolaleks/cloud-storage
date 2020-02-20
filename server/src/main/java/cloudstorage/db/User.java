package cloudstorage.db;

import java.sql.*;
import java.util.ArrayList;

public class User extends Record {

    private Integer id;
    private String login;
    private String pass;

    public static String getTableName() {
        return "users";
    }

    public boolean save() {
        if (this.isNewRecord) {
            return insert();
        } else {
            return update();
        }
    }

    protected boolean insert() {

        int id = DbConnection.getInstance().executeInsert("INSERT INTO " + getTableName() + " (login,pass)" +
                "VALUES(?,?)", this.login, this.pass);

        boolean success = id != 0;
        if (success) {
            this.isNewRecord = false;
            this.id = id;
        }
        return success;
    }

    protected boolean update() {

        int result = DbConnection.getInstance().executeUpdate("UPDATE " + getTableName() + " SET login = ?, pass = ? " +
                "WHERE id = ?", this.login, this.pass, this.id);

        return result == 1;
    }

    protected boolean delete() {

        int result = DbConnection.getInstance().executeUpdate("DELETE FROM " + getTableName() + " WHERE id = ?", this.id);
        return result == 1;
    }

    public static User findOne(Integer id) {

        ResultSet resultSet = DbConnection.getInstance().executeQuery("SELECT * FROM " + getTableName() + " WHERE id = ? LIMIT 1", id);
        try {
            if (resultSet.next()) {
                User user = new User();
                user.prepareModel(resultSet);


                return user;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return null;
    }

    public static User findOneByLogin(String login) {

        ResultSet resultSet = DbConnection.getInstance().executeQuery("SELECT * FROM " + getTableName() + " WHERE login = ? LIMIT 1", login);
        try {
            if (resultSet.next()) {
                User user = new User();
                user.prepareModel(resultSet);

                return user;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return null;
    }


    public static ArrayList<User> findAll() {

        ResultSet resultSet = DbConnection.getInstance().executeQuery("SELECT * FROM " + getTableName());

        ArrayList<User> users = new ArrayList<>();
        try {

            while (resultSet.next()) {
                User user = new User();
                user.prepareModel(resultSet);


                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return users;
    }


    public void prepareModel(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.login = resultSet.getString("login");
        this.pass = resultSet.getString("pass");
        this.isNewRecord = false;
    }

    public static void dropTable() {
        DbConnection.getInstance().executeUpdate("DROP TABLE IF EXISTS " + getTableName());
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }
}
