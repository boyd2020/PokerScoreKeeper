package com.example.pokerscorekeeper.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.playertab.ContactInfoFragment;
import com.example.pokerscorekeeper.playertab.ContactOverviewFragment;
import com.example.pokerscorekeeper.playertab.ContactScoresFragment;
import com.example.pokerscorekeeper.utils.BundleUtils;

public class ContactPagerAdapter extends FragmentPagerAdapter {

    private static final int CONTACT_INFO = 0, CONTACT_SCORES = 1, PAGER_COUNT = 2;

    private int playerId;

    public ContactPagerAdapter(FragmentManager fm, int playerId) {
        super(fm);
        this.playerId = playerId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        //Create Bundle and add Player
        Bundle bundle = new Bundle();
        bundle.putInt(BundleUtils.EXTRA_PLAYER_ID, playerId);

        switch (position)
        {
            case CONTACT_INFO:
                fragment = new ContactInfoFragment();
                fragment.setArguments(bundle);
                break;

            case CONTACT_SCORES:
                fragment = new ContactScoresFragment();
                fragment.setArguments(bundle);
                break;

            default:
                fragment = new ContactInfoFragment();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
