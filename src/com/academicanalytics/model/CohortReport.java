package com.academicanalytics.model;

import java.util.Map;

public class CohortReport {
    private String course;
    private String semester;
    private int totalStudents;
    private double averageGpa;
    private double averageAttendance;
    private double passRate;
    private Map<String, Long> gradeDistribution;
    private Map<AssignmentScore.Type, Double> avgScoreByType;

    public CohortReport(String course, String semester, int totalStudents,
                        double averageGpa, double averageAttendance, double passRate,
                        Map<String, Long> gradeDistribution,
                        Map<AssignmentScore.Type, Double> avgScoreByType) {
        this.course = course;
        this.semester = semester;
        this.totalStudents = totalStudents;
        this.averageGpa = averageGpa;
        this.averageAttendance = averageAttendance;
        this.passRate = passRate;
        this.gradeDistribution = gradeDistribution;
        this.avgScoreByType = avgScoreByType;
    }

    public String getCourse()                                    { return course; }
    public String getSemester()                                  { return semester; }
    public int getTotalStudents()                                { return totalStudents; }
    public double getAverageGpa()                               { return averageGpa; }
    public double getAverageAttendance()                        { return averageAttendance; }
    public double getPassRate()                                  { return passRate; }
    public Map<String, Long> getGradeDistribution()             { return gradeDistribution; }
    public Map<AssignmentScore.Type, Double> getAvgScoreByType() { return avgScoreByType; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Cohort Report: %s - %s ===\n", course, semester));
        sb.append(String.format("  Students   : %d\n", totalStudents));
        sb.append(String.format("  Avg GPA    : %.2f\n", averageGpa));
        sb.append(String.format("  Avg Attend : %.1f%%\n", averageAttendance));
        sb.append(String.format("  Pass Rate  : %.1f%%\n", passRate));
        sb.append("  Grade Dist : ");
        gradeDistribution.forEach((g, c) -> sb.append(g).append(":").append(c).append(" "));
        return sb.toString();
    }
}