package com.example.maddi.logme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InfoFragment extends Fragment {

    int position;


    public InfoFragment() {
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        final OnFloatingButtonClickListener mListener;

        try {
            mListener = (OnFloatingButtonClickListener) getContext();
            Log.d("mContext is ", getContext().toString());
        } catch (ClassCastException ex) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }



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


                if (ageET.getText().toString().length() == 0) {
                    ageET.setError("Age is required!");
                    return;
                }
                if (heightET.getText().toString().length() == 0) {
                    heightET.setError("Height is required!");
                    return;
                }
                if (weightET.getText().toString().length() == 0) {
                    weightET.setError("Weight is required!");
                    return;
                }
                if(myRadioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getContext(), "Gender is required!", Toast.LENGTH_SHORT).show();
                    return;
                }


                Bundle infos = new Bundle();

                infos.putString("Age",ageET.getText().toString());
                infos.putString("Height",heightET.getText().toString());
                infos.putString("Weight",weightET.getText().toString());
                infos.putInt("Gender",position);

                Intent intent = getActivity().getIntent();
                intent.putExtras(infos);


                mListener.onFloatingButtonClicked();

            }
        });


        return rootView;
    }

    public interface OnFloatingButtonClickListener {
        void onFloatingButtonClicked();
    }
}

