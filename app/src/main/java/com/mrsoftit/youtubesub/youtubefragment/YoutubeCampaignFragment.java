package com.mrsoftit.youtubesub.youtubefragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.mrsoftit.youtubesub.R;
import com.mrsoftit.youtubesub.activity.YoutubeVideInfoActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

public class YoutubeCampaignFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    Context context;

    public YoutubeCampaignFragment(Context context) {
        this.context = context;
    }

    public YoutubeCampaignFragment() {
        // Required empty public constructor
    }
    public static YoutubeCampaignFragment newInstance(String param1, String param2) {
        YoutubeCampaignFragment fragment = new YoutubeCampaignFragment();
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

    EditText youtube_add_edite_text;
    Button youtube_link_add_text;
    TextView textLinkview;
    String videoImageURL;
    String videoNameID;
    String videoID;
    String videoName;
    String channelImageUrl;
    String channelName;
    String channelID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaign, container, false);

        youtube_add_edite_text = view.findViewById(R.id.youtube_add_edite_text);
        youtube_link_add_text = view.findViewById(R.id.youtube_link_add_text);
        textLinkview = view.findViewById(R.id.textLinkview);



// To dismiss the dialog


        youtube_link_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = youtube_add_edite_text.getText().toString();

                if (url.isEmpty()){
                    Toast.makeText(getContext(), "Enter Url", Toast.LENGTH_SHORT).show();
                    return;
                }
                videoImageNameId videoImageNameId = new videoImageNameId(url);

                try {
                    String fulUrl = videoImageNameId.execute().get();


                    if (fulUrl == null){


                    }else {
                        String[] parts = fulUrl.split("#");


                        if (parts.length  <4){
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                            Log.d("sdf", "onClick: ");
                            return;
                        }else {
                            videoImageURL = parts[0];
                            videoNameID = parts[1];
                            videoID = parts[2];
                            channelID = parts[3];
                        }

                        Toast.makeText(context, "out", Toast.LENGTH_SHORT).show();

                        ChannelIconName channelIconName = new ChannelIconName(channelID);
                        String currentId =  channelIconName.execute().get();

                        String[] channelNameIcon = currentId.split("#");
                        String channelImageUrl1 = channelNameIcon[0];
                        channelImageUrl = channelImageUrl1;

                        String name = channelNameIcon[1];
                        channelName = name;

                        Intent intent = new Intent(context, YoutubeVideInfoActivity.class);
                        intent.putExtra("videoID",videoID);
                        intent.putExtra("videoName",videoNameID);
                        intent.putExtra("videoImageUrl",videoImageURL);
                        intent.putExtra("channelName",channelName);
                        intent.putExtra("chammelLogo",channelImageUrl);
                        intent.putExtra("channelID",channelID);
                        intent.putExtra("videurl",url);
                        v.getContext().startActivity(intent);

                    }



                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });




        return view;
    }


    private class videoImageNameId extends AsyncTask<Void, String, String> {
        String youtubeLink;

        public videoImageNameId(String youtubeLink) {
            this.youtubeLink = youtubeLink;
        }


        @Override
        protected String doInBackground(Void... voids) {
            String Videoname = null;
            String videoimageUrl = null;
            String channelid = null;
            String videID = null;

            try {
                Document doc  = Jsoup.connect(youtubeLink)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                        .get();


                Elements metaOgTitle = doc.select("meta[property=og:title]");
                if (metaOgTitle!=null) {
                    Videoname = metaOgTitle.attr("content");
                    Log.d("Videoname", "doInBackground: "+Videoname);
                }
                Elements metaOgImage = doc.select("meta[property=og:image]");

                if (metaOgImage!=null) {
                    videoimageUrl = metaOgImage.attr("content");
                    Log.d("Videoname", "doInBackground: "+videoimageUrl);

                }
                Elements meImage = doc.select("meta[itemprop=channelId]");
                if (meImage!=null){
                    channelid = meImage.attr("content");
                    Log.d("Videoname", "doInBackground: "+channelid);
                }
                Elements videID1 = doc.select("meta[itemprop=videoId]");
                if (meImage!=null){
                    videID = videID1.attr("content");
                    Log.d("Videoname", "doInBackground: "+videID);
                }
                Log.d("Videoname", videoimageUrl+"\n"+Videoname+"\n"+videID+"\n"+channelid);

                return videoimageUrl+"#"+Videoname+"#"+videID+"#"+channelid ;


            } catch (Exception e) {
                Log.d("youu", "doInBackground: "+e.getMessage());
                return e.getMessage();
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