package com.academicanalytics.model;

public class AssignmentScore {
    public enum Type { HOMEWORK, QUIZ, MIDTERM, PROJECT, LAB, FINAL }

    private Type type;
    private double score;
    private double maxScore;
    private String label;

    public AssignmentScore(Type type, String label, double score, double maxScore) {
        this.type = type;
        this.label = label;
        this.score = score;
        this.maxScore = maxScore;
    }

    public double getScore()       { return score; }
    public double getMaxScore()    { return maxScore; }
    public double getPercentage()  { return (score / maxScore) * 100.0; }
    public Type getType()          { return type; }
    public String getLabel()       { return label; }
}