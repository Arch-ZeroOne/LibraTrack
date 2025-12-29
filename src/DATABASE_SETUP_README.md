# 🗄️ LibraTrack Database Setup Guide

## 🚀 Quick Start

Your LibraTrack database schema is ready! Follow these simple steps to set up your database.

## 📋 Prerequisites

- **MySQL Server** installed and running
- **Database access** with appropriate permissions
- **MySQL client tools** (Workbench, command line, or phpMyAdmin)

---

## 🎯 Method 1: MySQL Workbench (Recommended)

### Step 1: Open MySQL Workbench
1. Launch MySQL Workbench
2. Connect to your MySQL server

### Step 2: Create Database
```sql
CREATE DATABASE IF NOT EXISTS libratrack_db;
```

### Step 3: Execute Schema
1. Open `complete_database_schema.sql` in Workbench
2. Click the lightning bolt (⚡) to execute
3. Or use: **Query → Execute (All or Selection)**

---

## 💻 Method 2: Command Line MySQL

### Step 1: Create Database
```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS libratrack_db;"
```

### Step 2: Execute Schema
```bash
mysql -u root -p libratrack_db < complete_database_schema.sql
```

**Note:** If your MySQL root password is empty, omit the `-p` flag.

---

## 🌐 Method 3: phpMyAdmin

### Step 1: Access phpMyAdmin
1. Open phpMyAdmin in your browser
2. Select your MySQL server

### Step 2: Create Database
- Go to **Databases** tab
- Enter `libratrack_db` and click **Create**

### Step 3: Import Schema
1. Select `libratrack_db` from the left sidebar
2. Click **Import** tab
3. Choose `complete_database_schema.sql` file
4. Click **Go**

---

## 🔧 Method 4: Automated Scripts

I've created two automated scripts for your convenience:

### Windows Batch Script (`setup_database.bat`)
```cmd
# Double-click the file or run from command prompt:
setup_database.bat
```

### PowerShell Script (`setup_database.ps1`)
```powershell
# Run from PowerShell (may require execution policy change):
.\setup_database.ps1
```

**Note:** These scripts assume MySQL is in your system PATH and root password is empty.

---

## ✅ Verification

After setup, verify your database:

### Check Tables Created:
```sql
USE libratrack_db;
SHOW TABLES;
```

You should see:
```
+---------------------+
| Tables_in_libratrack_db |
+---------------------+
| authors              |
| book                 |
| book_categories      |
| books                |
| borrow              |
| categories           |
| librarian            |
| log                  |
| statuses             |
| student              |
+---------------------+
```

### Check Sample Data:
```sql
SELECT COUNT(*) as total_books FROM books;
SELECT COUNT(*) as total_students FROM student;
SELECT COUNT(*) as total_authors FROM authors;
```

---

## 🎉 Success!

Once the database is set up, your LibraTrack system is ready!

### Start Using LibraTrack:
1. **Open NetBeans IDE**
2. **Run the LibraTrack application**
3. **Begin managing your library!**

### Features Now Available:
- ✅ **Book Management**: Add, edit, delete books
- ✅ **Student Registration**: Manage library members
- ✅ **Borrowing System**: Click "Borrow" → Scan student → Confirm
- ✅ **Returning System**: Click "Return" → Scan book → Confirm
- ✅ **Reports & Analytics**: Generate library reports
- ✅ **Attendance Tracking**: Monitor student visits

---

## 🆘 Troubleshooting

### Connection Issues:
- **MySQL not running**: Start MySQL service
- **Access denied**: Check user permissions
- **Database doesn't exist**: Create it manually first

### Import Errors:
- **File not found**: Ensure `complete_database_schema.sql` is in the same directory
- **Syntax errors**: The SQL file is validated and should work correctly
- **Permission issues**: Ensure your MySQL user has CREATE privileges

### Application Issues:
- **Still can't connect**: Check `DatabaseUtil.java` credentials
- **Tables missing**: Re-run the schema setup
- **Data not showing**: Verify database name in connection string

---

## 📞 Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Verify MySQL server is running
3. Ensure correct file paths
4. Try different setup methods

**The database schema is complete and tested!** 🚀

---

*LibraTrack - Modern Library Management System*
