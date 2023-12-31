package com.bhaskar.aa45.coderevision;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bhaskar.aa45.coderevision.Adapters.FragPageAdapter;
import com.bhaskar.aa45.coderevision.Fragments.SolvedFragment;
import com.bhaskar.aa45.coderevision.Fragments.TriedFragment;
import com.bhaskar.aa45.coderevision.Fragments.WishlistFragment;
import com.bhaskar.aa45.coderevision.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;
import java.util.Stack;

public class HomeFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Home");

        //for SlideBar - solved,tried,wishlist
        TabLayout tl = view.findViewById(R.id.tabLayout);
        ViewPager vp = view.findViewById(R.id.viewPager);
        tl.setupWithViewPager(vp);
        replaceFragment(new SolvedFragment());
        FragPageAdapter adapter = new FragPageAdapter(requireActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.fragmentAdd(new SolvedFragment(), "Solved");
        adapter.fragmentAdd(new TriedFragment(), "Tried");
        adapter.fragmentAdd(new WishlistFragment(), "Wishlist");
        vp.setAdapter(adapter);

        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

       return view;
    }
    static Stack<Integer> tabNo = new Stack<>();

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.viewPager,fragment);
        fragmentTransaction.commit();
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_button);

        LinearLayout addSolved = dialog.findViewById(R.id.addSolved);
        LinearLayout addTried = dialog.findViewById(R.id.addTried);
        LinearLayout addWishlist = dialog.findViewById(R.id.addWishlist);
        addSolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(!tabNo.isEmpty()){
                    tabNo.empty();
                }
                tabNo.add(0);
                Intent intent = new Intent(getContext(),AddProblemActivity.class);
                startActivity(intent);
            }
        });
        addTried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(!tabNo.isEmpty()){
                    tabNo.empty();
                }
                tabNo.add(1);
                Intent intent = new Intent(getContext(),AddProblemActivity.class);
                startActivity(intent);
            }
        });
        addWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(!tabNo.isEmpty()){
                    tabNo.empty();
                }
                tabNo.add(2);
                Intent intent = new Intent(getContext(),AddProblemActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.FloatingActionButton;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onStart() {
        super.onStart();
        boolean dark;
        SharedPreferences sharedPref = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (!sharedPref.contains("dark")) {
            dark= currentMode == Configuration.UI_MODE_NIGHT_YES;
        }else{
            dark = sharedPref.getBoolean("dark",true);
        }

        if(dark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
}