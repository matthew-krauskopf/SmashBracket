package DBase;

import java.sql.*;
import java.io.IOException;

public class Tournies {

    public static String database_name;
    public static String table_name = "Tournies";
    private static Statement stmt;

    public Tournies(Statement fed_stmt) {
        stmt = fed_stmt;
    }

    public void create(String dbase_name) {
        setDatabase(dbase_name);
        try {
            String sql = String.format("CREATE TABLE IF NOT EXISTS %s.%s (" +
                        "   ID int, " +
                        "   Name VARCHAR(255), " +
                        "   Entrants int, " +
                        "   Day Date, " +
                        "   PRIMARY KEY(ID));",
                            database_name, table_name);
            stmt.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setDatabase(String dbase_name) {
        database_name = dbase_name;
    }

    public void recordTourney(int id, String name, String date, int num_entrants) {
        try {
            String sql = String.format("INSERT INTO %s.%s (ID, Name, Day, Entrants) VALUES (%d, '%s', '%s', %d);",
                                        database_name, table_name, id, name, date, num_entrants);
            stmt.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int checkBracketDataNew(int ID) {
        // Ensure bracket has not been entered into db before
        try {
            String sql = String.format("SELECT 1 FROM %s.%s where ID = %d;",
                                        database_name, table_name, ID);
            ResultSet r = stmt.executeQuery(sql);
            // Data already exists
            if (r.next()) {
                return 0;
            }
            // New data: proceed with import
            else {
                return 1;
            }
        // Error handling
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}
