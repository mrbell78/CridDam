package com.criddam.medicine.rubelportion;

public class Helthdata {

    int tempareture;
    int weight;
    int pulse;
    int systolic;
    int diastolic;
    String date;

    public Helthdata() {
    }

    public Helthdata(int tempareture, int weight, int pulse, int systolic, int diastolic, String date) {
        this.tempareture = tempareture;
        this.weight = weight;
        this.pulse = pulse;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.date = date;
    }

    public int getTempareture() {
        return tempareture;
    }

    public void setTempareture(int tempareture) {
        this.tempareture = tempareture;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
