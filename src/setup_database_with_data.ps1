# LibraTrack Complete Database Setup with Sample Data
# =====================================================

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  LibraTrack Database Setup with Data" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Check if MySQL is available
Write-Host "Checking MySQL availability..." -ForegroundColor Yellow
try {
    $mysqlVersion = mysql --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ MySQL client found" -ForegroundColor Green
    } else {
        throw "MySQL not found"
    }
} catch {
    Write-Host "❌ ERROR: MySQL client not found in PATH." -ForegroundColor Red
    Write-Host "Please ensure MySQL is installed and added to your system PATH." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Test MySQL connection
Write-Host "Testing MySQL connection..." -ForegroundColor Yellow
try {
    mysql -u root -p"" -e "SELECT 1;" 2>$null | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Cannot connect to MySQL"
    }
} catch {
    Write-Host "❌ ERROR: Cannot connect to MySQL server." -ForegroundColor Red
    Write-Host "Please check:" -ForegroundColor Red
    Write-Host "  - MySQL server is running" -ForegroundColor Red
    Write-Host "  - Root password is empty (modify script if different)" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✓ MySQL connection successful" -ForegroundColor Green
Write-Host ""

# Create database
Write-Host "Creating/verifying database..." -ForegroundColor Yellow
try {
    mysql -u root -p"" -e "CREATE DATABASE IF NOT EXISTS libratrack_db;" 2>$null | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Cannot create database"
    }
} catch {
    Write-Host "❌ ERROR: Failed to create database." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✓ Database 'libratrack_db' ready" -ForegroundColor Green
Write-Host ""

# Execute schema
Write-Host "Executing database schema..." -ForegroundColor Yellow
try {
    $schemaResult = mysql -u root -p"" libratrack_db -e "SOURCE complete_database_schema.sql;" 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Schema execution output:" -ForegroundColor Yellow
        Write-Host $schemaResult -ForegroundColor Gray
        throw "Schema execution failed"
    }
} catch {
    Write-Host "❌ ERROR: Failed to execute database schema." -ForegroundColor Red
    Write-Host "Check that 'complete_database_schema.sql' exists in the current directory." -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✓ Database schema created successfully" -ForegroundColor Green
Write-Host ""

# Load sample data
Write-Host "Loading sample data..." -ForegroundColor Yellow
try {
    $seedResult = mysql -u root -p"" libratrack_db -e "SOURCE database_seed.sql;" 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ WARNING: Failed to load sample data." -ForegroundColor Yellow
        Write-Host "The database schema is ready, but sample data was not inserted." -ForegroundColor Yellow
        Write-Host "You can manually run database_seed.sql later." -ForegroundColor Yellow
    } else {
        Write-Host "✓ Sample data loaded successfully" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ WARNING: Exception during data loading." -ForegroundColor Yellow
    Write-Host "Database schema is ready, but sample data may be incomplete." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Green
Write-Host "      SUCCESS! Database Ready!" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host ""
Write-Host "LibraTrack database has been set up successfully!" -ForegroundColor Cyan
Write-Host ""

if ($LASTEXITCODE -eq 0) {
    Write-Host "Sample data included:" -ForegroundColor Cyan
    Write-Host "  📚 20+ books across multiple genres" -ForegroundColor White
    Write-Host "  👥 20 active students" -ForegroundColor White
    Write-Host "  📖 30+ book copies (some borrowed)" -ForegroundColor White
    Write-Host "  📅 Attendance records" -ForegroundColor White
    Write-Host "  🔐 Admin and librarian accounts" -ForegroundColor White
    Write-Host ""
}

Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Run the LibraTrack application" -ForegroundColor White
Write-Host "2. Login with admin/admin123" -ForegroundColor White
Write-Host "3. Start testing all features!" -ForegroundColor White
Write-Host ""
Write-Host "For testing help, see SAMPLE_DATA_README.md" -ForegroundColor Cyan
Write-Host ""

Read-Host "Press Enter to finish"
