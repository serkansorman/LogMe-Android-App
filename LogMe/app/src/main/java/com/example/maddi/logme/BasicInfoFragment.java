package com.example.maddi.logme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasicInfoFragment extends Fragment {

    int position;


    public BasicInfoFragment() {
    }

    public static BasicInfoFragment newInstance() {
        BasicInfoFragment fragment = new BasicInfoFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_basicinfo, container, false);
        final OnFloatingButtonClickListener mListener;

        try {
            mListener = (OnFloatingButtonClickListener) getContext();
            Log.d("mContext is ", getContext().toString());
        } catch (ClassCastException ex) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        final Button next = (Button) rootView.findViewById(R.id.nextbutton);

        final FloatingActionButton fab1 = (FloatingActionButton) getActivity().findViewById(R.id.next);
        final EditText ageET = (EditText) rootView.findViewById(R.id.ageInput);
        final EditText weightET = (EditText) rootView.findViewById(R.id.weightInput);
        final EditText heightET = (EditText) rootView.findViewById(R.id.heightInput);

        final RadioGroup myRadioGroup = (RadioGroup) rootView.findViewById(R.id.genderGroup);
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                position = myRadioGroup.indexOfChild(rootView.findViewById(checkedId));
                if (position == 0) {
                    Log.d("Gender is ", "Male");
                    //getUsersRef("gender").setValue("Male");
                } else {
                    Log.d("Gender is ", "Female");
                    //getUsersRef("gender").setValue("Female");
                }
            }
        });

        ImageView userPhoto = (ImageView) getActivity().findViewById(R.id.userPhoto);
        userPhoto.setImageResource(R.drawable.run3);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (nameET.getText().toString().length() == 0) {
                    nameET.setError("Name is required!");
                    return;
                }*/
                mListener.onFloatingButtonClicked();
                //getUsersRef("name").setValue(nameET.getText().toString());
                //getUsersRef("phone").setValue(phoneET.getText().toString());
                //getUsersRef("age").setValue(ageET.getText().toString());
                //getUsersRef("height").setValue(heightET.getText().toString());
                //getUsersRef("weight").setValue(weightET.getText().toString());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (nameET.getText().toString().length() == 0) {
                    nameET.setError("Name is required!");
                    return;
                }*/
                mListener.onFloatingButtonClicked();
                //getUsersRef("name").setValue(nameET.getText().toString());
                //getUsersRef("phone").setValue(phoneET.getText().toString());
                //getUsersRef("age").setValue(ageET.getText().toString());
                //getUsersRef("height").setValue(heightET.getText().toString());
                //getUsersRef("weight").setValue(weightET.getText().toString());
            }
        });


        return rootView;
    }

    public interface OnFloatingButtonClickListener {
        public void onFloatingButtonClicked();
    }
}

