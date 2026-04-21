package com.academicanalytics;

import com.academicanalytics.analytics.AnalyticsEngine;
import com.academicanalytics.model.AssignmentScore;
import com.academicanalytics.model.CohortReport;
import com.academicanalytics.model.Student;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Academic Performance Analytics Platform");
        System.out.println("===========================================\n");

        List<Student> students = buildSampleDataset();
        System.out.println("Dataset loaded: " + students.size() + " students across 4 courses\n");

        AnalyticsEngine engine = new AnalyticsEngine();

        CohortReport report = engine.generateCohortReport(students, "CS 101", "Spring 2026");
        System.out.println(report);
        System.out.println();

        List<String> semesterOrder = List.of(
                "Fall 2024", "Spring 2025", "Fall 2025", "Spring 2026");
        Map<String, Double> trend = engine.gpaGrends(students, "CS 101", semesterOrder);
        System.out.println("GPA Trend - CS 101:");
        trend.forEach((sem, gpa) ->
                System.out.printf("  %-15s  %.2f%n", sem, gpa));
        System.out.println();

        System.out.println("Course GPA Comparison - Spring 2026:");
        Map<String, Double> comparison = engine.compareCoursesGpa(students, "Spring 2026");
        comparison.forEach((course, gpa) ->
                System.out.printf("  %-20s  %.2f%n", course, gpa));
        System.out.println();

        double correlation = engine.attendanceGpaCorrelation(students);
        System.out.printf("Attendance-GPA Pearson Correlation: %.3f%n%n", correlation);

        List<Student> atRisk = engine.identifyAtRiskStudents(students, 2.8, 75);
        System.out.println("At-Risk Students (GPA < 2.8 or Attendance < 75%):");
        atRisk.forEach(s -> System.out.println("  " + s));
        System.out.println();

        System.out.println("Pipeline complete.");
    }

    private static List<Student> buildSampleDataset() {
        List<Student> students = new ArrayList<>();

        String[][] data = {
                {"S-1042", "Alex Kim",        "CS 101",          "Spring 2026", "3.8", "95"},
                {"S-1087", "Jamie Lee",       "CS 201",          "Spring 2026", "3.5", "90"},
                {"S-1103", "Sam Rivera",      "Discrete Math",   "Spring 2026", "2.9", "78"},
                {"S-1156", "Morgan Chen",     "Database Systems", "Spring 2026", "3.2", "88"},
                {"S-1198", "Taylor Patel",    "CS 101",          "Spring 2026", "2.6", "70"},
                {"S-1213", "Jordan Nguyen",   "CS 201",          "Spring 2026", "3.9", "98"},
                {"S-1244", "Casey Williams",  "Discrete Math",   "Spring 2026", "3.1", "82"},
                {"S-1267", "Drew Martinez",   "Database Systems", "Spring 2026", "3.6", "92"},
                {"S-1289", "Avery Thompson",  "CS 101",          "Spring 2026", "3.3", "85"},
                {"S-1301", "Quinn Johnson",   "CS 201",          "Spring 2026", "2.7", "72"},
                {"S-1042", "Alex Kim",        "CS 101",          "Fall 2025",   "3.5", "92"},
                {"S-1042", "Alex Kim",        "CS 101",          "Spring 2025", "3.2", "89"},
                {"S-1042", "Alex Kim",        "CS 101",          "Fall 2024",   "2.9", "84"},
                {"S-1198", "Taylor Patel",    "CS 101",          "Fall 2025",   "2.3", "65"},
        };

        for (String[] row : data) {
            Student s = new Student(row[0], row[1], row[2], row[3],
                    Double.parseDouble(row[4]), Integer.parseInt(row[5]));
            attachSampleAssignments(s);
            students.add(s);
        }

        return students;
    }

    private static void attachSampleAssignments(Student s) {
        double base = s.getGpa() * 22.5;
        Random rng = new Random(s.getStudentId().hashCode());

        Object[][] assignments = {
                {AssignmentScore.Type.HOMEWORK, "HW 1-5",    base + rng.nextInt(10),        100.0},
                {AssignmentScore.Type.QUIZ,     "Quiz 1-4",  base - 5 + rng.nextInt(8),     100.0},
                {AssignmentScore.Type.MIDTERM,  "Midterm",   base - 8 + rng.nextInt(12),    100.0},
                {AssignmentScore.Type.PROJECT,  "Project",   base + 5 + rng.nextInt(8),     100.0},
                {AssignmentScore.Type.LAB,      "Labs",      base + rng.nextInt(10),        100.0},
                {AssignmentScore.Type.FINAL,    "Final Exam", base - 5 + rng.nextInt(10),   100.0},
        };

        for (Object[] a : assignments) {
            double score = Math.min(100.0, Math.max(40.0, (double) a[2]));
            s.addAssignmentScore(new AssignmentScore(
                    (AssignmentScore.Type) a[0], (String) a[1], score, (double) a[3]));
        }
    }
}