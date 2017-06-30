package com.foursquare.takehome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.foursquare.takehome.adapter.PersonAdapter;
import com.foursquare.takehome.model.Person;
import com.foursquare.takehome.model.Venue;
import com.foursquare.takehome.model.VisitingPersonDuringTime;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRecyclerView;
    private PersonAdapter personAdapter;
    private List<VisitingPersonDuringTime> visitorsList;
    private List<Person> visitors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visitorsList = new ArrayList<>();
        visitors = new ArrayList<Person>();

        rvRecyclerView = (RecyclerView) findViewById(R.id.rvRecyclerView);
        personAdapter = new PersonAdapter(visitorsList);

        //TODO hook up your adapter and any additional logic here

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvRecyclerView.setLayoutManager(mLayoutManager);
        rvRecyclerView.setAdapter(personAdapter);

        parseVenueFromResponse();
    }

    /**
     * Parsing a fake json response from assets/people.json
     */
    private void parseVenueFromResponse() {
        new AsyncTask<Void, Void, Venue>() {
            @Override
            protected Venue doInBackground(Void... params) {
                try {
                    InputStream is = getAssets().open("people.json");
                    InputStreamReader inputStreamReader = new InputStreamReader(is);

                    PeopleHereJsonResponse response = new Gson().fromJson(inputStreamReader, PeopleHereJsonResponse.class);
                    return response.getVenue();
                } catch (Exception e) {}

                return null;
            }

            @Override
            protected void onPostExecute(Venue venue) {
                //TODO use the venue object to populate your recyclerview


                //visitors list

                visitors = venue.getVisitors();

                //Getting Individual data for each visitor
                for(Person person:visitors){
                    String interval = actualTimeFromUnixTimeStamp(person.getArriveTime())+" - "+actualTimeFromUnixTimeStamp(person.getLeaveTime());
                    String name = person.getName();
                    visitorsList.add(new VisitingPersonDuringTime(name,person.getArriveTime(),person.getLeaveTime(),interval));
                }
                //method calling to sort the visitors based on arrival time
                sortTheVisitorsList();

                /**
                 * Whole Logic to get the idle times
                 */


                /*
                       1.  Merging the intervals of overlapping durations. For Example, [1,3],[4,6],[4,8],[9,12]-->
                            It will merge the common times and gives below output.
                                1, 3, 4, 8, 9, 12
                            (In our application, We will get visitor timings with merging) Eg: [4 am - 5.30 am], [5 am - 8 am], [9 am - 12 pm]
                                output: 4, 8, 9, 12

                       2.  Added Venue open and close timings to the above list and made an ArrayList(Venue open time: 2 am, close time: 8 pm

                            [2am, 4am, 8am 9am, 12pm, 8pm]--->

                             Here the idle times are 2am to 4am, 8am to 9 am and 12pm to 8pm

                       3.   Run loop by taking 2 elements each time and added to visitorList(ArrayList of Visitors and idletimes objects)

                       4.   Sorted whole list again with arrival times to display in sorted order in list.


                 */


                /**
                 * Algorithm to merge the intervals
                 *
                 * Took first element from visitorList and compared with all in list using condition
                 * if
                 *
                 * current visitor's arrival time is greater than previous visitors leaving time(No overlapping time),
                 * we don't need to change as they didn't came in the same time to visit, we will just make it as previous one.
                 *
                 * else(Overlapping)
                 *
                 * I will find the maximum of leaving times of both previous and current visitor and will create new visitor as
                 * with these max leaving time and previous's arrival time
                 */


                //merging intervals
                List<VisitingPersonDuringTime> mergingResult = new ArrayList();
                VisitingPersonDuringTime previous = visitorsList.get(0);
                for (VisitingPersonDuringTime current : visitorsList) {
                    if(current.getArrivalTime()>previous.getLeavingTime()){
                        mergingResult.add(previous);
                        previous = current;
                    }else{
                        VisitingPersonDuringTime merged = new VisitingPersonDuringTime("",previous.getArrivalTime(), Math.max(previous.getLeavingTime(), current.getLeavingTime()),"");
                        previous = merged;
                    }
                }
                mergingResult.add(previous);

                List<Long> idleTimes = new ArrayList<Long>();
                idleTimes.add(venue.getOpenTime());
                for(VisitingPersonDuringTime i:mergingResult){
                    idleTimes.add(i.getArrivalTime());
                    idleTimes.add(i.getLeavingTime());
                }

                idleTimes.add(venue.getCloseTime());

                // adding Idle time objects to visitors list

                for(int i=0;i<idleTimes.size();i+=2) {
                    if (idleTimes.size() % 2 == 0) {
                        String interval2= actualTimeFromUnixTimeStamp(idleTimes.get(i))+" - "+actualTimeFromUnixTimeStamp(idleTimes.get(i+1));
                        visitorsList.add(new VisitingPersonDuringTime("No Visitors", idleTimes.get(i), idleTimes.get(i+1), interval2));
                    }

                }
                //sort the final list of objects to view in the list
                sortTheVisitorsList();

                //Updating the recycler view with the current changed data
                personAdapter.notifyDataSetChanged();

            }
        }.execute();


    }
    // Getting actual time using Unix timestamp
    private String actualTimeFromUnixTimeStamp(Long time){
       return  new SimpleDateFormat("hh:mm a").format(new Date(time * 1000L));
    }
    // Calling custom comparator to sort the objects
    private void sortTheVisitorsList(){
        Collections.sort(visitorsList, new ComparatorToSort());

    }

}
