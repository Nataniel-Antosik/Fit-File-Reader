package com.example.fitfilereader;


public class TrainingProgressModel {

    Double swimBestPaceButterfly;
    Double swimBestPaceBackstroke;
    Double swimBestPaceBreaststroke;
    Double swimBestPaceFreestyle;
    String swimDateProgress;

    public TrainingProgressModel(Double swimBestPaceButterfly, Double swimBestPaceBackstroke, Double swimBestPaceBreaststroke, Double swimBestPaceFreestyle, String swimDateProgress) {
        this.swimBestPaceButterfly = swimBestPaceButterfly;
        this.swimBestPaceBackstroke = swimBestPaceBackstroke;
        this.swimBestPaceBreaststroke = swimBestPaceBreaststroke;
        this.swimBestPaceFreestyle = swimBestPaceFreestyle;
        this.swimDateProgress = swimDateProgress;
    }

    public Double getSwimBestPaceButterfly() {
        return swimBestPaceButterfly;
    }

    public void setSwimBestPaceButterfly(Double swimBestPaceButterfly) {
        this.swimBestPaceButterfly = swimBestPaceButterfly;
    }

    public Double getSwimBestPaceBackstroke() {
        return swimBestPaceBackstroke;
    }

    public void setSwimBestPaceBackstroke(Double swimBestPaceBackstroke) {
        this.swimBestPaceBackstroke = swimBestPaceBackstroke;
    }

    public Double getSwimBestPaceBreaststroke() {
        return swimBestPaceBreaststroke;
    }

    public void setSwimBestPaceBreaststroke(Double swimBestPaceBreaststroke) {
        this.swimBestPaceBreaststroke = swimBestPaceBreaststroke;
    }

    public Double getSwimBestPaceFreestyle() {
        return swimBestPaceFreestyle;
    }

    public void setSwimBestPaceFreestyle(Double swimBestPaceFreestyle) {
        this.swimBestPaceFreestyle = swimBestPaceFreestyle;
    }

    public String getSwimDateProgress() {
        return swimDateProgress;
    }

    public void setSwimDateProgress(String swimDateProgress) {
        this.swimDateProgress = swimDateProgress;
    }
}
