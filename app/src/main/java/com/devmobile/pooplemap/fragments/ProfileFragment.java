package com.devmobile.pooplemap.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.devmobile.pooplemap.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Add buttons and actions for the profile fragment
        RelativeLayout btnAboutUs = view.findViewById(R.id.about_us_layout);
        RelativeLayout btnContactUs = view.findViewById(R.id.contact_us_layout);
        RelativeLayout btnLogout = view.findViewById(R.id.log_out_layout);

        btnAboutUs.setOnClickListener(this::onClick);
        btnContactUs.setOnClickListener(this::onClick);
        btnLogout.setOnClickListener(this::onClick);

        return view;
    }

    // Add the actions for the buttons
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_us_layout:
                // Open the about us activity
                // Print the about us information
                onClickAnimation(view);
                System.out.println("About us");
                break;
            case R.id.contact_us_layout:
                // Open the contact us activity
                onClickAnimation(view);

                System.out.println("Contact us");
                break;
            case R.id.log_out_layout:
                // Log out the user
                onClickAnimation(view);
                System.out.println("Log out");
                break;
        }
    }

    public void onClickAnimation(View view) {
        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Restore the original size of the view after the animation completes
                        view.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(200)
                                .start();
                    }
                })
                .start();
    }

}