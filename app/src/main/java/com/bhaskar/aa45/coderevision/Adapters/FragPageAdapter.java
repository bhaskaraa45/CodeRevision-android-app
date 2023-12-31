package com.bhaskar.aa45.coderevision.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bhaskar.aa45.coderevision.Fragments.SolvedFragment;
import com.bhaskar.aa45.coderevision.Fragments.TriedFragment;
import com.bhaskar.aa45.coderevision.Fragments.WishlistFragment;



import java.util.ArrayList;

public class FragPageAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> title = new ArrayList<>();

    public FragPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1 :
                return new TriedFragment();
            case 2 :
                return new WishlistFragment();
            default:
                return new SolvedFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void fragmentAdd(Fragment fragment,String string){
        fragmentArrayList.add(fragment);
        title.add(string);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
