package com.mrsoftit.youtubesub.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrsoftit.youtubesub.R;
import com.mrsoftit.youtubesub.modle.YoutubeSetData;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class YoutubeVideInfoActivity extends AppCompatActivity {


    String videoid;
    String videoName;
    String videoImageURL;
    String videoChannelID;
    String videoChannelName;
    String videoChannelLogo;
    String videurl;

    CircleImageView channel_logo;
    TextView channel_namae;
    TextView channel_id;

    RadioButton youtubeViewRedio;
    RadioButton youtubeSubscribeRedio;
    RadioButton youtubeLikeRedio;
    Button youtubeView;
    Button youtubeInfoAddButton;
    Button youtubeTime;
    PickerUI mPickerUI;

    int currentPosition = -1;
    List<String> options;

    //DataBase Connect
    FirebaseFirestore db;
    CollectionReference youtubeInfoAdd;

    String seleceVideoType = "views";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_vide_info);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            videoid = extras.getString("videoID");
            videoName = extras.getString("videoName");
            videoImageURL = extras.getString("videoImageUrl");
            videoChannelName = extras.getString("channelName");
            videoChannelLogo = extras.getString("chammelLogo");
            videoChannelID = extras.getString("channelID");
            videurl = extras.getString("videurl");
            //The key argument here must match that used in the other activity
        }
        channel_logo = findViewById(R.id.channel_logo);
        channel_namae = findViewById(R.id.channel_namae);
        channel_id = findViewById(R.id.channel_id);

        youtubeLikeRedio = findViewById(R.id.youtubeLikeRedio);
        youtubeSubscribeRedio = findViewById(R.id.youtubeSubscribeRedio);
        youtubeViewRedio = findViewById(R.id.youtubeViewRedio);
        youtubeView = findViewById(R.id.youtubeView);
        youtubeTime = findViewById(R.id.youtubeTime);
        mPickerUI = (PickerUI) findViewById(R.id.picker_ui_view);
        youtubeInfoAddButton =  findViewById(R.id.youtubeInfoAddButton);
        //FireStore Data Base
        db = FirebaseFirestore.getInstance();

        options = Arrays.asList(getResources().getStringArray(R.array.youtubetimeselec));

        //Populate list
        mPickerUI.setItems(this, options);
        mPickerUI.setColorTextCenter(R.color.background_picker);
        mPickerUI.setColorTextNoCenter(R.color.background_picker);
        mPickerUI.setBackgroundColorPanel(R.color.background_picker);
        mPickerUI.setLinesColor(R.color.background_picker);
        mPickerUI.setItemsClickables(true);
        mPickerUI.setAutoDismiss(false);

        Picasso.get()
                .load(videoChannelLogo)
                .into(channel_logo);

        channel_namae.setText(videoChannelName);
        channel_id.setText(videoChannelID);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view11);
        getLifecycle().addObserver(youTubePlayerView);
        YouTubePlayerTracker tracker = new YouTubePlayerTracker();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = videoid;
                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();
                youTubePlayer.addListener(tracker);


            }
        });



        youtubeInfoAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoutubeSetData setData = new YoutubeSetData(videoChannelName,videoChannelLogo,videoName,videurl,videoImageURL,videoid);


                youtubeInfoAdd = db.collection("youtube").document(seleceVideoType).collection("videoInfo");
                youtubeInfoAdd.add(setData)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                Toast.makeText(YoutubeVideInfoActivity.this, "Complet", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        youtubeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickerUI.setVisibility(View.VISIBLE);
                options = Arrays.asList(getResources().getStringArray(R.array.youtubetimeselec));
                //Populate list
                mPickerUI.setItems(v.getContext(), options);
                mPickerUI.setColorTextCenter(R.color.background_picker);
                mPickerUI.setColorTextNoCenter(R.color.background_picker);
                mPickerUI.setBackgroundColorPanel(R.color.background_picker);
                mPickerUI.setLinesColor(R.color.background_picker);
                mPickerUI.setItemsClickables(false);
                mPickerUI.setAutoDismiss(false);

                int randomColor = getRandomColor();
                PickerUISettings pickerUISettings =new PickerUISettings.Builder().withItems(options)
                        .withBackgroundColor(randomColor)
                        .build();
                mPickerUI.setSettings(pickerUISettings);
                if(currentPosition==-1) {
                    mPickerUI.slide();
                }
                else{
                    mPickerUI.slide(currentPosition);
                }

                mPickerUI.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
                    @Override
                    public void onItemClickPickerUI(int which, int position, String valueResult) {
                        currentPosition = position;
                        youtubeTime.setText(valueResult);
                        Toast.makeText(YoutubeVideInfoActivity.this, valueResult, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        youtubeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPickerUI.setVisibility(View.VISIBLE);
                options = Arrays.asList(getResources().getStringArray(R.array.Youtube_View));
                //Populate list
                mPickerUI.setItems(v.getContext(), options);
                mPickerUI.setColorTextCenter(R.color.background_picker);
                mPickerUI.setColorTextNoCenter(R.color.background_picker);
                mPickerUI.setBackgroundColorPanel(R.color.background_picker);
                mPickerUI.setLinesColor(R.color.background_picker);
                mPickerUI.setItemsClickables(false);
                mPickerUI.setAutoDismiss(false);

                int randomColor = getRandomColor();
                PickerUISettings pickerUISettings =new PickerUISettings.Builder().withItems(options)
                        .withBackgroundColor(randomColor)
                        .build();
                mPickerUI.setSettings(pickerUISettings);
                if(currentPosition==-1) {
                    mPickerUI.slide();
                }
                else{
                    mPickerUI.slide(currentPosition);
                }

                mPickerUI.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
                    @Override
                    public void onItemClickPickerUI(int which, int position, String valueResult) {
                        currentPosition = position;
                        youtubeView.setText(valueResult);
                        Toast.makeText(YoutubeVideInfoActivity.this, valueResult, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        youtubeViewRedio.setChecked(true);
        youtubeViewRedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if( youtubeViewRedio.isChecked()){
                   seleceVideoType = "views";
                   youtubeSubscribeRedio.setChecked(false);
                   youtubeLikeRedio.setChecked(false);
                }
            }
        });
        youtubeSubscribeRedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( youtubeSubscribeRedio.isChecked()){
                    seleceVideoType = "subscribe";
                    youtubeViewRedio.setChecked(false);
                    youtubeLikeRedio.setChecked(false);
                }
            }
        });

        youtubeLikeRedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( youtubeLikeRedio.isChecked()){
                    seleceVideoType = "like";
                    youtubeViewRedio.setChecked(false);
                    youtubeSubscribeRedio.setChecked(false);
                }
            }
        });


    }

    private int getRandomColor() {
        // generate the random integers for r, g and b value
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return Color.rgb(r, g, b);
    }

}