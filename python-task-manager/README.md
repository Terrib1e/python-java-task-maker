# Python Task Manager

A simple command-line task management application written in Python.

## Requirements
- Python 3.7 or higher
- No external dependencies (uses only standard library)

## Usage

### Add a task
```bash
python task_manager.py add "Task title" -d "Task description"
```

### List all tasks
```bash
python task_manager.py list
```

### List tasks by status
```bash
python task_manager.py list -s pending
python task_manager.py list -s completed
```

### Complete a task
```bash
python task_manager.py complete 1
```

### Delete a task
```bash
python task_manager.py delete 1
```

### View statistics
```bash
python task_manager.py stats
```

## Features
- Object-oriented design with Task and TaskManager classes
- JSON persistence for data storage
- Command-line interface using argparse
- Type hints for better code clarity
- Task filtering by status
- Statistics tracking
- Error handling and input validation
- ISO format timestamps