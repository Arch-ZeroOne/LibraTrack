# 📚 LibraTrack Sample Data Guide

## 🎯 **Database Seeding Complete!**

Your LibraTrack database now contains comprehensive sample data for testing all system features. Here's what has been added and how to use it.

---

## 📊 **Sample Data Overview**

### **📖 Books & Authors (20+ books)**
- **Fantasy**: Harry Potter series, Game of Thrones, Lord of the Rings
- **Science Fiction**: Dune, Neuromancer, 2001: A Space Odyssey
- **Mystery**: Agatha Christie classics
- **Horror**: Stephen King novels
- **Literary Fiction**: Handmaid's Tale, Americanah, Normal People
- **Young Adult**: Neil Gaiman books

### **👥 Students (20 active students)**
- **Computer Science**: 5 students
- **Information Technology**: 5 students
- **Business Administration**: 5 students
- **Engineering**: 5 students
- **Inactive students**: 2 (for testing)

### **📚 Book Copies (30+ physical copies)**
- Multiple copies of popular books
- Mix of available and borrowed books
- Different condition statuses

### **📅 Borrowing Records (5 active borrows)**
- Real borrowing transactions
- Due dates and return tracking

### **📊 Attendance Logs (15+ records)**
- Today's attendance
- Historical data for testing

---

## 🔑 **Login Credentials**

### **Admin/Librarian Accounts**
| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | System Administrator |
| `mrodriguez` | `lib123` | Librarian |
| `cmartinez` | `lib456` | Librarian |

---

## 🧪 **Testing Scenarios**

### **📚 Book Management Testing**

#### **View Books**
- Navigate to **Books** section
- Browse 20+ books across multiple genres
- Test search functionality

#### **Borrow Books**
1. Click on any **"Borrow"** button
2. Scan student barcode or use manual entry:
   - `2021001` (Juan Dela Cruz)
   - `2021002` (Maria Garcia)
   - `2021003` (Jose Reyes)
3. Set borrow/due dates
4. Confirm borrowing

#### **Return Books**
1. Find borrowed books (marked with "Return" button)
2. Click **"Return"** button
3. Scan book ISBN to verify:
   - `9780747532699` (Harry Potter 1)
   - `9780553103540` (Game of Thrones)
   - `9780007119318` (Murder on Orient Express)
4. Confirm return

### **👥 Student Management Testing**

#### **View Students**
- Navigate to **Students** section
- See 20 active students
- Test search and filtering

#### **Student Details**
- Active students: 20 total
- Inactive students: 2 (for testing status filters)
- Multiple courses represented

### **📊 Dashboard Testing**

#### **Statistics Display**
- **Total Books**: 20+ titles
- **Total Students**: 20 active
- **Borrowed Books**: 5 currently borrowed
- **Available Books**: 25+ available
- **Today's Attendance**: 5 students
- **Overdue Books**: 0 (fresh data)

### **📅 Attendance Logging**

#### **Log Attendance**
1. Click **"Attendance"** button
2. Scan student barcode:
   - `2021001` - `2021005` (already logged today)
   - `2021006` - `2021020` (available for logging)
3. Verify student information appears
4. Confirm attendance logging

#### **View Attendance Statistics**
- Today's count: 5 students
- Historical data available
- Monthly tracking

---

## 🔍 **Barcode Testing**

### **Student Barcodes**
```
2021001 - Juan Dela Cruz (Computer Science)
2021002 - Maria Garcia (Computer Science)
2021003 - Jose Reyes (Computer Science)
2021004 - Ana Torres (Computer Science)
2021005 - Miguel Flores (Computer Science)
2021006 - Carmen Vasquez (IT)
2021007 - Roberto Herrera (IT)
2021008 - Isabella Morales (IT)
2021009 - Diego Gutierrez (IT)
2021010 - Sofia Ortega (IT)
```

### **Book ISBNs for Testing**
```
9780747532699 - Harry Potter and the Philosopher's Stone
9780747538493 - Harry Potter and the Chamber of Secrets
9780553103540 - A Game of Thrones
9780441172719 - Dune
9780441569595 - Neuromancer
9780007119318 - Murder on the Orient Express
9780307743657 - The Shining
```

---

## 📈 **Expected Dashboard Values**

After setup, your dashboard should show:

| Metric | Expected Value |
|--------|----------------|
| **Total Books** | 20 |
| **Total Students** | 20 |
| **Borrowed Books** | 5 |
| **Available Books** | 25+ |
| **Today's Attendance** | 5 |
| **Overdue Books** | 0 |

---

## 🔄 **Borrowing Workflow Testing**

### **Complete Borrowing Cycle**

1. **Borrow Book**:
   - Book: "Harry Potter and the Philosopher's Stone"
   - Student: Juan Dela Cruz (2021001)
   - Result: Book status changes to "borrowed"

2. **Check Dashboard**:
   - Borrowed Books: Increases by 1
   - Available Books: Decreases by 1

3. **Return Book**:
   - Find the borrowed Harry Potter book
   - Click "Return", scan ISBN: 9780747532699
   - Result: Book status changes back to "available"

4. **Verify Statistics**:
   - Numbers return to original values

---

## 🎯 **Advanced Testing Scenarios**

### **Multiple Borrows**
- Borrow 3-4 books to different students
- Verify dashboard updates correctly
- Test return process for multiple books

### **Attendance Tracking**
- Log attendance for 5-10 different students
- Check daily attendance counter updates
- Verify historical data preservation

### **Search & Filter Testing**
- Test book search by title, author, ISBN
- Test student search by name, ID, course
- Verify category filtering works

### **Status Management**
- Test active/inactive student filtering
- Verify book availability status updates
- Check borrowing restrictions for inactive students

---

## 🚨 **Troubleshooting**

### **Dashboard Shows Wrong Numbers**
- Run `verify_database.sql` to check data integrity
- Ensure all SQL files executed successfully
- Check database connections

### **Borrowing Not Working**
- Verify student is active
- Check book availability status
- Ensure proper barcode format

### **Search Not Finding Results**
- Check spelling and case sensitivity
- Verify data was inserted correctly
- Test with different search terms

---

## 📋 **Data Reset (If Needed)**

To reset sample data:
```sql
-- Clear all data (be careful!)
DELETE FROM log;
DELETE FROM borrow;
DELETE FROM book;
DELETE FROM book_categories;
DELETE FROM books;
DELETE FROM student;
DELETE FROM librarian;
DELETE FROM authors;
DELETE FROM categories;

-- Then re-run database_seed.sql
```

---

## 🎉 **Ready for Testing!**

Your LibraTrack system is now fully populated with realistic test data. You can:

- ✅ **Test all borrowing features**
- ✅ **Verify attendance logging**
- ✅ **Check dashboard statistics**
- ✅ **Practice search and filtering**
- ✅ **Test user management**
- ✅ **Explore reporting features**

**Happy Testing!** 🚀📚✨

---

**LibraTrack** - Complete Library Management Solution
