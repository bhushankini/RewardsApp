package com.bpk.rewards;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bpk.rewards.fragments.AccountFragment;
import com.bpk.rewards.fragments.HistoryFragment;
import com.bpk.rewards.fragments.LeaderBoardFragment;
import com.bpk.rewards.fragments.RewardsFragment;
import com.bpk.rewards.fragments.VideoFragment;
import com.bpk.rewards.model.Statistics;
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

    private static final String TAG = "Rewards" ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txtPoints;
    private DatabaseReference mFirebaseUserDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseStatisticsDatabase;
    private Button btnRetry;
    private RelativeLayout rlNoConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        rlNoConnection = (RelativeLayout) findViewById(R.id.rl_noconnection);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        btnRetry =(Button) findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        txtPoints = (TextView) findViewById(R.id.toolbar_points);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseUserDatabase = mFirebaseInstance.getReference(User.FIREBASE_USER_ROOT);
        mFirebaseStatisticsDatabase = mFirebaseInstance.getReference(Statistics.FIREBASE_STATISTICS_ROOT);
        addPointsChangeListener();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkConnection();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new VideoFragment(), "Home");
        adapter.addFrag(new RewardsFragment(), "Rewards");
        adapter.addFrag(new LeaderBoardFragment(), "Leaderboard");
        adapter.addFrag(new HistoryFragment(), "History");
        adapter.addFrag(new AccountFragment(), "Account");
        viewPager.setAdapter(adapter);
    }

    private void checkConnection(){
        if(Utils.isNetworkAvailable(this)){
            viewPager.setVisibility(View.VISIBLE);
            rlNoConnection.setVisibility(View.GONE);
        } else {
            viewPager.setVisibility(View.GONE);
            rlNoConnection.setVisibility(View.VISIBLE);

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

                PrefUtils.saveToPrefs(MainActivity.this, Constants.LAST_DAILY_REWARD,user.getLastopen());
                PrefUtils.saveToPrefs(MainActivity.this, Constants.USER_NAME,user.getName());

                boolean isNewDay = Utils.isNewDate(user.getLastopen(),PrefUtils.getFromPrefs(MainActivity.this,Constants.SERVER_TIME,user.getLastopen()));

                Log.e("KHUSHI", "KHUSHI is new Day " + isNewDay);
                initStatistics(isNewDay);

           //     PrefUtils.saveToPrefs(MainActivity.this, Constants.USER_ID, user.getUserId());
                txtPoints.setText(" " + user.getPoints() + "  ");
                Log.e("KHUSHI", "KHUSHI last open " + user.getLastopen());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("KHUSHI", "Failed to read user", error.toException());
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }

    private void initStatistics(boolean isNewDay){
        final String userId = PrefUtils.getFromPrefs(this, Constants.USER_ID, "unknownuser");
        if (isNewDay){

            PrefUtils.saveIntToPrefs(this, Constants.DICE_COUNT,0);
            PrefUtils.saveIntToPrefs(this, Constants.TTT_COUNT,0);
            mFirebaseStatisticsDatabase.child(userId).child("dice").setValue(0);
            mFirebaseStatisticsDatabase.child(userId).child("tictactoe").setValue(0);
            return;
        }

        mFirebaseStatisticsDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"DDDDDDDD stats "+dataSnapshot.getValue());
                Statistics stats = dataSnapshot.getValue(Statistics.class);
                if(stats == null){
                    Log.e(TAG,"DDDDDDDD stats null");
                } else {
                    PrefUtils.saveIntToPrefs(MainActivity.this, Constants.DICE_COUNT,stats.getDice());
                    PrefUtils.saveIntToPrefs(MainActivity.this, Constants.TTT_COUNT,stats.getTictactoe());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
