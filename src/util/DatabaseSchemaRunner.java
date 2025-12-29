package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchemaRunner {

    private DatabaseUtil dbUtil;

    public DatabaseSchemaRunner() {
        this.dbUtil = new DatabaseUtil();
    }

    public void runCompleteSchema() {
        System.out.println("🚀 Starting LibraTrack Database Schema Setup...");

        Connection connection = dbUtil.connect();
        if (connection == null) {
            System.err.println("❌ Failed to connect to database. Make sure:");
            System.err.println("   - MySQL server is running");
            System.err.println("   - Database 'libratrack_db' exists");
            System.err.println("   - MySQL JDBC driver is available");
            return;
        }

        try {
            System.out.println("✅ Database connection successful");

            // Execute the complete schema
            executeSqlFile(connection, "../complete_database_schema.sql");

            System.out.println("✅ Database schema setup completed successfully!");
            System.out.println("🎉 LibraTrack database is now ready!");

        } catch (Exception e) {
            System.err.println("❌ Database setup failed:");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                System.out.println("🔌 Database connection closed");
            } catch (SQLException e) {
                System.err.println("Warning: Could not close database connection");
            }
        }
    }

    private void executeSqlFile(Connection connection, String filename) throws IOException, SQLException {
        System.out.println("📄 Reading SQL file: " + filename);

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder sqlBuilder = new StringBuilder();
        String line;
        int statementCount = 0;

        Statement statement = connection.createStatement();

        try {
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) {
                    continue;
                }

                sqlBuilder.append(line).append(" ");

                // Execute when we hit a semicolon
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString().trim();
                    sql = sql.replaceAll(";$", ""); // Remove semicolon

                    if (!sql.trim().isEmpty()) {
                        try {
                            statement.executeUpdate(sql);
                            statementCount++;
                            String preview = sql.length() > 60 ? sql.substring(0, 60) + "..." : sql;
                            System.out.println("✅ [" + statementCount + "] " + preview);
                        } catch (SQLException e) {
                            System.err.println("❌ Failed statement " + statementCount + ": " + sql);
                            throw e;
                        }
                    }

                    sqlBuilder.setLength(0); // Reset for next statement
                }
            }
        } finally {
            statement.close();
            reader.close();
        }

        System.out.println("📊 Total statements executed: " + statementCount);
    }

    // Main method for direct execution
    public static void main(String[] args) {
        DatabaseSchemaRunner runner = new DatabaseSchemaRunner();
        runner.runCompleteSchema();
    }
}
