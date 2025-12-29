package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import util.DatabaseUtil;

public class StatisticsViewController implements Initializable {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label totalBorrowedLabel;
    @FXML
    private Label totalGenresLabel;
    @FXML
    private Label availableBooksLabel;
    @FXML
    private Label todayAttendanceLabel;
    @FXML
    private Label overdueBooksLabel;

    @FXML
    private PieChart bookStatusChart;
    @FXML
    private BarChart<String, Number> genreChart;
    @FXML
    private LineChart<String, Number> borrowingTrendChart;
    @FXML
    private BarChart<String, Number> studentActivityChart;

    private DatabaseUtil dbUtil;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbUtil = new DatabaseUtil();
        loadDashboardStatistics();
    }

    private void loadDashboardStatistics() {
        Connection conn = null;
        try {
            conn = dbUtil.connect();
            if (conn == null) {
                setDefaultValues();
                return;
            }

            // Load basic statistics from the view
            loadBasicStatistics(conn);

            // Load additional computed statistics
            loadAdditionalStatistics(conn);

            // Load chart data
            loadChartData(conn);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            setDefaultValues();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private void loadBasicStatistics(Connection conn) throws SQLException {
        String query = "SELECT * FROM library_dashboard_totals";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalBooksLabel.setText(String.valueOf(rs.getInt("total_books")));
                totalStudentsLabel.setText(String.valueOf(rs.getInt("active_students")));
                totalBorrowedLabel.setText(String.valueOf(rs.getInt("borrowed_books")));
                totalGenresLabel.setText(String.valueOf(rs.getInt("total_categories")));
            } else {
                setDefaultValues();
            }
        }
    }

    private void loadAdditionalStatistics(Connection conn) throws SQLException {
        // Load available books count
        int availableBooks = getAvailableBooks(conn);
        availableBooksLabel.setText(String.valueOf(availableBooks));

        // Load today's attendance count
        int todayAttendance = getTodayAttendance(conn);
        todayAttendanceLabel.setText(String.valueOf(todayAttendance));

        // Load overdue books count
        int overdueBooks = getOverdueBooks(conn);
        overdueBooksLabel.setText(String.valueOf(overdueBooks));
    }

    private void loadChartData(Connection conn) throws SQLException {
        try {
            // Load book status pie chart
            ObservableList<PieChart.Data> bookStatusData = getBookStatusData(conn);
            System.out.println("Book status data loaded: " + bookStatusData.size() + " items");
            bookStatusChart.setData(bookStatusData);

            // Load genre distribution bar chart
            ObservableList<XYChart.Series<String, Number>> genreData = getGenreData(conn);
            System.out.println("Genre data loaded: " + genreData.size() + " series");
            if (!genreData.isEmpty() && !genreData.get(0).getData().isEmpty()) {
                System.out.println("First genre series has: " + genreData.get(0).getData().size() + " data points");
            }
            genreChart.setData(genreData);

            // Load borrowing trends line chart
            ObservableList<XYChart.Series<String, Number>> trendData = getBorrowingTrendData(conn);
            System.out.println("Borrowing trend data loaded: " + trendData.size() + " series");
            if (!trendData.isEmpty() && !trendData.get(0).getData().isEmpty()) {
                System.out.println("Trend series has: " + trendData.get(0).getData().size() + " data points");
            }
            borrowingTrendChart.setData(trendData);

            // Load student activity bar chart
            ObservableList<XYChart.Series<String, Number>> activityData = getStudentActivityData(conn);
            System.out.println("Student activity data loaded: " + activityData.size() + " series");
            studentActivityChart.setData(activityData);

            System.out.println("Chart data loading completed successfully");
        } catch (Exception e) {
            System.err.println("Error loading chart data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setDefaultValues() {
        totalBooksLabel.setText("0");
        totalStudentsLabel.setText("0");
        totalBorrowedLabel.setText("0");
        totalGenresLabel.setText("0");
        availableBooksLabel.setText("0");
        todayAttendanceLabel.setText("0");
        overdueBooksLabel.setText("0");

        // Set default chart data
        bookStatusChart.setData(FXCollections.observableArrayList());
        genreChart.setData(FXCollections.observableArrayList());
        borrowingTrendChart.setData(FXCollections.observableArrayList());
        studentActivityChart.setData(FXCollections.observableArrayList());
    }

    // Utility methods for additional statistics (can be expanded)
    private int getTodayAttendance(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM log WHERE DATE(log_date) = CURDATE()";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }

    private int getOverdueBooks(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM borrow WHERE return_date IS NULL AND due_date < CURDATE()";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }

    private int getAvailableBooks(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM book WHERE isAvailable = 'Available'";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }

    // Chart data methods
    private ObservableList<PieChart.Data> getBookStatusData(Connection conn) throws SQLException {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        String query = "SELECT isAvailable, COUNT(*) as count FROM book GROUP BY isAvailable";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("isAvailable");
                int count = rs.getInt("count");
                System.out.println("Book status: " + status + " = " + count);
                data.add(new PieChart.Data(status, count));
            }
        }
        return data;
    }

    private ObservableList<XYChart.Series<String, Number>> getGenreData(Connection conn) throws SQLException {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
        XYChart.Series<String, Number> genreSeries = new XYChart.Series<>();
        genreSeries.setName("Books by Genre");

        String query = "SELECT c.category_name as category, COUNT(*) as count " +
                       "FROM books b " +
                       "JOIN book_categories bc ON b.book_id = bc.book_id " +
                       "JOIN categories c ON bc.category_id = c.category_id " +
                       "GROUP BY c.category_name ORDER BY count DESC LIMIT 10";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String genre = rs.getString("category");
                if (genre == null || genre.trim().isEmpty()) {
                    genre = "Uncategorized";
                }
                int count = rs.getInt("count");
                System.out.println("Genre: " + genre + " = " + count);
                genreSeries.getData().add(new XYChart.Data<>(genre, count));
            }
        }
        series.add(genreSeries);
        return series;
    }

    private ObservableList<XYChart.Series<String, Number>> getBorrowingTrendData(Connection conn) throws SQLException {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
        XYChart.Series<String, Number> trendSeries = new XYChart.Series<>();
        trendSeries.setName("Borrowings");

        String query = "SELECT DATE_FORMAT(borrow_date, '%Y-%m') as month, COUNT(*) as count " +
                      "FROM borrow " +
                      "WHERE borrow_date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
                      "GROUP BY DATE_FORMAT(borrow_date, '%Y-%m') " +
                      "ORDER BY month";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("count");
                trendSeries.getData().add(new XYChart.Data<>(month, count));
            }
        }
        series.add(trendSeries);
        return series;
    }

    private ObservableList<XYChart.Series<String, Number>> getStudentActivityData(Connection conn) throws SQLException {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();

        // Active borrowers (students who have borrowed in the last 30 days)
        XYChart.Series<String, Number> activeSeries = new XYChart.Series<>();
        activeSeries.setName("Active Borrowers");

        String activeQuery = "SELECT COUNT(DISTINCT student_id) as count FROM borrow " +
                           "WHERE borrow_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        try (PreparedStatement stmt = conn.prepareStatement(activeQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                activeSeries.getData().add(new XYChart.Data<>("Active (30 days)", rs.getInt("count")));
            }
        }

        // Total students
        XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
        totalSeries.setName("Total Students");

        String totalQuery = "SELECT COUNT(*) as count FROM student";
        try (PreparedStatement stmt = conn.prepareStatement(totalQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalSeries.getData().add(new XYChart.Data<>("Total Students", rs.getInt("count")));
            }
        }

        // Students with overdue books
        XYChart.Series<String, Number> overdueSeries = new XYChart.Series<>();
        overdueSeries.setName("Students with Overdue");

        String overdueQuery = "SELECT COUNT(DISTINCT student_id) as count FROM borrow " +
                            "WHERE return_date IS NULL AND due_date < CURDATE()";
        try (PreparedStatement stmt = conn.prepareStatement(overdueQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                overdueSeries.getData().add(new XYChart.Data<>("Overdue", rs.getInt("count")));
            }
        }

        series.addAll(activeSeries, totalSeries, overdueSeries);
        return series;
    }
}
