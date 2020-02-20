package cloudstorage.db;

import java.sql.Connection;

public abstract class Record {

    protected boolean isNewRecord = true;


    public boolean save() {
        if (this.isNewRecord) {
            return insert();
        } else {
            return update();
        }
    }

    protected abstract boolean insert();

    protected abstract boolean update();


    protected static Connection getDbConnection() {

        return DbConnection.getInstance().getConnection();
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

}
