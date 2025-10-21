// MongoDB reset script for Library Database System
// Usage (mongosh): mongosh --quiet --file "database/mongodb/reset_mongo.js"
// Usage (legacy mongo): mongo --quiet --eval "load('database/mongodb/reset_mongo.js')"

(function() {
  function log(msg) { print(`[mongo-reset] ${msg}`); }
  function fail(msg) { print(`[mongo-reset][ERROR] ${msg}`); throw new Error(msg); }

  // Select database in a way compatible with both mongo and mongosh
  var dbName = 'library_db';
  if (typeof db === 'undefined') {
    // legacy mongo script run with new Mongo()
    var conn = new Mongo('localhost:27017');
    this.db = conn.getDB(dbName);
  } else {
    // Both shells support getSiblingDB
    db = db.getSiblingDB(dbName);
  }

  // Ping to verify connectivity
  log(`Connecting to '${dbName}'...`);
  var pingRes = db.runCommand({ ping: 1 });
  if (!pingRes || pingRes.ok !== 1) fail('Unable to ping MongoDB server.');
  log('Connection OK.');

  // Step 1: Drop all existing collections
  log('Step 1: Dropping existing collections...');
  var existing = db.getCollectionNames();
  existing.forEach(function(name) {
    try {
      db.getCollection(name).drop();
      log(`Dropped collection '${name}'.`);
    } catch (e) {
      fail(`Failed to drop '${name}': ${e.message}`);
    }
  });
  log('All existing collections dropped.');

  // Step 2: Recreate structure (collections + indexes)
  log('Step 2: Creating collections and indexes...');
  var students = db.getCollection('students');
  var books = db.getCollection('books');
  var loans = db.getCollection('loans');
  var fines = db.getCollection('fines');
  var counters = db.getCollection('counters');

  [students, books, loans, fines, counters].forEach(function(c) { c.insertOne({ _init: true }); c.deleteOne({ _init: true }); });

  // Indexes (data integrity and fast lookups)
  students.createIndex({ student_id: 1 }, { name: 'student_id_unique', unique: true });
  students.createIndex({ email: 1 }, { name: 'email_unique', unique: true });
  books.createIndex({ book_id: 1 }, { name: 'book_id_unique', unique: true });
  books.createIndex({ isbn: 1 }, { name: 'isbn_unique', unique: true });
  loans.createIndex({ loan_id: 1 }, { name: 'loan_id_unique', unique: true });
  loans.createIndex({ student_id: 1 }, { name: 'loan_student_idx' });
  loans.createIndex({ book_id: 1 }, { name: 'loan_book_idx' });
  loans.createIndex({ status: 1 }, { name: 'loan_status_idx' });
  fines.createIndex({ fine_id: 1 }, { name: 'fine_id_unique', unique: true });
  fines.createIndex({ student_id: 1 }, { name: 'fine_student_idx' });
  fines.createIndex({ loan_id: 1 }, { name: 'fine_loan_idx' });

  log('Collections and indexes created.');

  // Step 3: Insert predefined demo data (aligned with existing seed.js)
  log('Step 3: Inserting demo data...');
  var studentsData = [
    { student_id: 1001, name: 'John Smith', email: 'john.smith@email.com', phone: '+44 7700 900001', address: '10 Library Lane', registration_date: '2025-05-15' },
    { student_id: 1002, name: 'Jane Doe', email: 'jane.doe@email.com', phone: '+44 7700 900002', address: '22 Archive Avenue', registration_date: '2025-06-10' },
    { student_id: 1003, name: 'Bob Johnson', email: 'bob.johnson@email.com', phone: '+44 7700 900003', address: '33 Research Road', registration_date: '2025-07-20' }
  ];
  var booksData = [
    { book_id: 2001, title: 'Database Systems', author: 'Silberschatz, Korth, Sudarshan', isbn: '9780078022159', category: 'Databases', publication_year: 2020, available_copies: 2, total_copies: 5 },
    { book_id: 2002, title: 'Effective Java', author: 'Joshua Bloch', isbn: '9780134685991', category: 'Programming', publication_year: 2018, available_copies: 1, total_copies: 4 },
    { book_id: 2003, title: 'Clean Code', author: 'Robert C. Martin', isbn: '9780132350884', category: 'Programming', publication_year: 2008, available_copies: 0, total_copies: 3 }
  ];
  var loansData = [
    { loan_id: 3001, student_id: 1001, book_id: 2001, loan_date: '2025-08-01', due_date: '2025-08-15', return_date: null, status: 'ACTIVE', renewal_count: 0 },
    { loan_id: 3002, student_id: 1002, book_id: 2002, loan_date: '2025-09-05', due_date: '2025-09-19', return_date: '2025-09-16', status: 'RETURNED', renewal_count: 1 },
    { loan_id: 3003, student_id: 1003, book_id: 2003, loan_date: '2025-09-20', due_date: '2025-10-04', return_date: null, status: 'ACTIVE', renewal_count: 0 }
  ];
  var finesData = [
    { fine_id: 4001, student_id: 1001, loan_id: 3001, fine_amount: NumberDecimal('12.50'), fine_date: '2025-08-25', payment_date: null, payment_status: 'UNPAID' },
    { fine_id: 4002, student_id: 1002, loan_id: 3002, fine_amount: NumberDecimal('5.00'), fine_date: '2025-09-18', payment_date: '2025-09-20', payment_status: 'PAID' }
  ];

  try {
    var sRes = students.insertMany(studentsData);
    var bRes = books.insertMany(booksData);
    var lRes = loans.insertMany(loansData);
    var fRes = fines.insertMany(finesData);
    counters.insertMany([
      { _id: 'student_id', value: 1003 },
      { _id: 'book_id', value: 2003 },
      { _id: 'loan_id', value: 3003 },
      { _id: 'fine_id', value: 4002 }
    ]);
    log(`Inserted students: ${sRes.insertedIds.length}`);
    log(`Inserted books: ${bRes.insertedIds.length}`);
    log(`Inserted loans: ${lRes.insertedIds.length}`);
    log(`Inserted fines: ${fRes.insertedIds.length}`);
  } catch (e) {
    fail(`Insert error: ${e.message}`);
  }

  // Step 4: Validate data integrity and relationships
  log('Step 4: Validating relationships...');
  var missingRefs = [];
  loans.find().forEach(function(loan) {
    if (students.count({ student_id: loan.student_id }) === 0) missingRefs.push(`Loan ${loan.loan_id} missing student ${loan.student_id}`);
    if (books.count({ book_id: loan.book_id }) === 0) missingRefs.push(`Loan ${loan.loan_id} missing book ${loan.book_id}`);
  });
  fines.find().forEach(function(fine) {
    if (loans.count({ loan_id: fine.loan_id }) === 0) missingRefs.push(`Fine ${fine.fine_id} missing loan ${fine.loan_id}`);
    if (students.count({ student_id: fine.student_id }) === 0) missingRefs.push(`Fine ${fine.fine_id} missing student ${fine.student_id}`);
  });
  if (missingRefs.length > 0) {
    missingRefs.forEach(function(msg){ print(`[mongo-reset][RELATIONSHIP-ERROR] ${msg}`); });
    fail('Relationship validation failed.');
  }
  log('Relationships validated successfully.');

  // Step 5: Summaries and success confirmation
  var sCount = students.countDocuments ? students.countDocuments({}) : students.count({});
  var bCount = books.countDocuments ? books.countDocuments({}) : books.count({});
  var lCount = loans.countDocuments ? loans.countDocuments({}) : loans.count({});
  var fCount = fines.countDocuments ? fines.countDocuments({}) : fines.count({});
  log(`Final counts — students: ${sCount}, books: ${bCount}, loans: ${lCount}, fines: ${fCount}`);
  log('MongoDB reset completed successfully.');

  // Exit with success code when possible
  if (typeof quit === 'function') { quit(0); }
  else if (typeof exit === 'function') { exit(0); }
})();