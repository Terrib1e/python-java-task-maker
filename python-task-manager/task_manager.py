import json
import os
import argparse
from datetime import datetime
from typing import List, Dict, Optional


class Task:
    """Represents a single task with attributes like title, description, status, and timestamps."""
    
    def __init__(self, title: str, description: str = "", task_id: Optional[int] = None):
        self.id = task_id
        self.title = title
        self.description = description
        self.status = "pending"
        self.created_at = datetime.now().isoformat()
        self.completed_at = None
    
    def complete(self) -> None:
        """Mark the task as completed with timestamp."""
        self.status = "completed"
        self.completed_at = datetime.now().isoformat()
    
    def to_dict(self) -> Dict:
        """Convert task to dictionary for JSON serialization."""
        return {
            "id": self.id,
            "title": self.title,
            "description": self.description,
            "status": self.status,
            "created_at": self.created_at,
            "completed_at": self.completed_at
        }
    
    @classmethod
    def from_dict(cls, data: Dict) -> 'Task':
        """Create task from dictionary (for JSON deserialization)."""
        task = cls(data["title"], data["description"], data["id"])
        task.status = data["status"]
        task.created_at = data["created_at"]
        task.completed_at = data.get("completed_at")
        return task
    
    def __str__(self) -> str:
        """String representation of the task."""
        status_symbol = "✓" if self.status == "completed" else "○"
        return f"[{status_symbol}] {self.id}: {self.title}"


class TaskManager:
    """Manages a collection of tasks with persistence to JSON file."""
    
    def __init__(self, filename: str = "tasks.json"):
        self.filename = filename
        self.tasks: List[Task] = []
        self.next_id = 1
        self.load_tasks()
    
    def load_tasks(self) -> None:
        """Load tasks from JSON file if it exists."""
        if os.path.exists(self.filename):
            try:
                with open(self.filename, 'r') as f:
                    data = json.load(f)
                    self.tasks = [Task.from_dict(task_data) for task_data in data["tasks"]]
                    self.next_id = data.get("next_id", 1)
            except (json.JSONDecodeError, KeyError) as e:
                print(f"Error loading tasks: {e}")
                self.tasks = []
                self.next_id = 1
    
    def save_tasks(self) -> None:
        """Save tasks to JSON file."""
        data = {
            "tasks": [task.to_dict() for task in self.tasks],
            "next_id": self.next_id
        }
        with open(self.filename, 'w') as f:
            json.dump(data, f, indent=2)
    
    def add_task(self, title: str, description: str = "") -> Task:
        """Add a new task to the manager."""
        task = Task(title, description, self.next_id)
        self.tasks.append(task)
        self.next_id += 1
        self.save_tasks()
        return task
    
    def list_tasks(self, status: Optional[str] = None) -> List[Task]:
        """List all tasks or filter by status."""
        if status:
            return [task for task in self.tasks if task.status == status]
        return self.tasks
    
    def get_task(self, task_id: int) -> Optional[Task]:
        """Get a specific task by ID."""
        for task in self.tasks:
            if task.id == task_id:
                return task
        return None
    
    def complete_task(self, task_id: int) -> bool:
        """Mark a task as completed."""
        task = self.get_task(task_id)
        if task and task.status != "completed":
            task.complete()
            self.save_tasks()
            return True
        return False
    
    def delete_task(self, task_id: int) -> bool:
        """Delete a task by ID."""
        task = self.get_task(task_id)
        if task:
            self.tasks.remove(task)
            self.save_tasks()
            return True
        return False
    
    def get_statistics(self) -> Dict[str, int]:
        """Get statistics about tasks."""
        total = len(self.tasks)
        completed = len([t for t in self.tasks if t.status == "completed"])
        pending = total - completed
        return {
            "total": total,
            "completed": completed,
            "pending": pending
        }


def main():
    """Main entry point for the command-line interface."""
    parser = argparse.ArgumentParser(
        description="Simple Task Manager - A Python CLI application to manage your tasks"
    )
    subparsers = parser.add_subparsers(dest="command", help="Available commands")
    
    # Add command
    add_parser = subparsers.add_parser("add", help="Add a new task")
    add_parser.add_argument("title", help="Task title")
    add_parser.add_argument("-d", "--description", default="", help="Task description")
    
    # List command
    list_parser = subparsers.add_parser("list", help="List tasks")
    list_parser.add_argument("-s", "--status", choices=["pending", "completed"], help="Filter by status")
    
    # Complete command
    complete_parser = subparsers.add_parser("complete", help="Mark a task as completed")
    complete_parser.add_argument("task_id", type=int, help="Task ID to complete")
    
    # Delete command
    delete_parser = subparsers.add_parser("delete", help="Delete a task")
    delete_parser.add_argument("task_id", type=int, help="Task ID to delete")
    
    # Stats command
    stats_parser = subparsers.add_parser("stats", help="Show task statistics")
    
    args = parser.parse_args()
    
    # Initialize task manager
    manager = TaskManager()
    
    # Execute commands
    if args.command == "add":
        task = manager.add_task(args.title, args.description)
        print(f"Task added: {task}")
    
    elif args.command == "list":
        tasks = manager.list_tasks(args.status)
        if not tasks:
            print("No tasks found.")
        else:
            print("\nTasks:")
            print("-" * 50)
            for task in tasks:
                print(task)
                if task.description:
                    print(f"    Description: {task.description}")
            print("-" * 50)
    
    elif args.command == "complete":
        if manager.complete_task(args.task_id):
            print(f"Task {args.task_id} marked as completed!")
        else:
            print(f"Task {args.task_id} not found or already completed.")
    
    elif args.command == "delete":
        if manager.delete_task(args.task_id):
            print(f"Task {args.task_id} deleted.")
        else:
            print(f"Task {args.task_id} not found.")
    
    elif args.command == "stats":
        stats = manager.get_statistics()
        print("\nTask Statistics:")
        print(f"Total tasks: {stats['total']}")
        print(f"Completed: {stats['completed']}")
        print(f"Pending: {stats['pending']}")
        if stats['total'] > 0:
            completion_rate = (stats['completed'] / stats['total']) * 100
            print(f"Completion rate: {completion_rate:.1f}%")
    
    else:
        parser.print_help()


if __name__ == "__main__":
    main()