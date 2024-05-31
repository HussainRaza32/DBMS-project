import java.util.*;


public class Main {

    public static void main(String[] args) {
        Auth.authenticateUser();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Add a student");
            System.out.println("2. Add a teacher");
            System.out.println("3. Add a subject");
            System.out.println("4. Search for a student");
            System.out.println("5. Update student information");
            System.out.println("6. Update teacher information");
            System.out.println("7. Update subject information");
            System.out.println("8. Delete a student");
            System.out.println("9. Delete a teacher");
            System.out.println("10. Delete a subject");
            System.out.println("11. Show all enrolled students");
            System.out.println("12. Show all teachers");
            System.out.println("13. Show all subjects");
            System.out.println("14. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student's first name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Enter student's last name: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Enter student's email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter the number of subjects the student wants to add: ");
                    int numSubjects = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    StringBuilder subjectIdsBuilder = new StringBuilder();

                    // Loop to get details for each subject
                    for (int i = 1; i <= numSubjects; i++) {
                        System.out.print("Enter subject ID for subject " + i + ": ");
                        int subjectId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        if (i > 1) {
                            subjectIdsBuilder.append(",");
                        }
                        subjectIdsBuilder.append(subjectId);
                    }

                    String subjectIds = subjectIdsBuilder.toString();

                    DatabaseHelper.addStudent(firstName, lastName, email, subjectIds);
                    break;
                case 2:
                    System.out.print("Enter teacher's first name: ");
                    firstName = scanner.nextLine();

                    System.out.print("Enter teacher's last name: ");
                    lastName = scanner.nextLine();

                    System.out.print("Enter teacher's email: ");
                    email = scanner.nextLine();

                    System.out.print("Enter the number of subjects the teacher wants to teach: ");
                    numSubjects = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    subjectIdsBuilder = new StringBuilder();

                    // Loop to get details for each subject
                    for (int i = 1; i <= numSubjects; i++) {
                        System.out.print("Enter subject ID for subject " + i + ": ");
                        int subjectId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        if (i > 1) {
                            subjectIdsBuilder.append(",");
                        }
                        subjectIdsBuilder.append(subjectId);
                    }

                    subjectIds = subjectIdsBuilder.toString();
                    DatabaseHelper.addTeacher(firstName, lastName, email, subjectIds);
                    break;
                case 3:
                    System.out.print("Enter subject name: ");
                    String subjectName = scanner.nextLine();

                    System.out.print("Enter teacher ID: ");
                    int teacherId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    DatabaseHelper.addSubject(subjectName, teacherId);
                    break;
                case 4:
                    System.out.print("Enter student ID: ");
                    int studentID = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    DatabaseHelper.displayStudentById(studentID);
                    break;
                case 5:
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    System.out.print("Enter new student first name: ");
                    String newFirstName = scanner.nextLine();

                    System.out.print("Enter new student last name: ");
                    String newLastName = scanner.nextLine();

                    System.out.print("Enter new student email: ");
                    String newEmail = scanner.nextLine();

                    DatabaseHelper.updateStudent(studentId, newFirstName, newLastName, newEmail);
                    break;
                case 6:
                    System.out.print("Enter teacher ID: ");
                    int teacher3Id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    System.out.print("Enter new teacher first name: ");
                    newFirstName = scanner.nextLine();

                    System.out.print("Enter new teacher last name: ");
                    newLastName = scanner.nextLine();

                    System.out.print("Enter new teacher email: ");
                    newEmail = scanner.nextLine();

                    DatabaseHelper.updateTeacher(teacher3Id, newFirstName, newLastName, newEmail);
                    break;
                case 7:
                    System.out.print("Enter subject ID: ");
                    int subjectIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    System.out.print("Enter new subject name: ");
                    String newSubjectName = scanner.nextLine();

                    System.out.print("Enter new teacher ID: ");
                    int newTeacherId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    DatabaseHelper.updateSubject(subjectIdToUpdate, newSubjectName, newTeacherId);
                    break;
                case 8:
                    System.out.print("Enter student ID: ");
                    int studentIdToDelete = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    DatabaseHelper.deleteStudent(studentIdToDelete);
                    break;
                case 9:
                    System.out.print("Enter teacher ID: ");
                    int teacherIdToDelete = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    DatabaseHelper.deleteTeacher(teacherIdToDelete);
                    break;
                case 10:
                    System.out.print("Enter subject ID: ");
                    int subjectIdToDelete = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    DatabaseHelper.deleteSubject(subjectIdToDelete);
                    break;
                case 11:
                    DatabaseHelper.getEnrolledStudents();
                    break;
                case 12:
                    DatabaseHelper.getAllTeachers();
                    break;
                case 13:
                    DatabaseHelper.getAllSubjects();
                    break;
                case 14:
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 14.");
        }
        }
    }
}

