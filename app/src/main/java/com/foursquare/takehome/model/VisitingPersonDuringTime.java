package com.foursquare.takehome.model;



public class VisitingPersonDuringTime  {

    private String name;
    private String interval;
    private long arrivalTime;
    private long leavingTime;

    public VisitingPersonDuringTime(  String name, long arrivalTime,long leavingTime, String interval) {
        this.arrivalTime = arrivalTime;
        this.leavingTime = leavingTime;
        this.name = name;
        this.interval = interval;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public long getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(long leavingTime) {
        this.leavingTime = leavingTime;
    }
}
