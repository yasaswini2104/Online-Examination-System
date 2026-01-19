# Online Examination System

## Project Description
A terminal-based Java application for conducting secure, time-bound online examinations with automatic evaluation and file-based persistent storage.

## Technologies Used
- Java SE (Core Java)
- File I/O for persistence
- Multithreading for timer functionality
- Collections Framework

## How to Run
```bash
# Compile
javac -d bin src/com/exam/**/*.java

# Run
java -cp bin com.exam.main.Main
```

## Folder Structure
```
Online-Examination-System/
├── data/           # File-based storage
├── src/            # Source code
│   └── com/exam/
│       ├── main/
│       ├── model/
│       ├── service/
│       ├── util/
│       └── exception/
└── docs/           # Documentation
```

## Features
- Role-based access (Admin/Student)
- Time-bound examinations
- Automatic evaluation
- Persistent storage
- Secure authentication
```

