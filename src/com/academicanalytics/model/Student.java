package com.academicanalytics.model;

import java.util.List;
import java.util.ArrayList;

public class Student {
    private String studentId;
    private String name;
    private String course;
    private String semester;
    private double gpa;
    private int attendancePercent;
    private List<AssignmentScore> assignmentScores;

    public Student(String studentId, String name, String course, String semester,
                   double gpa, int attendancePercent) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.gpa = gpa;
        this.attendancePercent = attendancePercent;
        this.assignmentScores = new ArrayList<>();
    }

    public void addAssignmentScore(AssignmentScore score) {
        assignmentScores.add(score);
    }

    public double getAverageAssignmentScore() {
        if (assignmentScores.isEmpty()) return 0.0;
        return assignmentScores.stream()
                .mapToDouble(AssignmentScore::getScore)
                .average()
                .orElse(0.0);
    }

    public String getLetterGrade() {
        double avg = getAverageAssignmentScore();
        if (avg >= 90) return "A";
        if (avg >= 80) return "B";
        if (avg >= 70) return "C";
        if (avg >= 60) return "D";
        return "F";
    }

    public String getStudentId()       { return studentId; }
    public String getName()            { return name; }
    public String getCourse()          { return course; }
    public String getSemester()        { return semester; }
    public double getGpa()             { return gpa; }
    public int getAttendancePercent()  { return attendancePercent; }
    public List<AssignmentScore> getAssignmentScores() { return assignmentScores; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | GPA: %.2f | Att: %d%% | Grade: %s",
                studentId, name, course, gpa, attendancePercent, getLetterGrade());
    }
}