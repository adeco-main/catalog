package com.sherdle.universal.nav;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherdle.universal.R;
import com.sherdle.universal.activity.MainActivity;

import java.util.List;

public class NavDrawerFragment extends Fragment implements NavDrawerCallback {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    
    private NavDrawerCallback mCallbacks;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private int mCurrentSelectedPosition;
    
    private List<NavItem> mConfiguration;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view;
        view = inflater.inflate(R.layout.drawer_fragment, container, false);

        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);
        mDrawerList.setItemAnimator(null);

        final List<NavItem> NavItems = getConfiguration();
        NavDrawerAdapter adapter = new NavDrawerAdapter(NavItems, NavDrawerFragment.this);
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavDrawerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavDrawerCallback.");
        }
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void loadInitialItem(){
        final List<NavItem> NavItems = getConfiguration();
        selectItem(mCurrentSelectedPosition, NavItems.get(mCurrentSelectedPosition));
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        if (!((MainActivity) getActivity()).useTabletMenu()) {
            mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    if (!isAdded()) return;
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    if (!isAdded()) return;

                    getActivity().invalidateOptionsMenu();
                }
            };

            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mActionBarDrawerToggle.syncState();
                }
            });

            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }
        
        //TODO This works (hides the drawer if there is only 1 item), but makes settings and favorites unreachable
        if (NavListConfig.USE_NEW_DRAWER == false && getConfiguration().size() == 1){
        	mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        
	    if (NavListConfig.USE_NEW_DRAWER == true && getConfiguration().size() == 2){
	    	mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	    }
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    public void selectItem(int position, NavItem item) {
    	//If on start, item is section, change it.
    	if (item.getType() == NavItem.SECTION || item.getType() ==  NavItem.TOP){
    		position = position + 1;
    		item = getConfiguration().get(position);
    		selectItem(position, item);
    		return;
    	}
    	
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            if (!((MainActivity) getActivity()).useTabletMenu()) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            }
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, item);
        }
        ((NavDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!((MainActivity) getActivity()).useTabletMenu())
            mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, NavItem item) {
    	//TODO we can also call this method here, but that won't set an initial item. 
        //mCallbacks.onNavigationDrawerItemSelected(position, item);
    	if (item.getType() != NavItem.SECTION){
    		selectItem(position, item);
    	}
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }
    
    private List<NavItem> getConfiguration(){
    	if (null == mConfiguration){
    		mConfiguration = NavListConfig.getNavFragment();

    	    if (NavListConfig.USE_NEW_DRAWER == true){
    	    	mConfiguration.add(0, new NavItem("Header", NavItem.TOP));
    	    }
    	}
    	
    	return mConfiguration;
    }

}
