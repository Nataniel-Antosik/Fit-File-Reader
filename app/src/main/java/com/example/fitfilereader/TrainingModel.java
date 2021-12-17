package com.example.fitfilereader;

public class TrainingModel {
    String trainingData;
    String trainingPace;
    String trainingDistance;
    int id;


    public TrainingModel(String trainingData, String trainingPace, String trainingDistance, int id) {
        this.trainingData = trainingData;
        this.trainingPace = trainingPace;
        this.trainingDistance = trainingDistance;
        this.id = id;
    }

    public String getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(String trainingData) {
        this.trainingData = trainingData;
    }

    public String getTrainingPace() {
        return trainingPace;
    }

    public void setTrainingPace(String trainingPace) {
        this.trainingPace = trainingPace;
    }

    public String getTrainingDistance() {
        return trainingDistance;
    }

    public void setTrainingDistance(String trainingDistance) {
        this.trainingDistance = trainingDistance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
