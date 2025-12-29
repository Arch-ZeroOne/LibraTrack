# 📚 LibraTrack Library Management System - User Manual

## Welcome to LibraTrack!

LibraTrack is a modern, barcode-powered library management system designed for efficient book tracking, student management, and attendance logging. This manual will guide you through all features and operations.

---

## 🚀 Quick Start Guide

### System Requirements
- **MySQL Server** (5.7+)
- **Java Runtime Environment** (JRE 11+)
- **Barcode Scanner** (recommended for optimal performance)

### First Time Setup
1. **Database Setup**: Run the provided database schema
2. **Launch Application**: Start LibraTrack from NetBeans
3. **Initial Login**: Use admin credentials (admin/admin123)

---

## 🧭 Navigation Overview

### Main Navigation Menu
- **🏠 Dashboard**: System overview and statistics
- **📚 Books**: Manage library book collection
- **👥 Students**: Student registration and management
- **📊 Attendance**: Student check-in/out logging

### Quick Actions
- **Logout**: Securely exit the system
- **Reports**: Generate library reports

---

## 📊 Dashboard (Statistics)

The dashboard provides real-time insights into your library's performance:

### 📈 Key Metrics
- **👥 Total Students**: Active library members
- **📚 Total Books**: Complete book collection
- **🔄 Borrowed Books**: Currently checked out
- **🏷️ Genres**: Available book categories
- **✅ Available Books**: Books ready for borrowing
- **📅 Today's Attendance**: Students checked in today
- **⚠️ Overdue Books**: Books past due date

### Understanding the Data
- **Real-time Updates**: Statistics refresh automatically
- **Color-coded Cards**: Visual status indicators
- **Click to Drill-down**: Detailed views available

---

## 📚 Book Management

### Viewing Books
1. Navigate to **Books** section
2. Browse the complete book catalog
3. Use search and filters for specific books

### Book Actions

#### ➕ Adding New Books
1. Click **"Add Book"** button
2. Fill in book details:
   - Title, Author, Publisher
   - ISBN, Publication Date
   - Genre categories
3. Click **Save**

#### ✏️ Editing Books
1. Find the book in the table
2. Click **"Update"** button
3. Modify book information
4. Click **Save Changes**

#### 🗑️ Removing Books
1. Find the book in the table
2. Click **"Delete"** button
3. Confirm deletion in dialog

### 🔄 Borrowing Books
1. Find the desired book in the table
2. Click **"Borrow"** button (available for books not currently borrowed)
3. **Scan Student Barcode** or manually enter student ID
4. Set borrow and due dates
5. Click **"Confirm Borrow"**

### ↩️ Returning Books
1. Find the borrowed book in the table
2. Click **"Return"** button (available for currently borrowed books)
3. **Scan Book Barcode** to verify identity
4. Confirm return details
5. Click **"Confirm Return"**

---

## 👥 Student Management

### Viewing Students
1. Navigate to **Students** section
2. Browse registered students
3. Use search for specific students

### Student Actions

#### ➕ Registering Students
1. Click **"Add Student"** button
2. Fill in student details:
   - Full name, Student ID
   - Course/Program
   - Contact information
3. Click **Save**

#### ✏️ Updating Student Information
1. Find the student in the table
2. Click **"Update"** button
3. Modify student details
4. Click **Save Changes**

#### 🔄 Student Status Management
- **Active/Inactive**: Toggle student library access
- **Soft Delete**: Mark students as inactive instead of permanent deletion

---

## 📅 Attendance Logging

### Student Check-in Process
1. Navigate to **Attendance** section
2. Click **"Log Attendance"** button
3. **Scan Student Barcode** or manually enter student ID
4. System displays student information
5. Click **"Log Attendance"** to confirm

### Attendance Features
- **Real-time Logging**: Instant check-in recording
- **Student Verification**: Displays student details before logging
- **Daily Tracking**: Separate logs for each day
- **Historical Data**: Complete attendance history

---

## 🔍 Search & Filter Features

### Book Search
- **Title Search**: Find books by title
- **Author Search**: Search by author name
- **ISBN Lookup**: Direct barcode/ISBN search
- **Category Filter**: Filter by book genres
- **Date Filter**: Filter by publication date

### Student Search
- **Name Search**: Search by student name
- **ID Search**: Quick lookup by student ID
- **Course Filter**: Filter by program/course
- **Status Filter**: Active/inactive students

---

## 📋 Barcode Integration

### Supported Operations
- **Book Scanning**: ISBN barcode lookup
- **Student ID Scanning**: Quick student identification
- **Attendance Logging**: Fast check-in process
- **Book Return Verification**: Security scan for returns

### Barcode Best Practices
1. **Clean Barcodes**: Ensure barcodes are clean and undamaged
2. **Proper Scanning**: Hold scanner steady, proper distance
3. **Verification**: Always verify scanned data before confirming
4. **Backup Entry**: Manual entry available if scanner fails

---

## 📊 Reports & Analytics

### Available Reports
- **📈 Library Statistics**: Complete system overview
- **📚 Book Reports**: Inventory and circulation data
- **👥 Student Reports**: Registration and activity data
- **📅 Attendance Reports**: Daily/weekly/monthly attendance
- **⚠️ Overdue Reports**: Books past due date

### Generating Reports
1. Click **"Generate Report"** from main navigation
2. Select report type and date range
3. Choose export format (PDF/Excel)
4. Generate and download report

---

## 🔧 System Administration

### User Management
- **Librarian Accounts**: Create/manage library staff accounts
- **Permission Levels**: Different access levels for staff
- **Password Management**: Secure password policies

### System Maintenance
- **Database Backup**: Regular data backups recommended
- **Log Management**: Monitor system activity
- **Performance Tuning**: Optimize for your library size

---

## 🛡️ Security Features

### Access Control
- **User Authentication**: Secure login system
- **Role-based Access**: Different permissions per user type
- **Session Management**: Automatic logout for security

### Data Protection
- **Soft Deletes**: No permanent data loss
- **Audit Trails**: Complete activity logging
- **Data Validation**: Input sanitization and validation

---

## 🔧 Troubleshooting

### Common Issues

#### 📚 Books Not Appearing
- Check database connection
- Verify book status (not removed)
- Refresh the books table

#### 👥 Students Not Found
- Verify student ID format
- Check if student is marked as active
- Confirm barcode scanner settings

#### 📅 Attendance Not Logging
- Verify student exists in system
- Check barcode quality
- Confirm database permissions

#### 📊 Statistics Not Updating
- Refresh the dashboard
- Check database connectivity
- Verify view permissions

### Getting Help
1. **Check Logs**: Review application logs for error details
2. **Database Check**: Verify MySQL server is running
3. **Permissions**: Ensure proper database user permissions
4. **Network**: Check network connectivity if using remote database

---

## 📞 Technical Support

### System Information
- **Version**: LibraTrack 1.0
- **Database**: MySQL 5.7+
- **Java**: JDK 11+
- **OS**: Windows/Linux/Mac

### Contact Information
- **System Admin**: Your IT department
- **Documentation**: This manual
- **Support**: Check application logs and database status

---

## 🎯 Best Practices

### Daily Operations
1. **Morning Setup**: Verify system connectivity
2. **Regular Backups**: Daily database backups
3. **Scanner Maintenance**: Clean barcode scanners regularly
4. **End-of-Day Review**: Check daily statistics

### Library Management
1. **Regular Inventory**: Periodic book inventory checks
2. **Student Updates**: Keep student information current
3. **Overdue Management**: Regular overdue book follow-up
4. **Report Analysis**: Weekly review of library statistics

### Performance Tips
1. **Database Optimization**: Regular table maintenance
2. **Search Optimization**: Use specific search terms
3. **Batch Operations**: Process multiple items efficiently
4. **System Monitoring**: Monitor resource usage

---

## 📋 Quick Reference

### Keyboard Shortcuts
- **Enter**: Confirm barcode scan
- **Tab**: Navigate between fields
- **Ctrl+S**: Save current operation

### Status Indicators
- **🟢 Green**: Available/Active
- **🟡 Yellow**: Borrowed/Pending
- **🔴 Red**: Overdue/Unavailable

### Common Workflows
1. **New Book**: Add Book → Set Categories → Save
2. **Student Registration**: Add Student → Set Details → Activate
3. **Borrow Process**: Find Book → Borrow → Scan Student → Confirm
4. **Return Process**: Find Book → Return → Scan Book → Confirm
5. **Attendance**: Open Attendance → Scan Student → Log

---

## 🔄 System Updates

### Version History
- **v1.0**: Initial release with full library management features

### Future Features
- Mobile app integration
- Advanced reporting
- RFID integration
- Online reservation system

---

*Thank you for choosing LibraTrack! We hope this system streamlines your library operations and enhances the user experience for your students and staff.*

**📧 For technical support or questions, please contact your system administrator.**

---

**LibraTrack** - Modern Library Management, Simplified. 📚✨
