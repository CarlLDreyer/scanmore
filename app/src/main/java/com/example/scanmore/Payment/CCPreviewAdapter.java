package com.example.scanmore.Payment;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CCPreviewAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mManager;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public CCPreviewAdapter(FragmentManager manager) {
        super(manager);
        this.mManager = manager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        notifyDataSetChanged();
    }

    public void removeFragment(int position) {
        mFragmentTitleList.remove(position);
        mManager.beginTransaction().remove(mFragmentList.get(position)).commit();
        mFragmentList.remove(position);
        notifyDataSetChanged();
    }

    public List<Fragment> getFragmentList(){
        return mFragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

}