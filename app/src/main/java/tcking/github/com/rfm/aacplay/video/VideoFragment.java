package tcking.github.com.rfm.aacplay.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tcking.viewquery.ViewQuery;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by TangChao on 2017/6/15.
 */

public class VideoFragment extends Fragment {
    private ViewQuery $;
    private int aspectRatio = VideoInfo.AR_ASPECT_FIT_PARENT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set global configuration: turn on multiple_requests
        PlayerManager.getInstance().getDefaultVideoInfo().addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "multiple_requests", 1L));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        $ = new ViewQuery(view);

        String testUrl = "https://play.myovn.com/s1/cache/radiozu/playlist.m3u8"; //test live stream;

        final VideoView videoView = $.id(R.id.video_view).view();
        videoView.setVideoPath(testUrl);

        videoView.getPlayer().start();





                /*if (v.getId() == R.id.btn_play) {
                    if (videoView.getPlayer().isPlaying()) {
                        videoView.getPlayer().pause();
                    } else {
                        videoView.getPlayer().start();
                    }
                }*/


    }


}
