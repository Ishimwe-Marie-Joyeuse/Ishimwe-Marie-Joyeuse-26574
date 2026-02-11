

package question5_task_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import question5_task_api.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // In-memory list to store tasks (simulating a database)
    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 7L;

    // Initialize with sample tasks
    public TaskController() {
        tasks.add(new Task(1L, "Complete Spring Boot Assignment", 
                "Finish all 5 questions for the REST API assignment", false, "HIGH", "2025-02-15"));
        tasks.add(new Task(2L, "Study for Midterm Exam", 
                "Review chapters 1-5 for upcoming exam", false, "HIGH", "2025-02-20"));
        tasks.add(new Task(3L, "Buy Groceries", 
                "Get milk, eggs, bread, and vegetables", false, "MEDIUM", "2025-02-12"));
        tasks.add(new Task(4L, "Gym Workout", 
                "Cardio and strength training session", true, "LOW", "2025-02-11"));
        tasks.add(new Task(5L, "Team Meeting", 
                "Discuss project milestones and deliverables", false, "MEDIUM", "2025-02-13"));
        tasks.add(new Task(6L, "Read Book Chapter", 
                "Read chapter 3 of Clean Code", true, "LOW", "2025-02-10"));
    }

    /**
     * GET /api/tasks - Get all tasks
     * @return List of all tasks with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/{taskId} - Get task by ID
     * @param taskId - Task ID
     * @return Task with HTTP 200 OK or HTTP 404 Not Found
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();
        
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/tasks/status?completed={true/false} - Get tasks by completion status
     * @param completed - Completion status (true/false)
     * @return List of tasks with specified completion status with HTTP 200 OK
     */
    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> filteredTasks = tasks.stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(filteredTasks);
    }

    /**
     * GET /api/tasks/priority/{priority} - Get tasks by priority
     * @param priority - Priority level (LOW, MEDIUM, HIGH)
     * @return List of tasks with specified priority with HTTP 200 OK
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> priorityTasks = tasks.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(priorityTasks);
    }

    /**
     * POST /api/tasks - Create new task
     * @param task - Task object from request body
     * @return Created task with HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setTaskId(nextId++);
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    /**
     * PUT /api/tasks/{taskId} - Update task
     * @param taskId - Task ID to update
     * @param updatedTask - Updated task information
     * @return Updated task with HTTP 200 OK or HTTP 404 Not Found
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long taskId,
            @RequestBody Task updatedTask) {
        
        Optional<Task> existingTask = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();
        
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            
            // Update task fields
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            task.setPriority(updatedTask.getPriority());
            task.setDueDate(updatedTask.getDueDate());
            
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * PATCH /api/tasks/{taskId}/complete - Mark task as completed
     * @param taskId - Task ID
     * @return Updated task with HTTP 200 OK or HTTP 404 Not Found
     */
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long taskId) {
        Optional<Task> existingTask = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();
        
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setCompleted(true);
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * DELETE /api/tasks/{taskId} - Delete task
     * @param taskId - Task ID to delete
     * @return HTTP 204 No Content if deleted, HTTP 404 Not Found if not exists
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean removed = tasks.removeIf(t -> t.getTaskId().equals(taskId));
        
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}