# AcademicAnalytics

A small Java program that analyses student performance: cohort reports, GPA trends, course comparisons, the link between attendance and GPA, and a list of students who may need support.

> **All data is made up.** The sample students, IDs and scores are generated in code. No real student records are used.

## What it does

| Feature | Method | What you get |
|---|---|---|
| Cohort report | `generateCohortReport` | Average GPA, average attendance, pass rate, grade distribution and average score by assignment type for one course and semester |
| GPA trend | `gpaTrends` | Average GPA for a course, semester by semester |
| Attendance trend | `attendanceTrends` | Average attendance for a course, semester by semester |
| Course comparison | `compareCoursesGpa` | Courses ranked by average GPA in one semester |
| Attendance vs GPA | `attendanceGpaCorrelation` | Pearson correlation between attendance and GPA |
| At-risk list | `identifyAtRiskStudents` | Students below a GPA or attendance threshold |
| CSV loading | `DataLoader` | Reads students and assignment scores from CSV, skips malformed rows and records why |

## How to run it

Requires Java 17 or newer. No build tool or libraries are needed.

```bash
javac -d out $(find src -name "*.java")
java -cp out com.academicanalytics.Main
```

On Windows PowerShell:

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.academicanalytics.Main
```

## Sample output

```
=== Cohort Report: CS 101 - Spring 2026 ===
  Students   : 3
  Avg GPA    : 3.23
  Avg Attend : 83.3%
  Pass Rate  : 66.7%
  Grade Dist : B:1 C:1 D:1

GPA Trend - CS 101:
  Fall 2024        2.90
  Spring 2025      3.20
  Fall 2025        2.90
  Spring 2026      3.23

Course GPA Comparison - Spring 2026:
  Database Systems      3.40
  CS 201                3.37
  CS 101                3.23
  Discrete Math         3.00

Attendance-GPA Pearson Correlation: 0.966

At-Risk Students (GPA < 2.8 or Attendance < 75%):
  [S-1198] Taylor Patel | CS 101 | GPA: 2.30 | Att: 65% | Assignments: F
  [S-1198] Taylor Patel | CS 101 | GPA: 2.60 | Att: 70% | Assignments: D
  [S-1301] Quinn Johnson | CS 201 | GPA: 2.70 | Att: 72% | Assignments: D
```

The correlation is high because the sample data is tiny (14 rows) and generated; it says nothing about real students.

## Project layout

```
src/com/academicanalytics/
├── Main.java                  # builds the sample data and prints the reports
├── analytics/
│   └── AnalyticsEngine.java   # all the calculations
├── data/
│   └── DataLoader.java        # CSV reading, validation and export
└── model/
    ├── Student.java           # one student in one course and semester
    ├── AssignmentScore.java   # one graded item (homework, quiz, midterm...)
    └── CohortReport.java      # the summary for a course and semester
```

## Known limitations

- `Main` uses the built-in sample data; the CSV loader is written but not wired into the demo.
- There are no automated tests yet.
- The letter shown per student is the average of their assignment scores, not their GPA, so the two can differ in the sample data.

## Author

Michael Hanson ([MichaelH117](https://github.com/MichaelH117))
