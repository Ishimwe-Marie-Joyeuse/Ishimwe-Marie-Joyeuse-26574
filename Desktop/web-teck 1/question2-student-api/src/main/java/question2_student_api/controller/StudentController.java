package question2_student_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import question2_student_api.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    // In-memory list to store students (simulating a database)
    private List<Student> students = new ArrayList<>();
    private Long nextId = 6L;

    // Initialize with 5 sample students with different majors and GPAs
    public StudentController() {
        students.add(new Student(1L, "John", "Doe", "john.doe@university.edu", "Computer Science", 3.8));
        students.add(new Student(2L, "Jane", "Smith", "jane.smith@university.edu", "Engineering", 3.9));
        students.add(new Student(3L, "Michael", "Johnson", "michael.j@university.edu", "Computer Science", 3.5));
        students.add(new Student(4L, "Emily", "Brown", "emily.brown@university.edu", "Business", 3.2));
        students.add(new Student(5L, "David", "Wilson", "david.wilson@university.edu", "Engineering", 3.7));
    }

    /**
     * GET /api/students - Get all students
     * @return List of all students with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(students);
    }

    /**
     * GET /api/students/{studentId} - Get student by ID
     * @param studentId - Student ID
     * @return Student object with HTTP 200 OK or HTTP 404 Not Found
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Optional<Student> student = students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
        
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/students/major/{major} - Get all students by major
     * @param major - Major to filter by
     * @return List of students in the specified major with HTTP 200 OK
     */
    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> studentsByMajor = students.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .toList();
        
        return ResponseEntity.ok(studentsByMajor);
    }

    /**
     * GET /api/students/filter?gpa={minGpa} - Filter students with GPA >= minimum
     * @param gpa - Minimum GPA threshold
     * @return List of students with GPA >= minGpa with HTTP 200 OK
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGpa(@RequestParam Double gpa) {
        List<Student> filteredStudents = students.stream()
                .filter(s -> s.getGpa() >= gpa)
                .toList();
        
        return ResponseEntity.ok(filteredStudents);
    }

    /**
     * POST /api/students - Register a new student
     * @param student - Student object from request body
     * @return Created student with HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        student.setStudentId(nextId++);
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    /**
     * PUT /api/students/{studentId} - Update student information
     * @param studentId - Student ID to update
     * @param updatedStudent - Updated student information from request body
     * @return Updated student with HTTP 200 OK or HTTP 404 Not Found
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, 
                                                  @RequestBody Student updatedStudent) {
        Optional<Student> existingStudent = students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
        
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            
            // Update student fields
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            student.setEmail(updatedStudent.getEmail());
            student.setMajor(updatedStudent.getMajor());
            student.setGpa(updatedStudent.getGpa());
            
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * DELETE /api/students/{studentId} - Delete a student
     * @param studentId - Student ID to delete
     * @return HTTP 204 No Content or HTTP 404 Not Found
     */
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId().equals(studentId));
        
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

