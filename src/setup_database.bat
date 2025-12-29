@echo off
echo ========================================
echo   LibraTrack Database Setup Script
echo ========================================
echo.

echo Checking MySQL availability...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MySQL client not found in PATH.
    echo Please ensure MySQL is installed and added to your system PATH.
    echo.
    echo You can also run the SQL file manually in MySQL Workbench or phpMyAdmin.
    pause
    exit /b 1
)

echo MySQL client found. Attempting to connect...
echo.

mysql -u root -p"" -e "CREATE DATABASE IF NOT EXISTS libratrack_db;" 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Cannot connect to MySQL server.
    echo Please check:
    echo - MySQL server is running
    echo - Root password is empty (or modify the script)
    echo - MySQL service is accessible
    echo.
    pause
    exit /b 1
)

echo Database connection successful!
echo Executing complete schema...
echo.

mysql -u root -p"" libratrack_db < complete_database_schema.sql
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to execute schema. Check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   SUCCESS! Database setup completed!
echo ========================================
echo.
echo LibraTrack database is now ready with all tables created.
echo.
echo Next steps:
echo 1. Open NetBeans IDE
echo 2. Run the LibraTrack application
echo 3. Start managing your library!
echo.
echo Tables created:
echo - authors, categories, statuses
echo - books, book_categories, book
echo - student, librarian, borrow, log
echo.
pause
