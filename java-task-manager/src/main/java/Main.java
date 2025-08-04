import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for the command-line interface.
 */
public class Main {
    private static TaskManager manager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        manager = new TaskManager();
        scanner = new Scanner(System.in);
        
        if (args.length == 0) {
            printHelp();
            return;
        }
        
        String command = args[0];
        
        switch (command) {
            case "add":
                handleAdd(args);
                break;
            case "list":
                handleList(args);
                break;
            case "complete":
                handleComplete(args);
                break;
            case "delete":
                handleDelete(args);
                break;
            case "stats":
                handleStats();
                break;
            default:
                System.out.println("Unknown command: " + command);
                printHelp();
        }
    }
    
    /**
     * Handle add command.
     */
    private static void handleAdd(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Task title is required");
            System.out.println("Usage: java Main add \"Task title\" [-d \"Description\"]");
            return;
        }
        
        String title = args[1];
        String description = "";
        
        // Check for description flag
        for (int i = 2; i < args.length - 1; i++) {
            if (args[i].equals("-d") || args[i].equals("--description")) {
                description = args[i + 1];
                break;
            }
        }
        
        Task task = manager.addTask(title, description);
        System.out.println("Task added: " + task);
    }
    
    /**
     * Handle list command.
     */
    private static void handleList(String[] args) {
        String status = null;
        
        // Check for status filter
        for (int i = 1; i < args.length - 1; i++) {
            if (args[i].equals("-s") || args[i].equals("--status")) {
                status = args[i + 1];
                if (!status.equals("pending") && !status.equals("completed")) {
                    System.out.println("Error: Status must be 'pending' or 'completed'");
                    return;
                }
                break;
            }
        }
        
        List<Task> tasks = manager.listTasks(status);
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("\nTasks:");
            System.out.println("-".repeat(50));
            for (Task task : tasks) {
                System.out.println(task);
                if (!task.getDescription().isEmpty()) {
                    System.out.println("    Description: " + task.getDescription());
                }
            }
            System.out.println("-".repeat(50));
        }
    }
    
    /**
     * Handle complete command.
     */
    private static void handleComplete(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Task ID is required");
            System.out.println("Usage: java Main complete <task_id>");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(args[1]);
            if (manager.completeTask(taskId)) {
                System.out.println("Task " + taskId + " marked as completed!");
            } else {
                System.out.println("Task " + taskId + " not found or already completed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Task ID must be a number");
        }
    }
    
    /**
     * Handle delete command.
     */
    private static void handleDelete(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Task ID is required");
            System.out.println("Usage: java Main delete <task_id>");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(args[1]);
            if (manager.deleteTask(taskId)) {
                System.out.println("Task " + taskId + " deleted.");
            } else {
                System.out.println("Task " + taskId + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Task ID must be a number");
        }
    }
    
    /**
     * Handle stats command.
     */
    private static void handleStats() {
        Map<String, Integer> stats = manager.getStatistics();
        
        System.out.println("\nTask Statistics:");
        System.out.println("Total tasks: " + stats.get("total"));
        System.out.println("Completed: " + stats.get("completed"));
        System.out.println("Pending: " + stats.get("pending"));
        
        if (stats.get("total") > 0) {
            double completionRate = (stats.get("completed") * 100.0) / stats.get("total");
            System.out.printf("Completion rate: %.1f%%\n", completionRate);
        }
    }
    
    /**
     * Print help message.
     */
    private static void printHelp() {
        System.out.println("Simple Task Manager - A Java CLI application to manage your tasks");
        System.out.println("\nUsage: java Main <command> [options]");
        System.out.println("\nAvailable commands:");
        System.out.println("  add <title> [-d <description>]  Add a new task");
        System.out.println("  list [-s <status>]              List tasks (optionally filter by status)");
        System.out.println("  complete <task_id>              Mark a task as completed");
        System.out.println("  delete <task_id>                Delete a task");
        System.out.println("  stats                           Show task statistics");
    }
}