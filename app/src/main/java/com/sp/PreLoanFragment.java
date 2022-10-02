package com.sp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PreLoanFragment extends Fragment {
    private Button buttonSimulate;

    public PreLoanFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pre_loan, container, false);
        buttonSimulate = view.findViewById(R.id.button_simulate);
        buttonSimulate.setOnClickListener(onSimulate);
        return view;
    }

    private View.OnClickListener onSimulate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(),LoanSimulationActivity.class));
        }
    };
}