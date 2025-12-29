import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FixBookCopies {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/revise_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        System.out.println("🔧 Fixing book copies for existing books...");

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully");

            // First check what databases exist
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ MySQL connection established");

            checkDatabases(connection);
            connection.close();

            // Now connect to revise_db specifically
            Connection dbConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connected to revise_db");

            // Check what tables exist
            checkTables(dbConnection);

            // Check if there are book copies
            checkBookCopies(dbConnection);

            // Add copies for existing books
            addCopiesForExistingBooks(connection);

            // Close connection
            connection.close();
            System.out.println("✅ Book copies fix completed successfully!");
            System.out.println("🎉 Book borrowing should now work!");

        } catch (Exception e) {
            System.err.println("❌ Book copies fix failed:");
            e.printStackTrace();
        }
    }

    private static void checkTables(Connection connection) throws Exception {
        System.out.println("🔍 Checking existing tables...");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SHOW TABLES");

        System.out.println("📋 Existing tables:");
        while (rs.next()) {
            System.out.println("  - " + rs.getString(1));
        }

        // Check structure of key tables
        checkTableStructure(connection, "books");
        checkTableStructure(connection, "book");
        checkTableStructure(connection, "borrow");

        rs.close();
        statement.close();
        rs.close();
        statement.close();
    }

    private static void checkDatabases(Connection connection) throws Exception {
        System.out.println("🔍 Checking available databases...");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SHOW DATABASES");

        System.out.println("📋 Available databases:");
        while (rs.next()) {
            String dbName = rs.getString(1);
            System.out.println("  - " + dbName);
            if (dbName.equals("revise_db")) {
                System.out.println("  ✅ Found revise_db!");
            }
        }
        rs.close();
        statement.close();
    }

    private static void checkBookCopies(Connection connection) throws Exception {
        System.out.println("📚 Checking book copies...");

        // Count total books
        Statement statement = connection.createStatement();
        ResultSet rs1 = statement.executeQuery("SELECT COUNT(*) as total_books FROM books");
        rs1.next();
        int totalBooks = rs1.getInt("total_books");
        rs1.close();

        // Count book copies
        ResultSet rs2 = statement.executeQuery("SELECT COUNT(*) as total_copies FROM book");
        rs2.next();
        int totalCopies = rs2.getInt("total_copies");
        rs2.close();

        // Count available copies
        ResultSet rs3 = statement.executeQuery("SELECT COUNT(*) as available_copies FROM book WHERE isAvailable = 'Available'");
        rs3.next();
        int availableCopies = rs3.getInt("available_copies");
        rs3.close();

        System.out.println("📊 Book Statistics:");
        System.out.println("  - Total books in catalog: " + totalBooks);
        System.out.println("  - Total book copies: " + totalCopies);
        System.out.println("  - Available copies: " + availableCopies);

        if (totalCopies == 0 && totalBooks > 0) {
            System.out.println("⚠️  WARNING: Books exist but no copies available for borrowing!");
            System.out.println("🔧 Creating copies for existing books...");

            // Create copies for existing books
            ResultSet rs4 = statement.executeQuery("SELECT book_id FROM books");
            int copiesCreated = 0;
            while (rs4.next()) {
                int bookId = rs4.getInt("book_id");

                // Check if copy already exists
                PreparedStatement checkPs = connection.prepareStatement("SELECT COUNT(*) FROM book WHERE book_id = ?");
                checkPs.setInt(1, bookId);
                ResultSet checkRs = checkPs.executeQuery();
                checkRs.next();
                int existingCopies = checkRs.getInt(1);
                checkRs.close();
                checkPs.close();

                if (existingCopies == 0) {
                    // Create a copy
                    PreparedStatement insertPs = connection.prepareStatement("INSERT INTO book (book_id, isAvailable, condition_status) VALUES (?, 'Available', 'Good')");
                    insertPs.setInt(1, bookId);
                    insertPs.executeUpdate();
                    insertPs.close();
                    copiesCreated++;
                }
            }
            rs4.close();

            System.out.println("✅ Created " + copiesCreated + " book copies");
        }

        statement.close();
    }

    private static void checkTableStructure(Connection connection, String tableName) throws Exception {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("DESCRIBE " + tableName);

            System.out.println("📋 Structure of table '" + tableName + "':");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("Field") + " (" + rs.getString("Type") + ")");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("❌ Could not check table '" + tableName + "': " + e.getMessage());
        }
    }

    private static void addCopiesForExistingBooks(Connection connection) throws Exception {
        System.out.println("📚 Adding copies for existing books...");

        // Find books that don't have any copies
        String findBooksQuery = "SELECT b.book_id FROM books b LEFT JOIN book bc ON b.book_id = bc.book_id WHERE bc.book_id IS NULL";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(findBooksQuery);

        int count = 0;
        while (rs.next()) {
            int bookId = rs.getInt("book_id");

            // Add one copy for each book that doesn't have any
            String insertCopyQuery = "INSERT INTO book (book_id, isAvailable) VALUES (?, 'Available')";
            PreparedStatement ps = connection.prepareStatement(insertCopyQuery);
            ps.setInt(1, bookId);
            ps.executeUpdate();
            ps.close();

            count++;
            System.out.println("✅ Added copy for book ID: " + bookId);
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
