import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages a collection of tasks with persistence to JSON file.
 */
public class TaskManager {
    private String filename;
    private List<Task> tasks;
    private int nextId;
    private Gson gson;
    
    /**
     * Constructor initializes the task manager with a filename.
     */
    public TaskManager(String filename) {
        this.filename = filename;
        this.tasks = new ArrayList<>();
        this.nextId = 1;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadTasks();
    }
    
    /**
     * Default constructor uses "tasks.json" as filename.
     */
    public TaskManager() {
        this("tasks.json");
    }
    
    /**
     * Load tasks from JSON file if it exists.
     */
    private void loadTasks() {
        File file = new File(filename);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> data = gson.fromJson(reader, type);
                
                if (data != null && data.containsKey("tasks")) {
                    List<Map<String, Object>> tasksList = (List<Map<String, Object>>) data.get("tasks");
                    tasks.clear();
                    
                    for (Map<String, Object> taskData : tasksList) {
                        Task task = new Task(
                            (String) taskData.get("title"),
                            (String) taskData.get("description"),
                            ((Double) taskData.get("id")).intValue()
                        );
                        task.setStatus((String) taskData.get("status"));
                        task.setCreatedAt((String) taskData.get("createdAt"));
                        task.setCompletedAt((String) taskData.get("completedAt"));
                        tasks.add(task);
                    }
                    
                    if (data.containsKey("nextId")) {
                        nextId = ((Double) data.get("nextId")).intValue();
                    }
                }
            } catch (IOException | ClassCastException e) {
                System.err.println("Error loading tasks: " + e.getMessage());
                tasks = new ArrayList<>();
                nextId = 1;
            }
        }
    }
    
    /**
     * Save tasks to JSON file.
     */
    private void saveTasks() {
        Map<String, Object> data = new HashMap<>();
        data.put("tasks", tasks);
        data.put("nextId", nextId);
        
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    /**
     * Add a new task to the manager.
     */
    public Task addTask(String title, String description) {
        Task task = new Task(title, description, nextId);
        tasks.add(task);
        nextId++;
        saveTasks();
        return task;
    }
    
    /**
     * List all tasks or filter by status.
     */
    public List<Task> listTasks(String status) {
        if (status != null && !status.isEmpty()) {
            return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
        }
        return new ArrayList<>(tasks);
    }
    
    /**
     * Get a specific task by ID.
     */
    public Task getTask(int taskId) {
        return tasks.stream()
            .filter(task -> task.getId() == taskId)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Mark a task as completed.
     */
    public boolean completeTask(int taskId) {
        Task task = getTask(taskId);
        if (task != null && !task.getStatus().equals("completed")) {
            task.complete();
            saveTasks();
            return true;
        }
        return false;
    }
    
    /**
     * Delete a task by ID.
     */
    public boolean deleteTask(int taskId) {
        Task task = getTask(taskId);
        if (task != null) {
            tasks.remove(task);
            saveTasks();
            return true;
        }
        return false;
    }
    
    /**
     * Get statistics about tasks.
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        int total = tasks.size();
        int completed = (int) tasks.stream()
            .filter(task -> task.getStatus().equals("completed"))
            .count();
        int pending = total - completed;
        
        stats.put("total", total);
        stats.put("completed", completed);
        stats.put("pending", pending);
        
        return stats;
    }
}