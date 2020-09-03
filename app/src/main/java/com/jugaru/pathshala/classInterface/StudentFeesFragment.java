package com.jugaru.pathshala.classInterface;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class StudentFeesFragment extends Fragment {
    EditText amount , note , name , upiid ;
    Button pay ;
    TextView feeAmount , noFacility ;
    private LinearLayout paymentCard ;

    final  int UPI_PAYMENT=0 ;

    public StudentFeesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_fees, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        final Classes classes =getActivity().getIntent().getParcelableExtra("SingleClass");

        feeAmount.setText(classes.getClassFee());
        if(classes.getPaymentUPI() == null){
            noFacility.setVisibility(View.VISIBLE);
        }else {
            paymentCard.setVisibility(View.VISIBLE);
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String amountText = classes.getClassFee();
                    String noteText = note.getText().toString();
                    String nameText = name.getText().toString();
                    String upiText = classes.getPaymentUPI();
                    payUsingUpi(amountText , noteText , nameText , upiText);
                }
            });
        }
    }

    private void payUsingUpi(String amountText, String noteText, String nameText, String upiText) {
        Uri uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa" , upiText)
                .appendQueryParameter("pn" , nameText)
                .appendQueryParameter("tn" , noteText)
                .appendQueryParameter("am" , amountText)
                .appendQueryParameter("cu" , "INR").build();
        Intent upi_payment = new Intent(Intent.ACTION_VIEW);
        upi_payment.setData(uri);
        Intent chooser = Intent.createChooser(upi_payment , "Pay With");
        if(null != chooser.resolveActivity(getActivity().getPackageManager())){
            startActivityForResult(chooser , UPI_PAYMENT);
        }else {
            Toast.makeText(getContext(), "NO UPI APP FOUND", Toast.LENGTH_SHORT).show();
        }
    }
    private  void init(View view){
        note = view.findViewById(R.id.note);
        name = view.findViewById(R.id.name);
        pay = view.findViewById(R.id.fee_button);
        feeAmount = view.findViewById(R.id.fee_amount);
        noFacility = view.findViewById(R.id.no_facility);
        paymentCard = view.findViewById(R.id.payment_board);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case UPI_PAYMENT:
                if(RESULT_OK == resultCode || resultCode ==11){
                    if(data!=null){
                        String txt =data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult:" + txt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("Nothing");
                        upiPaymentDataOperation(dataList);
                    }else {
                        Log.d("UPI", "onActivityResult:" + "Return Data Is Null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("Nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }else {
                    Log.d("UPI", "onActivityResult:" + "Return Data Is Null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("Nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if(isConnectionAvaliable(Objects.requireNonNull(getActivity()).getApplicationContext()))
        {
            String str = data.get(0);

            Log.d("UPIPay", "UPI_paymentOperation:" + str);
            String paymentCancel = "";
            if(str == null)str = "discard";
            String status = "";
            String approvalRef = "";
            String[] response = str.split("&") ;
            for(int i = 0 ; i < response.length ; i++){

                String equalStr[] = response[i].split("=");
                if(equalStr.length>2){
                    if(equalStr[0].toLowerCase().equals("Status".toLowerCase())){
                        status=equalStr[i].toLowerCase();
                    }
                    else if(equalStr[0].toLowerCase().equals("approval Ref".toLowerCase()) ||
                            equalStr[0].toLowerCase().equals("txnRef".toLowerCase())){
                        approvalRef = equalStr[i];
                    }
                }
                else {
                    paymentCancel = "Payment cancel by user";
                    if(status.equals("success")){
                        Toast.makeText(getContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                    Log.d("UPI", "responseStr:" + approvalRef);
                    }
                    else if ("Payment cancel by user".equals(paymentCancel)){
                        Toast.makeText(getContext(), "Payment Cancel by user", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Transaction Failed Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isConnectionAvaliable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager!= null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()
                    && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()){
                return true;

            }

        }
        return false;
    }
}