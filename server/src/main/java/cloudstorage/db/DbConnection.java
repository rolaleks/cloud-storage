package cloudstorage.db;

import java.sql.*;

public class DbConnection {

    private Connection connection;
    private Statement stmt;

    private static DbConnection self;


    private DbConnection() {

    }

    public static DbConnection getInstance() {
        if (self == null) {
            self = new DbConnection();
        }

        return self;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:DB.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int executeUpdate(String query, Object... params) {
        try {
            PreparedStatement ps =  getInstance().getConnection().prepareStatement(query);

            prepareParams(ps, params);

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int executeInsert(String query, Object... params) {
        try {
            PreparedStatement ps =  getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            prepareParams(ps, params);

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ResultSet executeQuery(String query, Object... params) {

        try {
            PreparedStatement ps = getInstance().getConnection().prepareStatement(query);

            prepareParams(ps, params);
            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void prepareParams(PreparedStatement ps, Object[] params) throws SQLException {
        int i = 1;

        for (Object param : params) {
            if (param instanceof Integer) {
                ps.setInt(i++, (Integer) param);
            } else if (param instanceof Double) {
                ps.setDouble(i++, (Double) param);
            } else if (param instanceof Float) {
                ps.setFloat(i++, (Float) param);
            } else {
                ps.setString(i++, (String) param);
            }
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
