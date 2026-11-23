package com.academicanalytics.analytics;

import com.academicanalytics.model.AssignmentScore;
import com.academicanalytics.model.CohortReport;
import com.academicanalytics.model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsEngine {

    public CohortReport generateCohortReport(List<Student> students, String course, String semester) {
        List<Student> cohort = filterByCourseAndSemester(students, course, semester);

        if (cohort.isEmpty()) {
            throw new IllegalArgumentException("No students found for: " + course + " / " + semester);
        }

        double avgGpa = cohort.stream()
                .mapToDouble(Student::getGpa)
                .average().orElse(0.0);

        double avgAttendance = cohort.stream()
                .mapToInt(Student::getAttendancePercent)
                .average().orElse(0.0);

        long passed = cohort.stream()
                .filter(s -> !s.getLetterGrade().equals("F") && !s.getLetterGrade().equals("D"))
                .count();
        double passRate = ((double) passed / cohort.size()) * 100.0;

        Map<String, Long> gradeDist = cohort.stream()
                .collect(Collectors.groupingBy(Student::getLetterGrade, Collectors.counting()));

        Map<AssignmentScore.Type, Double> avgByType = computeAvgScoreByType(cohort);

        return new CohortReport(course, semester, cohort.size(),
                avgGpa, avgAttendance, passRate, gradeDist, avgByType);
    }

    public Map<String, Double> gpaTrends(List<Student> students, String course,
                                         List<String> semesterOrder) {
        Map<String, Double> trend = new LinkedHashMap<>();
        for (String sem : semesterOrder) {
            List<Student> cohort = filterByCourseAndSemester(students, course, sem);
            if (!cohort.isEmpty()) {
                double avg = cohort.stream().mapToDouble(Student::getGpa).average().orElse(0.0);
                trend.put(sem, Math.round(avg * 100.0) / 100.0);
            }
        }
        return trend;
    }

    public Map<String, Double> attendanceTrends(List<Student> students, String course,
                                                List<String> semesterOrder) {
        Map<String, Double> trend = new LinkedHashMap<>();
        for (String sem : semesterOrder) {
            List<Student> cohort = filterByCourseAndSemester(students, course, sem);
            if (!cohort.isEmpty()) {
                double avg = cohort.stream().mapToInt(Student::getAttendancePercent)
                        .average().orElse(0.0);
                trend.put(sem, Math.round(avg * 10.0) / 10.0);
            }
        }
        return trend;
    }

    public Map<String, Double> compareCoursesGpa(List<Student> students, String semester) {
        return students.stream()
                .filter(s -> s.getSemester().equals(semester))
                .collect(Collectors.groupingBy(Student::getCourse,
                        Collectors.averagingDouble(Student::getGpa)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public List<Student> identifyAtRiskStudents(List<Student> students,
                                                double gpaThreshold,
                                                int attendanceThreshold) {
        return students.stream()
                .filter(s -> s.getGpa() < gpaThreshold || s.getAttendancePercent() < attendanceThreshold)
                .sorted(Comparator.comparingDouble(Student::getGpa))
                .collect(Collectors.toList());
    }

    public double attendanceGpaCorrelation(List<Student> students) {
        int n = students.size();
        if (n < 2) return 0.0;

        double[] x = students.stream().mapToDouble(Student::getAttendancePercent).toArray();
        double[] y = students.stream().mapToDouble(Student::getGpa).toArray();

        double meanX = Arrays.stream(x).average().orElse(0);
        double meanY = Arrays.stream(y).average().orElse(0);

        double numerator = 0, denomX = 0, denomY = 0;
        for (int i = 0; i < n; i++) {
            double dx = x[i] - meanX, dy = y[i] - meanY;
            numerator += dx * dy;
            denomX += dx * dx;
            denomY += dy * dy;
        }

        double denom = Math.sqrt(denomX * denomY);
        return denom == 0 ? 0 : numerator / denom;
    }

    private Map<AssignmentScore.Type, Double> computeAvgScoreByType(List<Student> cohort) {
        Map<AssignmentScore.Type, List<Double>> grouped = new HashMap<>();
        for (Student s : cohort) {
            for (AssignmentScore score : s.getAssignmentScores()) {
                grouped.computeIfAbsent(score.getType(), k -> new ArrayList<>())
                        .add(score.getPercentage());
            }
        }
        Map<AssignmentScore.Type, Double> result = new LinkedHashMap<>();
        grouped.forEach((type, scores) -> {
            double avg = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            result.put(type, Math.round(avg * 10.0) / 10.0);
        });
        return result;
    }

    public List<Student> filterByCourseAndSemester(List<Student> students,
                                                   String course, String semester) {
        return students.stream()
                .filter(s -> s.getCourse().equals(course) && s.getSemester().equals(semester))
                .collect(Collectors.toList());
    }

    public List<Student> filterByCourse(List<Student> students, String course) {
        return students.stream()
                .filter(s -> s.getCourse().equals(course))
                .collect(Collectors.toList());
    }
}