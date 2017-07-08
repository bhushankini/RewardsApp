package com.bpk.rewards.games.dice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpk.rewards.R;
import com.bpk.rewards.model.Statistics;
import com.bpk.rewards.model.User;
import com.bpk.rewards.model.UserTransaction;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DiceActivity extends AppCompatActivity {

    public static final Random RANDOM = new Random();
    private static final String TAG = "DICE";
    private Button rollDices;
    private ImageView imageView1, imageView2;
    private TextView txtMessage;
    private int total = 0;
    private boolean diceOneStatus = false;
    private boolean diceTwoStatus = false;
    private int gameCount = 0;

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseUserDatabase;
    private DatabaseReference mFirebaseTransactionDatabase;
    private DatabaseReference mFirebaseStatsticsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        rollDices = (Button) findViewById(R.id.rollDices);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseUserDatabase = mFirebaseInstance.getReference(User.FIREBASE_USER_ROOT);

        mFirebaseTransactionDatabase = mFirebaseInstance.getReference(UserTransaction.FIREBASE_TRANSACTION_ROOT);
        mFirebaseStatsticsDatabase = mFirebaseInstance.getReference(Statistics.FIREBASE_STATISTICS_ROOT);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Roll Dice");
        }

        rollDices.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                rollDices.setEnabled(false);
                final Animation anim1 = AnimationUtils.loadAnimation(DiceActivity.this, R.anim.shake);
                final Animation anim2 = AnimationUtils.loadAnimation(DiceActivity.this, R.anim.shake);

                final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        total = 0;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int value = randomDiceValue();
                        total = total + value;
                        int res = getResources().getIdentifier("dice_" + value, "drawable", "com.bpk.rewards");
                        if (animation == anim1) {
                            imageView1.setImageResource(res);
                            diceOneStatus = true;
                        } else if (animation == anim2) {
                            imageView2.setImageResource(res);
                            diceTwoStatus = true;
                        }

                        if(diceOneStatus && diceTwoStatus){
                            diceOneStatus = false;
                            diceTwoStatus = false;
                            rollDices.setEnabled(true);
                            gameCount(total,true);
                            total = 0;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };

                anim1.setAnimationListener(animationListener);
                anim2.setAnimationListener(animationListener);

                imageView1.startAnimation(anim1);
                imageView2.startAnimation(anim2);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public static int randomDiceValue() {
        return RANDOM.nextInt(6) + 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameCount(total, false);

    }

    private void gameCount(final int points, final boolean updateCount){

            final String userId = PrefUtils.getFromPrefs(this, Constants.USER_ID, "unknownuser");
        mFirebaseStatsticsDatabase.child(userId).child("dice").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        long totalPoints = (long) dataSnapshot.getValue();
                        Log.d("TAG","KKKKK totla "+totalPoints);
                        gameCount = (int) totalPoints;
                        if(gameCount >=2) {
                            txtMessage.setText("You already rolled 3 times today. Come back tomorrow");
                            rollDices.setEnabled(false);
                        } else {
                            txtMessage.setText("You have "+(3 - gameCount)+" dice rolls");
                            rollDices.setEnabled(true);
                        }
                        if(updateCount) {
                            mFirebaseStatsticsDatabase.child(userId).child("dice").setValue(gameCount+1);
                            Log.d("TAG","KKKKK update db "+gameCount);

                            rewardsPoints(points, "Roll Dice", "Game");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }


    private void rewardsPoints(final int points, final String source, final String type) {
        final String userId = PrefUtils.getFromPrefs(this, Constants.USER_ID, "unknownuser");
        mFirebaseUserDatabase.child(userId).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    long totalPoints = (long) dataSnapshot.getValue();
                    mFirebaseUserDatabase.child(userId).child("points").setValue(totalPoints + points);
                    UserTransaction ut = new UserTransaction();
                    ut.setSource(source);
                    ut.setPoints(points);
                    ut.setType(type);
                    mFirebaseTransactionDatabase.child(userId).push().setValue(ut.toMap());
                    showPointsRewardsDialog(points);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showPointsRewardsDialog(int points){
        new SweetAlertDialog(this, SweetAlertDialog.BUTTON_POSITIVE)
                .setTitleText("Congratulations!!!")
                .setCustomImage(R.mipmap.ic_launcher)
                .setContentText("Congratulations you got "+points+ " points")
                .show();
    }
}