package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class stats extends AppCompatActivity {
    //reference to All Orders
    DatabaseReference allOrdersDB = FirebaseDatabase.getInstance().getReference("Clients").child("All Commands");

    //reference to Delivered Orders in Commands
    DatabaseReference deliveredOrdersDB = FirebaseDatabase.getInstance().getReference("Clients").child("Delivered");
    DatabaseReference  onProcessOrdersDB = FirebaseDatabase.getInstance().getReference("Clients").child("On Process");
    DatabaseReference failedOrdersDB = FirebaseDatabase.getInstance().getReference("Clients").child("Failed");

    TextView totalOrders,deliveredCount, deliveredPercentage,onProcessCount,onProcessPercentage, failedCount, failedPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> data = new ArrayList<>();


        totalOrders = findViewById(R.id.totalOrders);
        onProcessCount = findViewById(R.id.onProcessCount);
        onProcessPercentage = findViewById(R.id.onProcessPercentage);

        deliveredCount = findViewById(R.id.deliveredCount);
        deliveredPercentage = findViewById(R.id.deliveredPercentage);

        failedCount = findViewById(R.id.failedCount);
        failedPercentage = findViewById(R.id.failedPercentage);


        readAllOrdersData(new AllOrdersData() {
            @Override
            public void getAllOrders(List<Client> allOrders) {
                readDeliveredOrdersData(new DeliveredOrdersData() {
                    @Override
                    public void getDeliveredOrders(List<Client> deliveredOrders) {
                        readFailedOrdersData(new FailedOrdersData() {
                            @Override
                            public void getFailedOrders(List<Client> failed) {
                                readOnProcessOrdersData(new OnProcessOrdersData() {
                                    @Override
                                    public void getOnProcessOrders(List<Client> onProcess) {
                                        data.add(new PieEntry(deliveredOrders.size(),"Delivered Orders"));
                                        data.add(new PieEntry(onProcess.size(),"OnProcess Orders"));
                                        data.add(new PieEntry(failed.size(),"Failed Orders"));

                                        totalOrders.setText(""+allOrders.size());
                                        deliveredCount.setText(""+deliveredOrders.size());
                                        deliveredPercentage.setText(""+deliveredOrders.size()*100/allOrders.size());

                                        onProcessCount.setText(""+onProcess.size());
                                        onProcessPercentage.setText(""+onProcess.size()*100/allOrders.size());

                                        failedCount.setText(""+failed.size());
                                        failedPercentage.setText(""+failed.size()*100/allOrders.size());

                                        PieDataSet pieDataSet = new PieDataSet(data,"Stats");
                                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        pieDataSet.setValueTextColor(0xFF16C79A);
                                        pieDataSet.setValueTextSize(16f);

                                        PieData pieData = new PieData(pieDataSet);
                                        pieChart.setData(pieData);
                                        pieChart.getDescription().setEnabled(false);
                                        pieChart.setCenterText("Orders");
                                        pieChart.animate();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    //    data.add
    }



    private void readAllOrdersData(AllOrdersData allOrdersData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<Client> clients = new ArrayList<>();

                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                }

                allOrdersData.getAllOrders(clients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        allOrdersDB.addValueEventListener(productListener);
    }



    private interface AllOrdersData{
        void getAllOrders(List<Client> clients);

    }
    private void readDeliveredOrdersData(DeliveredOrdersData deliveredOrdersData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<Client> clients = new ArrayList<>();

                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                }

                deliveredOrdersData.getDeliveredOrders(clients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        deliveredOrdersDB.addValueEventListener(productListener);
    }



    private interface DeliveredOrdersData{
        void getDeliveredOrders(List<Client> clients);

    }

    private void readFailedOrdersData(FailedOrdersData failedOrdersData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<Client> clients = new ArrayList<>();

                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                }

                failedOrdersData.getFailedOrders(clients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        failedOrdersDB.addValueEventListener(productListener);
    }



    private interface FailedOrdersData{
        void getFailedOrders(List<Client> clients);

    }

    private void readOnProcessOrdersData(OnProcessOrdersData onProcessOrdersData){
        //load the list of item from firebase in the list
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the list to renew it
                List<Client> clients = new ArrayList<>();

                //get the list of clients with their keys
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Client client = snapshot.getValue(Client.class);
                    clients.add(client);
                }

                onProcessOrdersData.getOnProcessOrders(clients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //empty
            }
        };
        onProcessOrdersDB.addValueEventListener(productListener);
    }



    private interface OnProcessOrdersData{
        void getOnProcessOrders(List<Client> clients);

    }


}