    # 🧱 DAO (Data Access Object) Pattern — JavaFX + JDBC Guide

> A comprehensive guide to implementing the DAO pattern in JavaFX applications with JDBC for clean, maintainable database access.

---

## 📖 Table of Contents

- [What is the DAO Pattern?](#-what-is-the-dao-pattern)
- [Architecture Overview](#️-dao-layer-architecture)
- [DAO Flow](#-dao-flow)
- [Core Components](#-dao-layer-components)
- [Best Practices](#-key-best-practices)
- [Layer Responsibilities](#-summary)
- [Advantages](#-advantages-of-the-dao-pattern)
- [References](#-references)

---

## 🧠 What is the DAO Pattern?

The **DAO (Data Access Object)** pattern is a design paradigm that:

- Encapsulates all **database access logic** inside specific classes (DAOs)
- Keeps **controllers** and **services** free from SQL code
- Makes the system **easier to maintain**, **extend**, and **test**

**Core Principle**: Separate data persistence logic from business logic and presentation layers.

---

## ⚙️ DAO Layer Architecture

### Recommended Folder Structure

```
src/main/java/com/example/app/
│
├── model/
│   ├── User.java
│   └── Product.java
│
├── dao/
│   ├── UserDAO.java
│   ├── ProductDAO.java
│   └── interfaces/
│       ├── IUserDAO.java
│       └── IProductDAO.java
│
├── util/
│   └── DBConnection.java
│
├── service/
│   └── UserService.java
│
└── controller/
    ├── LoginController.java
    └── DashboardController.java
```

---

## 🔄 DAO Flow

```
[UI / Controller] → [Service Layer] → [DAO Layer] → [Database]
```

### Example Flow

```java
// LoginController.java
boolean valid = userService.authenticate(username, password);

// UserService.java
public boolean authenticate(String username, String password) {
    return userDAO.validateUser(username, password);
}

// UserDAO.java
public boolean validateUser(String username, String password) throws SQLException {
    String query = "SELECT 1 FROM users WHERE username=? AND password=?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, username);
    ps.setString(2, password);
    ResultSet rs = ps.executeQuery();
    return rs.next();
}
```

---

## 🧩 DAO Layer Components

### 1. Model Class (POJO)

Represents one record in a database table.

```java
public class User {
    private int id;
    private String username;
    private String password;

    // Constructor
    public User() {}
    
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

### 2. DAO Interface

Defines the contract for data operations.

```java
public interface IUserDAO {
    boolean addUser(User user);
    boolean validateUser(String username, String password);
    User getUserById(int id);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
}
```

### 3. DAO Implementation

Implements data access logic using JDBC.

```java
public class UserDAO implements IUserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
```

### 4. Database Connection Utility

Provides centralized database connection management.

```java
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### 5. Controller Integration

How to use DAO in your JavaFX controllers.

```java
public class LoginController {
    private UserDAO userDAO;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;

    public LoginController() {
        try {
            this.userDAO = new UserDAO(DBConnection.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userDAO.validateUser(username, password)) {
            System.out.println("Login successful!");
            // Navigate to dashboard
        } else {
            System.out.println("Invalid credentials!");
            // Show error message
        }
    }
}
```

---

## 💡 Key Best Practices

| Principle | Description |
|-----------|-------------|
| **Keep DAO Independent** | DAO should not depend on UI or controller logic |
| **Use Interfaces** | Define clear contracts and make implementations swappable |
| **Return Model Objects** | DAO should return `User`, `Product`, etc., not raw SQL data |
| **Use Try-With-Resources** | Always close connections, statements, and result sets automatically |
| **One DAO Per Entity** | Each table should have its dedicated DAO class |
| **Handle Exceptions Properly** | Log errors and provide meaningful feedback |
| **Use PreparedStatements** | Prevent SQL injection and improve performance |
| **Avoid Business Logic in DAO** | Keep DAO focused solely on data access |

---

## 🧭 Summary

### Layer Responsibilities

| Layer | Responsibility |
|-------|----------------|
| **Controller** | Handles user events (buttons, inputs, UI interactions) |
| **Service** | Contains business logic and application rules |
| **DAO** | Executes SQL queries and manages data access |
| **Model** | Represents your database entities (POJOs) |
| **Util** | Holds reusable utilities (like database connections) |

### Architecture Visualization

```
┌──────────────────────────┐
│        Controller        │
│  (Handles UI Events)     │
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│         Service          │
│ (Business Logic Layer)   │
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│           DAO            │
│  (Database Operations)   │
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│        Database          │
└──────────────────────────┘
```

---

## ✅ Advantages of the DAO Pattern

1. **Clean Separation** - Database logic is isolated from UI and business logic
2. **Maintainability** - Changes to database structure only affect DAO classes
3. **Testability** - Easy to mock DAOs for unit testing
4. **Reusability** - DAOs can be used across multiple controllers/services
5. **Flexibility** - Easy to switch between different database implementations
6. **Single Responsibility** - Each class has one clear purpose
7. **Scalability** - New entities can be added without affecting existing code

---

## 📝 Quick Reference

### Common DAO Methods

```java
// Create
boolean add(Entity entity)

// Read
Entity getById(int id)
List<Entity> getAll()
List<Entity> findBy(criteria)

// Update
boolean update(Entity entity)

// Delete
boolean delete(int id)
boolean deleteAll()
```

### JDBC Best Practices Checklist

- ✅ Use `PreparedStatement` instead of `Statement`
- ✅ Always close resources using try-with-resources
- ✅ Handle `SQLException` appropriately
- ✅ Use connection pooling for production applications
- ✅ Never hardcode credentials (use configuration files)
- ✅ Validate input before passing to DAO
- ✅ Use transactions for multiple related operations

---

## 📘 References

- [Oracle JDBC Documentation](https://docs.oracle.com/javase/tutorial/jdbc/)
- [JavaFX Official Documentation](https://openjfx.io/)
- [DAO Pattern — Baeldung](https://www.baeldung.com/java-dao-pattern)
- [Design Patterns — Gang of Four](https://en.wikipedia.org/wiki/Design_Patterns)

---

## 👤 Author

**Windyl**

**Project Context**: JavaFX + JDBC Admin Dashboard

**Purpose**: Structured DAO Pattern Reference for consistent architecture

---

## 📄 License

This documentation is provided for educational purposes.

---

**Last Updated**: November 2025