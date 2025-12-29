# LibraTrack Database Setup Script
# ========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  LibraTrack Database Setup Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if MySQL is available
Write-Host "Checking MySQL availability..." -ForegroundColor Yellow
try {
    $mysqlVersion = mysql --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "MySQL client found: $mysqlVersion" -ForegroundColor Green
    } else {
        throw "MySQL not found"
    }
} catch {
    Write-Host "ERROR: MySQL client not found in PATH." -ForegroundColor Red
    Write-Host "Please ensure MySQL is installed and added to your system PATH." -ForegroundColor Red
    Write-Host ""
    Write-Host "Alternative: Run the SQL file manually in MySQL Workbench or phpMyAdmin." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Attempting to connect to MySQL server..." -ForegroundColor Yellow
Write-Host ""

# Try to create database
try {
    mysql -u root -p"" -e "CREATE DATABASE IF NOT EXISTS libratrack_db;" 2>$null
    if ($LASTEXITCODE -ne 0) {
        throw "Cannot connect to MySQL server"
    }
} catch {
    Write-Host "ERROR: Cannot connect to MySQL server." -ForegroundColor Red
    Write-Host "Please check:" -ForegroundColor Red
    Write-Host "- MySQL server is running" -ForegroundColor Red
    Write-Host "- Root password is empty (modify script if different)" -ForegroundColor Red
    Write-Host "- MySQL service is accessible" -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Database connection successful!" -ForegroundColor Green
Write-Host "Executing complete schema..." -ForegroundColor Yellow
Write-Host ""

# Execute the schema
try {
    $sqlContent = Get-Content "complete_database_schema.sql" -Raw
    mysql -u root -p"" libratrack_db -e $sqlContent 2>$null

    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "ERROR: Failed to execute schema." -ForegroundColor Red
        Write-Host "Check MySQL error messages above." -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
} catch {
    Write-Host ""
    Write-Host "ERROR: Exception during schema execution:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   SUCCESS! Database setup completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "LibraTrack database is now ready with all tables created." -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Open NetBeans IDE" -ForegroundColor White
Write-Host "2. Run the LibraTrack application" -ForegroundColor White
Write-Host "3. Start managing your library!" -ForegroundColor White
Write-Host ""
Write-Host "Tables created:" -ForegroundColor Yellow
Write-Host "- authors, categories, statuses" -ForegroundColor White
Write-Host "- books, book_categories, book" -ForegroundColor White
Write-Host "- student, librarian, borrow, log" -ForegroundColor White
Write-Host ""
Read-Host "Press Enter to finish"
