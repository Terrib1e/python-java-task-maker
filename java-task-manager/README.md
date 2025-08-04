# Java Task Manager

A simple command-line task management application written in Java.

## Requirements
- Java 11 or higher
- Maven (for dependency management)

## Setup
1. Install dependencies:
   ```bash
   mvn clean install
   ```

2. Compile the application:
   ```bash
   javac -cp "gson-2.10.1.jar" *.java
   ```
   
   Or with Maven:
   ```bash
   mvn compile
   ```

## Usage

### Add a task
```bash
java -cp ".;gson-2.10.1.jar" Main add "Task title" -d "Task description"
```

### List all tasks
```bash
java -cp ".;gson-2.10.1.jar" Main list
```

### List tasks by status
```bash
java -cp ".;gson-2.10.1.jar" Main list -s pending
java -cp ".;gson-2.10.1.jar" Main list -s completed
```

### Complete a task
```bash
java -cp ".;gson-2.10.1.jar" Main complete 1
```

### Delete a task
```bash
java -cp ".;gson-2.10.1.jar" Main delete 1
```

### View statistics
```bash
java -cp ".;gson-2.10.1.jar" Main stats
```

## Features
- Object-oriented design with Task and TaskManager classes
- JSON persistence using Gson library
- Command-line interface with argument parsing
- Task filtering by status
- Statistics tracking
- Error handling and input validation