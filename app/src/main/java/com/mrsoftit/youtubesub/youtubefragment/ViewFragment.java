package com.mrsoftit.youtubesub.youtubefragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mrsoftit.youtubesub.R;
import com.mrsoftit.youtubesub.modle.YoutubeSetData;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ViewFragment() {
        // Required empty public constructor
    }
    public static ViewFragment newInstance(String param1, String param2) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    List<YoutubeSetData> dataList = new ArrayList<>();

    //Database
    FirebaseFirestore db;
    CollectionReference youtubeInfoAdd;
    DocumentReference docRef;
    int randomID;
    String videoId;
    //
    Button nextVideoView;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        youtubeInfoAdd = db.collection("youtube").document("views").collection("videoInfo");
        nextVideoView = view.findViewById(R.id.nextVideoView);

        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        YouTubePlayerTracker tracker = new YouTubePlayerTracker();

        youtubeInfoAdd.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String videoId = document.get("youtubeVideId").toString();
                            String videoUrl = document.get("youtubeVideId").toString();
                            dataList.add(new YoutubeSetData(videoUrl,videoId));
                        }

                        if (dataList.isEmpty()){
                            return;
                        }

                        Random random = new Random();
                        if (!dataList.isEmpty()){
                            randomID = random.nextInt(dataList.size());
                        }
                        String videoId =  dataList.get(randomID).getYoutubeVideId();


                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videoId, 0);
                                youTubePlayer.pause();
                                youTubePlayer.addListener(tracker);

                            }
                        });
                    } else {
                        Log.d("dataqq", "Error getting documents: ", task.getException());
                    }
                });

        if (!dataList.isEmpty()){
            String videoId =  dataList.get(randomID).getYoutubeVideId();
            Toast.makeText(getContext(), videoId+"", Toast.LENGTH_SHORT).show();
        }
        nextVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                if (!dataList.isEmpty()){
                    randomID = random.nextInt(dataList.size());
                }
                String videoId1 =  dataList.get(randomID).getYoutubeVideId();

                youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId1,0));

            }
        });

        return view;
    }
}