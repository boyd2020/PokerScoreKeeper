package com.example.pokerscorekeeper.playertab;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokerscorekeeper.activities.AddScoreActivity;
import com.example.pokerscorekeeper.activities.EditPlayerActivity;
import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.adapters.ContactPagerAdapter;
import com.example.pokerscorekeeper.managers.PlayerManager;
import com.example.pokerscorekeeper.model.Player;
import com.example.pokerscorekeeper.utils.BundleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactOverviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbar;

    //Objects/Variables
    private int playerId;
    private Player player;
    private Unbinder unbinder;
    private ContactPagerAdapter adapter;
    private PlayerManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_overview_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {
            playerId = savedInstanceState.getInt(BundleUtils.EXTRA_PLAYER_ID);
            player = savedInstanceState.getParcelable(BundleUtils.EXTRA_PLAYER);
        }
        else
            playerId = getArguments().getInt(BundleUtils.EXTRA_PLAYER_ID);


        manager = new PlayerManager(getContext());
        adapter = new ContactPagerAdapter(getChildFragmentManager(), playerId);
        viewPager.setAdapter(adapter);

        addTabLayout();

        getActivity().getSupportLoaderManager().destroyLoader(40);
        getActivity().getSupportLoaderManager().initLoader(40, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BundleUtils.EXTRA_PLAYER_ID, playerId);
        outState.putParcelable(BundleUtils.EXTRA_PLAYER, player);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                break;

            case R.id.menuEditContact:
                intent = new Intent(getContext(), EditPlayerActivity.class);
                intent.putExtra(BundleUtils.EXTRA_PLAYER, player);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;

            case R.id.menuAddScore:
                intent = new Intent(getContext(), AddScoreActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return manager.getPlayerInfo(playerId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        player = manager.getPlayerFromCursor(data);
        collapsingToolbar.setTitle(player.getName());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addTabLayout()
    {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_contact_info)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_scores)));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
