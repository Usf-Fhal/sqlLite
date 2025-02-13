# DUMB SQL  

A simple SQLite clone implemented in Java as a way to experiment with database concepts.  

## 🚀 Current Features  
- Supports table creation with basic data types: `INT`, `STRING`, `DATE`, `FLOAT`, `BOOLEAN`.  
- Supported SQL clauses: `SELECT`, `INSERT`, `CREATE`.  
- **No data persistence** (data is lost when the program exits).  

## 🛠️ To-Do List  
- **Support for:** `DELETE`, `UPDATE`, `ALTER` statements.  
- **Indexing support** for faster queries.  
- **Data persistence** to retain information between sessions.  

## 📂 Project Structure  
📦 DUMB SQL  
├── 📂 src/  
│   ├── 📂 Banner/            # ASCII Art Banner  
│   ├── 📂 DataBase/MetaData/ # Handles table and column metadata  
│   ├── 📂 Exceptions/        # Custom exception handling  
│   ├── 📂 ExecutionEngine/   # SQL execution logic   
│   ├── 📂 Parser/            # SQL query parsing  
│   ├── 📂 Serialization/     # Data storage and retrieval  
│   ├── 📂 StorageEngine/     # Low-level data management  
│   ├── 📂 Utils/             # Utility functions  
│   └── 📄 SQLiteShell.java   # Entry Point  
└── 📄 README.md              # Project documentation  
  


## 📝 Simple Test Case  
Welcome to the SQLite Shell. Type `'sql'` to enter SQL mode, `'exit'` to quit.  

```sh
> sql  
Entered SQL mode. Type your SQL queries or 'exit' to leave.
SQL> CREATE TABLE USER ( ID: INT , NAME: STRING );
SQL> INSERT INTO TABLE USER ( ID , NAME ) VALUES ( 25 , 'youssef' );
SQL> INSERT INTO TABLE USER ( ID , NAME ) VALUES ( 24 , 'Amine' );
SQL> SELECT * FROM USER;
+----+---------+
| ID | NAME    |
+----+---------+
| 25 | youssef |
| 24 | Amine   |
+----+---------+
