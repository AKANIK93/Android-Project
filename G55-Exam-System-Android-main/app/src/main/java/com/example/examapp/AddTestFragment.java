package com.example.examapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText catT,testT,timeT;
    private String  catStr,testStr,timeStr;
    private Button addQB;
    private ImageView backB;

    public AddTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTestFragment newInstance(String param1, String param2) {
        AddTestFragment fragment = new AddTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_test, container, false);

        init(view);


        addQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData())
                {
                    v = inflater.inflate(R.layout.question_item_layout, container, false);
                }
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        return view;
    }


    private void init(View view)
    {
        catT = view.findViewById(R.id.add_test_cat_name);
        testT = view.findViewById(R.id.add_test_name);
        timeT = view.findViewById(R.id.test_time);
        backB = view.findViewById(R.id.add_test_backB);
        addQB = view.findViewById(R.id.add_test_questionB);
    }

    private boolean validData()
    {
        catStr = catT.getText().toString().trim();
        testStr = testT.getText().toString().trim();
        timeStr = timeT.getText().toString().trim();

        if (catStr.isEmpty())
        {
            catT.setError("Enter Category Name");
            return false;
        }
        if(testStr.isEmpty())
        {
            testT.setError("Enter Test Name");
            return false;
        }
        if(timeStr.isEmpty())
        {
            timeT.setError("Enter Time");
            return false;
        }
        return true;
    }



}