@echo off
echo ========================================
echo    LibraTrack Database Setup
echo ========================================
echo.

echo This script will set up the LibraTrack database.
echo Make sure MySQL server is running and you have admin privileges.
echo.

mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS libratrack_db;" 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Cannot connect to MySQL server.
    echo Please check:
    echo - MySQL server is running
    echo - You have correct permissions
    echo - MySQL is in your system PATH
    pause
    exit /b 1
)

echo Database 'libratrack_db' created/verified.
echo Running complete schema setup...
echo.

mysql -u root -p libratrack_db < complete_database_schema.sql
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to execute database schema.
    echo Check the complete_database_schema.sql file exists and is valid.
    pause
    exit /b 1
)

echo Schema executed successfully!
echo Loading sample data...
echo.

mysql -u root -p libratrack_db < database_seed.sql
if %errorlevel% neq 0 (
    echo.
    echo WARNING: Failed to load sample data.
    echo The database schema is ready, but no sample data was inserted.
    echo You can manually run database_seed.sql later.
) else (
    echo Sample data loaded successfully!
)

echo.
echo ========================================
echo       SUCCESS! Database Ready!
echo ========================================
echo.
echo LibraTrack database has been set up successfully!
echo Sample data has been loaded for testing.
echo You can now run the application.
echo.
pause
