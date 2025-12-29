package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import util.DatabaseUtil;

public class SchemaInitializer {

    public static void initializeDatabase() {
        System.out.println("🚀 LibraTrack Database Schema Initializer");
        System.out.println("========================================");

        DatabaseUtil dbUtil = new DatabaseUtil();
        Connection connection = dbUtil.connect();

        if (connection == null) {
            System.err.println("❌ CRITICAL: Database connection failed!");
            System.err.println("Please ensure:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Database 'libratrack_db' exists or will be created");
            System.err.println("  3. MySQL JDBC driver is properly configured");
            System.err.println("  4. Database credentials are correct");
            return;
        }

        try {
            System.out.println("✅ Connected to database successfully");

            // Try to create database if it doesn't exist
            createDatabaseIfNeeded(connection);

            // Execute schema
            executeCompleteSchema(connection);

            System.out.println("\n🎉 SUCCESS: LibraTrack database schema created!");
            System.out.println("📚 All tables are ready for use.");
            System.out.println("🚀 You can now start the application and begin managing your library!");

        } catch (Exception e) {
            System.err.println("❌ FATAL ERROR during database setup:");
            e.printStackTrace();
            System.err.println("\n💡 TROUBLESHOOTING:");
            System.err.println("  - Check MySQL server status");
            System.err.println("  - Verify database permissions");
            System.err.println("  - Ensure complete_database_schema.sql exists");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("🔌 Database connection closed");
                }
            } catch (SQLException e) {
                System.err.println("Warning: Could not close connection properly");
            }
        }
    }

    private static void createDatabaseIfNeeded(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        try {
            // Try to create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS libratrack_db");
            System.out.println("✅ Database 'libratrack_db' verified/created");

            // Switch to the database
            stmt.executeUpdate("USE libratrack_db");
            System.out.println("✅ Using database 'libratrack_db'");
        } finally {
            stmt.close();
        }
    }

    private static void executeCompleteSchema(Connection connection) throws IOException, SQLException {
        System.out.println("\n📄 Executing complete database schema...");

        BufferedReader reader = new BufferedReader(new FileReader("complete_database_schema.sql"));
        StringBuilder sqlBuffer = new StringBuilder();
        String line;
        int totalStatements = 0;
        int successCount = 0;

        Statement statement = connection.createStatement();

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) {
                    continue;
                }

                sqlBuffer.append(line).append(" ");

                // Execute when we hit a semicolon
                if (line.endsWith(";")) {
                    String sql = sqlBuffer.toString().trim();
                    sql = sql.replaceAll(";$", ""); // Remove semicolon

                    if (!sql.trim().isEmpty()) {
                        totalStatements++;
                        try {
                            statement.executeUpdate(sql);
                            successCount++;

                            // Show progress for major statements
                            if (sql.toUpperCase().startsWith("CREATE TABLE")) {
                                String tableName = extractTableName(sql);
                                System.out.println("📋 Created table: " + tableName);
                            } else if (sql.toUpperCase().startsWith("INSERT")) {
                                System.out.println("💾 Inserted sample data");
                            } else if (sql.toUpperCase().startsWith("CREATE VIEW")) {
                                String viewName = extractViewName(sql);
                                System.out.println("👁️ Created view: " + viewName);
                            }

                        } catch (SQLException e) {
                            System.err.println("❌ Failed: " + sql.substring(0, Math.min(50, sql.length())));
                            throw e;
                        }
                    }

                    sqlBuffer.setLength(0);
                }
            }

            System.out.println("\n📊 EXECUTION SUMMARY:");
            System.out.println("   Total statements: " + totalStatements);
            System.out.println("   Successful: " + successCount);
            System.out.println("   Failed: " + (totalStatements - successCount));

        } finally {
            statement.close();
            reader.close();
        }
    }

    private static String extractTableName(String createTableSql) {
        try {
            // Extract table name from "CREATE TABLE table_name"
            String upper = createTableSql.toUpperCase();
            int tableIndex = upper.indexOf("TABLE") + 6;
            int endIndex = createTableSql.indexOf("(", tableIndex);
            if (endIndex == -1) endIndex = createTableSql.indexOf(" ", tableIndex + 1);
            return createTableSql.substring(tableIndex, endIndex).trim();
        } catch (Exception e) {
            return "unknown_table";
        }
    }

    private static String extractViewName(String createViewSql) {
        try {
            // Extract view name from "CREATE VIEW view_name"
            String upper = createViewSql.toUpperCase();
            int viewIndex = upper.indexOf("VIEW") + 5;
            int endIndex = createViewSql.indexOf(" ", viewIndex);
            return createViewSql.substring(viewIndex, endIndex).trim();
        } catch (Exception e) {
            return "unknown_view";
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        initializeDatabase();
    }
}
