import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single task with attributes like title, description, status, and timestamps.
 */
public class Task {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private Integer id;
    private String title;
    private String description;
    private String status;
    private String createdAt;
    private String completedAt;
    
    /**
     * Constructor for creating a new task.
     */
    public Task(String title, String description, Integer taskId) {
        this.id = taskId;
        this.title = title;
        this.description = description != null ? description : "";
        this.status = "pending";
        this.createdAt = LocalDateTime.now().format(formatter);
        this.completedAt = null;
    }
    
    /**
     * Mark the task as completed with timestamp.
     */
    public void complete() {
        this.status = "completed";
        this.completedAt = LocalDateTime.now().format(formatter);
    }
    
    /**
     * String representation of the task.
     */
    @Override
    public String toString() {
        String statusSymbol = status.equals("completed") ? "✓" : "○";
        return String.format("[%s] %d: %s", statusSymbol, id, title);
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}