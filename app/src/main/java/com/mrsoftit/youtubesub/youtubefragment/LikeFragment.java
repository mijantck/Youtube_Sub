package com.mrsoftit.youtubesub.youtubefragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mrsoftit.youtubesub.R;
import com.mrsoftit.youtubesub.activity.OverlyActivity;
import com.mrsoftit.youtubesub.modle.YoutubeSetData;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LikeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public LikeFragment() {
        // Required empty public constructor
    }

    public static LikeFragment newInstance(String param1, String param2) {
        LikeFragment fragment = new LikeFragment();
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

    ImageView imageView1;
    TextView textView;
    String imageUlLike;
    String textLike;
    String part1;
    String part2;
    int randomID1;


    List<YoutubeSetData> dataList = new ArrayList<>();

    //Database
    FirebaseFirestore db;
    CollectionReference youtubeInfoAdd;
    DocumentReference docRef;
    int randomID;
    String videoId;
    String videoName;
    String videoImageUrl;
    String VideUrl;


    //Button
    Button nextVideoView;
    Button youtubeLikeId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        imageView1 = view.findViewById(R.id.likeviewImage);
        textView = view.findViewById(R.id.likeviewName);
        db = FirebaseFirestore.getInstance();
        youtubeInfoAdd = db.collection("youtube").document("like").collection("videoInfo");
        nextVideoView = view.findViewById(R.id.nextLikeYoutube);
        youtubeLikeId = view.findViewById(R.id.youtubeLikeId);

        youtubeInfoAdd.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String videoName = document.get("youtubeVideoName").toString();
                    String videoUrl = document.get("youtubeVideUrl").toString();
                    String videoImageUrl = document.get("youtubeImageUrl").toString();
                    dataList.add(new YoutubeSetData(videoName,videoUrl,videoImageUrl));
                }
                Random random = new Random();
                if (!dataList.isEmpty()){
                    randomID = random.nextInt(dataList.size());
                    randomID1 = randomID;
                    String videoImageUrl =  dataList.get(randomID).getYoutubeImageUrl();
                    String videoName =  dataList.get(randomID).getYoutubeVideoName();
                    String videoUrl1 =  dataList.get(randomID).getYoutubeVideUrl();
                    Picasso.get()
                            .load(videoImageUrl)
                            .into(imageView1);

                    textView.setText(videoName);
                    VideUrl=videoUrl1;
                }



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
                    if (randomID1 == randomID){
                        randomID = random.nextInt(dataList.size());
                        randomID1 =randomID;
                        String videoImageUrl =  dataList.get(randomID).getYoutubeImageUrl();
                        String videoName =  dataList.get(randomID).getYoutubeVideoName();
                        String videoUrl1 =  dataList.get(randomID).getYoutubeVideUrl();
                        Picasso.get()
                                .load(videoImageUrl)
                                .into(imageView1);

                        textView.setText(videoName);
                        VideUrl=videoUrl1;
                        Toast.makeText(getContext(), randomID1+"\n"+randomID+"  if", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), randomID1+"\n"+randomID+"  els", Toast.LENGTH_SHORT).show();
                        String videoImageUrl =  dataList.get(randomID).getYoutubeImageUrl();
                        String videoName =  dataList.get(randomID).getYoutubeVideoName();
                        String videoUrl1 =  dataList.get(randomID).getYoutubeVideUrl();
                        Picasso.get()
                                .load(videoImageUrl)
                                .into(imageView1);

                        textView.setText(videoName);
                        VideUrl=videoUrl1;
                    }

                }





            }
        });

        youtubeLikeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OverlyActivity.class);
                intent.putExtra("url",VideUrl);
                intent.putExtra("count","40");
                startActivity(intent);
            }
        });
        return view;
    }


    private class SubcribeIconName extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = null;
            String name = null;
            String imageUrl = null;
            String id = null;
            try {
                Document doc  = Jsoup.connect("https://www.youtube.com/watch?v=w1SHUGE4jmk")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                        .get();


                Elements metaOgTitle = doc.select("meta[property=og:title]");
                if (metaOgTitle!=null) {
                    name = metaOgTitle.attr("content");
                }
                Elements metaOgImage = doc.select("meta[property=og:image]");

                if (metaOgImage!=null) {
                    imageUrl = metaOgImage.attr("content");

                }
                Elements meImage = doc.select("meta[itemprop=channelId]");
                if (meImage!=null){
                    id = meImage.attr("content");
                }

                Log.d("fafsfa", "doInBackground: "+id);

                return imageUrl+"#"+name+";"+id ;
            } catch (Exception e) {
                return newVersion;
            }
        }
    }

    private class ChannelIconName extends AsyncTask<Void, String, String> {

        String id;

        public ChannelIconName(String id) {
            this.id = id;

        }

        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = null;
            String name = null;
            String imageUrlaaa = null;

            try {
                Document doc  = Jsoup.connect("https://www.youtube.com/channel/"+id)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                        .get();


                Elements metaOgTitle = doc.select("meta[property=og:title]");
                if (metaOgTitle!=null) {
                    name = metaOgTitle.attr("content");
                }

                Elements metaOgImage = doc.select("meta[property=og:image]");

                Elements meImage = doc.select("meta[itemprop=channelId]");
                if (meImage!=null) {
                    imageUrlaaa = metaOgImage.attr("content");
                    Log.d("jsdfhjsdh", "doInBackground: "+imageUrlaaa);

                }
                return imageUrlaaa+"#"+name;
            } catch (Exception e) {
                return newVersion;
            }
        }
    }

}