package com.bpk.rewards;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.bpk.rewards.fragments.AccountFragment;
import com.bpk.rewards.fragments.HistoryFragment;
import com.bpk.rewards.fragments.LeaderBoardFragment;
import com.bpk.rewards.fragments.RewardsFragment;
import com.bpk.rewards.fragments.VideoFragment;
import com.bpk.rewards.model.User;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;
import com.bpk.rewards.utility.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txtPoints;
    private DatabaseReference mFirebaseUserDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
       //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        txtPoints = (TextView) findViewById(R.id.toolbar_points);
      //  txtPoints.setText("100 points");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseUserDatabase = mFirebaseInstance.getReference(User.FIREBASE_USER_ROOT);
        addPointsChangeListener();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new VideoFragment(), "Home");
        adapter.addFrag(new HistoryFragment(), "History");
        adapter.addFrag(new RewardsFragment(), "Rewards");
        adapter.addFrag(new LeaderBoardFragment(), "Leaderboard");
        adapter.addFrag(new AccountFragment(), "Account");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    private void addPointsChangeListener() {
        // User data change listener
        mFirebaseUserDatabase.child(Utils.getUserId(MainActivity.this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    return;
                }
                PrefUtils.saveToPrefs(MainActivity.this, Constants.USER_ID, user.getUserId());
                txtPoints.setText(" "+user.getPoints() + "  ");
                Log.e("KHUSHI", "KHUSHI last open "+ user.getLastopen());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("KHUSHI", "Failed to read user", error.toException());
            }
        });
    }
}
