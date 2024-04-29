package com.example.prototype_1.ServiceMilegiOne.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.prototype_1.ServiceMilegiOne.R;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private CardView electrician ,plumber , painter , carpenter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        electrician = root.findViewById(R.id.electricians);
        painter = root.findViewById(R.id.painters);
        plumber = root.findViewById(R.id.plumbers);
        carpenter = root.findViewById(R.id.carpenters);


        electrician.setOnClickListener(this);
        painter.setOnClickListener(this);
        plumber.setOnClickListener(this);
        carpenter.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.electricians:
                Intent ele = new Intent(getActivity() , ElectricianActivity.class);
                ele.putExtra("job" ,"Electrician");
                startActivity(ele);
                break;


            case R.id.painters:
                Intent painter = new Intent(getActivity() , PainterActivity.class);
                painter.putExtra("job" ,"Painter");
                startActivity(painter);
                break;

            case R.id.plumbers:
                Intent plumber = new Intent(getActivity() , PlumberActivity.class);
                plumber.putExtra("job" ,"Plumber");
                startActivity(plumber);
                break;

            case R.id.carpenters:
                Intent carpenter = new Intent(getActivity() , CarpenterActivity.class);
                carpenter.putExtra("job" ,"Carpenter");
                startActivity(carpenter);
                break;

        }

    }



}