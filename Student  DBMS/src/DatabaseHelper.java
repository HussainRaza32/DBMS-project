import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;


public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/CarsDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Use your actual MySQL password

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    static Admin getAdmin() {
        String sql = "SELECT * FROM Admin LIMIT 1";
        Admin admin = null;
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                admin = new Admin(email, username, password);
            }
    
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        return admin;
    }

    static void displayStudentRecords() {
        String sql = "{CALL GetStudentRecords()}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            int currentStudentId = -1;

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String studentFirstName = rs.getString("student_first_name");
                String studentLastName = rs.getString("student_last_name");
                String studentEmail = rs.getString("student_email");
                String subjectName = rs.getString("subject_name");
                String teacherFirstName = rs.getString("teacher_first_name");
                String teacherLastName = rs.getString("teacher_last_name");

                if (studentId != currentStudentId) {
                    if (currentStudentId != -1) {
                        System.out.println();
                    }
                    currentStudentId = studentId;
                    System.out.printf("Student ID: %d%n", studentId);
                    System.out.printf("Name: %s %s%n", studentFirstName, studentLastName);
                    System.out.printf("Email: %s%n", studentEmail);
                }
                System.out.printf("  Subject: %s%n", subjectName);
                System.out.printf("  Taught By: %s %s%n", teacherFirstName, teacherLastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void displayStudentById(int studentId) {
        String sql = "{CALL GetStudentById(?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, studentId);

            try (ResultSet rs = cstmt.executeQuery()) {
                boolean studentFound = false;
                while (rs.next()) {
                    if (!studentFound) {
                        studentFound = true;
                        System.out.println("Student ID: " + rs.getInt("student_id"));
                        System.out.println("Name: " + rs.getString("student_first_name") + " " + rs.getString("student_last_name"));
                        System.out.println("Email: " + rs.getString("student_email"));
                        System.out.println("Subjects:");
                    }
                    System.out.println("  - Subject: " + rs.getString("subject_name"));
                    System.out.println("    Taught By: " + rs.getString("teacher_first_name") + " " + rs.getString("teacher_last_name"));
                }
                if (!studentFound) {
                    System.out.println("Student with ID " + studentId + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    static void displayTeacherRecords() {
        String sql = "{CALL GetTeacherRecords()}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            int currentTeacherId = -1;

            while (rs.next()) {
                int teacherId = rs.getInt("teacher_id");
                String teacherFirstName = rs.getString("teacher_first_name");
                String teacherLastName = rs.getString("teacher_last_name");
                String teacherEmail = rs.getString("teacher_email");
                String subjectName = rs.getString("subject_name");

                if (teacherId != currentTeacherId) {
                    if (currentTeacherId != -1) {
                        System.out.println();
                    }
                    currentTeacherId = teacherId;
                    System.out.printf("Teacher ID: %d%n", teacherId);
                    System.out.printf("Name: %s %s%n", teacherFirstName, teacherLastName);
                    System.out.printf("Email: %s%n", teacherEmail);
                }
                System.out.printf("  Subject: %s%n", subjectName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void displaySubjectsAndTeachers() {
        String sql = "{CALL GetSubjectsAndTeachers()}";
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {
    
            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                int teacherId = rs.getInt("teacher_id");
                String teacherName = rs.getString("teacher_name");
                String teacherEmail = rs.getString("teacher_email");
    
                System.out.printf("Subject ID: %d, Name: %s%n", subjectId, subjectName);
                System.out.printf("  Teacher ID: %d, Name: %s, Email: %s%n", teacherId, teacherName, teacherEmail);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void addStudent(String firstName, String lastName, String email, String subjectsList) {
        String sql = "{CALL AddStudent(?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, firstName);
            cstmt.setString(2, lastName);
            cstmt.setString(3, email);
            cstmt.setString(4, subjectsList);

            cstmt.executeUpdate();

            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void addSubject(String subjectName, Integer teacherId) {
        String sql = "{CALL AddSubject(?, ?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, subjectName);
            if (teacherId == null) {
                cstmt.setNull(2, Types.INTEGER); // Pass NULL if no teacher assigned
            } else {
                cstmt.setInt(2, teacherId);
            }

            cstmt.executeUpdate();

            System.out.println("Subject added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    static void addTeacher(String firstName, String lastName, String email, String subjectsList) {
        String sql = "{CALL AddTeacher(?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, firstName);
            cstmt.setString(2, lastName);
            cstmt.setString(3, email);
            cstmt.setString(4, subjectsList);

            cstmt.executeUpdate();

            System.out.println("Teacher added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void updateStudent(int studentId, String newFirstName, String newLastName, String newEmail) {
        String sql = "{CALL UpdateStudent(?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, studentId);
            cstmt.setString(2, newFirstName);
            cstmt.setString(3, newLastName);
            cstmt.setString(4, newEmail);

            cstmt.executeUpdate();

            System.out.println("Student updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void updateTeacher(int teacherId, String newFirstName, String newLastName, String newEmail) {
        String sql = "{CALL UpdateTeacher(?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, teacherId);
            cstmt.setString(2, newFirstName);
            cstmt.setString(3, newLastName);
            cstmt.setString(4, newEmail);

            cstmt.executeUpdate();

            System.out.println("Teacher updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void updateSubject(int subjectId, String newSubjectName, int newTeacherId) {
        String sql = "{CALL UpdateSubject(?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, subjectId);
            cstmt.setString(2, newSubjectName);
            cstmt.setInt(3, newTeacherId);

            cstmt.executeUpdate();

            System.out.println("Subject updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteTeacher(int teacherId) {
        String sql = "{CALL DeleteTeacher(?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, teacherId);

            cstmt.executeUpdate();

            System.out.println("Teacher deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteSubject(int subjectId) {
        String sql = "{CALL DeleteSubject(?)}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setInt(1, subjectId);

            cstmt.executeUpdate();

            System.out.println("Subject deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }   

    }

    static void deleteStudent(int studentId) {
    String sql = "{CALL DeleteStudent(?)}";

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         CallableStatement cstmt = conn.prepareCall(sql)) {

        cstmt.setInt(1, studentId);

        cstmt.executeUpdate();

        System.out.println("Student deleted successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    static void getEnrolledStudents() {
        String sql = "{CALL GetEnrolledStudents()}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String firstName = rs.getString("student_first_name");
                String lastName = rs.getString("student_last_name");
                String email = rs.getString("student_email");
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");

                System.out.println("Student ID: " + studentId);
                System.out.println("Name: " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Subject ID: " + subjectId);
                System.out.println("Subject Name: " + subjectName);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    static void getAllTeachers() {
        String sql = "{CALL GetAllTeachers()}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                int teacherId = rs.getInt("teacher_id");
                String firstName = rs.getString("teacher_first_name");
                String lastName = rs.getString("teacher_last_name");
                String email = rs.getString("teacher_email");
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");

                System.out.println("Teacher ID: " + teacherId);
                System.out.println("Name: " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Subject ID: " + subjectId);
                System.out.println("Subject Name: " + subjectName);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void getAllSubjects() {
        String sql = "{CALL GetAllSubjects()}";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                int teacherId = rs.getInt("teacher_id");
                String teacherFirstName = rs.getString("teacher_first_name");
                String teacherLastName = rs.getString("teacher_last_name");
                String teacherEmail = rs.getString("teacher_email");

                System.out.println("Subject ID: " + subjectId);
                System.out.println("Subject Name: " + subjectName);
                System.out.println("Teacher ID: " + teacherId);
                System.out.println("Teacher Name: " + teacherFirstName + " " + teacherLastName);
                System.out.println("Teacher Email: " + teacherEmail);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class Admin {
    String email;
    String username;
    String password;

    public Admin(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
     }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


class Auth {
    public static void authenticateUser() {
        Admin ajk = DatabaseHelper.getAdmin();
        Scanner scanner = new Scanner(System.in);

        String username;
        String password;
        boolean authenticated = false;

        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();

            System.out.print("Enter password: ");
            password = scanner.nextLine();

            // Replace this check with your actual authentication logic
            if (ajk.username.equals(username) && ajk.password.equals(password)) {
                authenticated = true;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

        } while (!authenticated);

        System.out.println("Authentication successful!");
    }
}

