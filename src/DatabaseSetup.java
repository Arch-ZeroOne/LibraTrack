import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/revise_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        System.out.println("🚀 Starting Revise Database Setup...");

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully");

            // Connect to database
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Database connection established");

            // First, create the database if it doesn't exist
            createDatabaseIfNotExists(connection);

            // Execute the complete schema
            executeSqlFile(connection, "src/complete_database_schema.sql");

            // Add copies for existing books
            addCopiesForExistingBooks(connection);

            // Close connection
            connection.close();
            System.out.println("✅ Database setup completed successfully!");
            System.out.println("🎉 Revise Database is ready to use!");

        } catch (Exception e) {
            System.err.println("❌ Database setup failed:");
            e.printStackTrace();
        }
    }

    private static void createDatabaseIfNotExists(Connection connection) throws Exception {
        Statement statement = connection.createStatement();

        // Create database if it doesn't exist
        String createDbSql = "CREATE DATABASE IF NOT EXISTS revise_db";
        statement.executeUpdate(createDbSql);
        System.out.println("✅ Database 'revise_db' created/verified");

        // Use the database
        statement.executeUpdate("USE revise_db");
        statement.close();
    }

    private static void executeSqlFile(Connection connection, String filename) throws Exception {
        System.out.println("📄 Executing SQL file: " + filename);

        // Try to find the file - first check current directory, then src directory
        java.io.File file = new java.io.File(filename);
        if (!file.exists()) {
            file = new java.io.File("src/" + filename);
        }
        if (!file.exists()) {
            throw new java.io.FileNotFoundException("Could not find SQL file: " + filename);
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sqlBuilder = new StringBuilder();
        String line;

        Statement statement = connection.createStatement();

        while ((line = reader.readLine()) != null) {
            // Skip comments and empty lines
            line = line.trim();
            if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) {
                continue;
            }

            sqlBuilder.append(line);

            // Execute when we hit a semicolon
            if (line.endsWith(";")) {
                String sql = sqlBuilder.toString();
                sql = sql.substring(0, sql.length() - 1); // Remove semicolon

                if (!sql.trim().isEmpty()) {
                    try {
                        statement.executeUpdate(sql);
                        System.out.println("✅ Executed: " + sql.substring(0, Math.min(50, sql.length())) + "...");
                    } catch (Exception e) {
                        System.err.println("❌ Failed to execute: " + sql);
                        throw e;
                    }
                }

                sqlBuilder.setLength(0); // Reset for next statement
            }
        }

        statement.close();
        reader.close();
        System.out.println("✅ SQL file execution completed");
    }

    private static void addCopiesForExistingBooks(Connection connection) throws Exception {
        System.out.println("📚 Adding copies for existing books...");

        // Find books that don't have any copies
        String findBooksQuery = "SELECT b.book_id FROM books b LEFT JOIN book bc ON b.book_id = bc.book_id WHERE bc.book_id IS NULL";
        Statement statement = connection.createStatement();
        var rs = statement.executeQuery(findBooksQuery);

        int count = 0;
        while (rs.next()) {
            int bookId = rs.getInt("book_id");

            // Add one copy for each book that doesn't have any
            String insertCopyQuery = "INSERT INTO book (book_id, isAvailable) VALUES (" + bookId + ", 'Available')";
            statement.executeUpdate(insertCopyQuery);
            count++;
        }

        rs.close();
        statement.close();

        if (count > 0) {
            System.out.println("✅ Added " + count + " book copies for existing books");
        } else {
            System.out.println("ℹ️  No existing books needed copies added");
        }
    }
}
