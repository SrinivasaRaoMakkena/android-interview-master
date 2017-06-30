package com.foursquare.takehome;

import com.foursquare.takehome.model.VisitingPersonDuringTime;

import java.util.Comparator;
import java.util.Date;


public class ComparatorToSort implements Comparator<VisitingPersonDuringTime> {
        @Override
        public int compare(VisitingPersonDuringTime o1, VisitingPersonDuringTime o2) {
            Date time1=new Date(o1.getArrivalTime()*1000);
            Date time2=new Date(o2.getArrivalTime()*1000);
            return time1.compareTo(time2);
        }
}


