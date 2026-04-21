package com.academicanalytics.data;

import com.academicanalytics.model.AssignmentScore;
import com.academicanalytics.model.Student;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

public class DataLoader {

    private static final Logger logger = Logger.getLogger(DataLoader.class.getName());
    private final List<String> validationErrors = new ArrayList<>();

    public List<Student> loadStudentsFromCsv(String filePath) throws IOException {
        List<Student> students = new ArrayList<>();
        List<String> lines = Files.readAllLines(Path.of(filePath));

        if (lines.isEmpty()) {
            throw new IOException("CSV file is empty: " + filePath);
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            try {
                Student student = parseStudentRow(line, i + 1);
                students.add(student);
            } catch (IllegalArgumentException e) {
                validationErrors.add("Row " + (i + 1) + ": " + e.getMessage());
                logger.warning("Skipping malformed row " + (i + 1) + ": " + e.getMessage());
            }
        }

        logger.info("Loaded " + students.size() + " students from " + filePath);
        return students;
    }

    public void loadAssignmentsFromCsv(String filePath, Map<String, Student> studentIndex)
            throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = line.split(",");
                if (parts.length < 5) throw new IllegalArgumentException("Insufficient columns");

                String studentId = parts[0].trim();
                AssignmentScore.Type type = AssignmentScore.Type.valueOf(parts[1].trim().toUpperCase());
                String label = parts[2].trim();
                double score = Double.parseDouble(parts[3].trim());
                double maxScore = Double.parseDouble(parts[4].trim());

                if (!studentIndex.containsKey(studentId)) {
                    validationErrors.add("Row " + (i + 1) + ": Unknown student ID: " + studentId);
                    continue;
                }

                studentIndex.get(studentId).addAssignmentScore(
                        new AssignmentScore(type, label, score, maxScore));

            } catch (Exception e) {
                validationErrors.add("Assignment row " + (i + 1) + ": " + e.getMessage());
            }
        }
    }

    private Student parseStudentRow(String line, int rowNum) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Expected 6 columns, found " + parts.length);
        }

        String studentId = parts[0].trim();
        String name      = parts[1].trim();
        String course    = parts[2].trim();
        String semester  = parts[3].trim();

        double gpa;
        int attendance;
        try {
            gpa = Double.parseDouble(parts[4].trim());
            attendance = Integer.parseInt(parts[5].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value: " + e.getMessage());
        }

        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA out of range [0.0, 4.0]: " + gpa);
        }
        if (attendance < 0 || attendance > 100) {
            throw new IllegalArgumentException("Attendance out of range [0, 100]: " + attendance);
        }

        return new Student(studentId, name, course, semester, gpa, attendance);
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }

    public static void writeSampleCsv(String filePath) throws IOException {
        String header = "studentId,name,course,semester,gpa,attendancePercent\n";
        String[][] rows = {
                {"S-1042","Alex Kim","CS 101","Spring 2026","3.8","95"},
                {"S-1087","Jamie Lee","CS 201","Spring 2026","3.5","90"},
                {"S-1103","Sam Rivera","Discrete Math","Spring 2026","2.9","78"},
                {"S-1156","Morgan Chen","Database Systems","Spring 2026","3.2","88"},
                {"S-1198","Taylor Patel","CS 101","Spring 2026","2.6","70"},
                {"S-1213","Jordan Nguyen","CS 201","Spring 2026","3.9","98"},
                {"S-1244","Casey Williams","Discrete Math","Spring 2026","3.1","82"},
                {"S-1267","Drew Martinez","Database Systems","Spring 2026","3.6","92"},
                {"S-1289","Avery Thompson","CS 101","Spring 2026","3.3","85"},
                {"S-1301","Quinn Johnson","CS 201","Spring 2026","2.7","72"},
        };

        StringBuilder sb = new StringBuilder(header);
        for (String[] row : rows) {
            sb.append(String.join(",", row)).append("\n");
        }

        Files.writeString(Path.of(filePath), sb.toString());
    }
}