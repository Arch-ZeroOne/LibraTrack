    package main;

import java.io.BufferedReader;
import java.io.FileReader;
import util.DatabaseUtil;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetupRunner {

    public static void main(String[] args) {
        System.out.println("🚀 Starting LibraTrack Database Setup...");

        DatabaseUtil dbUtil = new DatabaseUtil();
        Connection connection = dbUtil.connect();

        if (connection == null) {
            System.err.println("❌ Failed to connect to database");
            System.err.println("Make sure MySQL is running and the database credentials are correct");
            return;
        }

        try {
            // Execute the complete schema
            executeSqlFile(connection, "complete_database_schema.sql");

            System.out.println("✅ Database setup completed successfully!");
            System.out.println("🎉 LibraTrack is ready to use!");

        } catch (Exception e) {
            System.err.println("❌ Database setup failed:");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    private static void executeSqlFile(Connection connection, String filename) throws Exception {
        System.out.println("📄 Executing SQL file: " + filename);

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder sqlBuilder = new StringBuilder();
        String line;
        int statementCount = 0;

        Statement statement = connection.createStatement();

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
                        System.out.println("✅ Executed statement " + statementCount + ": " +
                                         sql.substring(0, Math.min(60, sql.length())) + "...");
                    } catch (Exception e) {
                        System.err.println("❌ Failed to execute statement " + statementCount + ": " + sql);
                        throw e;
                    }
                }

                sqlBuilder.setLength(0); // Reset for next statement
            }
        }

        statement.close();
        reader.close();
        System.out.println("✅ SQL file execution completed - " + statementCount + " statements executed");
    }
}
