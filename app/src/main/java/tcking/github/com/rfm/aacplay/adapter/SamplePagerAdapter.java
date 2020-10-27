package tcking.github.com.rfm.aacplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.WebViewActivity;
import tcking.github.com.rfm.aacplay.pojos.SliderApiResponse;

public class SamplePagerAdapter extends PagerAdapter {

    private final Random random = new Random();
  //  private int mSize;
    List<SliderApiResponse> list = new ArrayList<>();
    Context context;

    public SamplePagerAdapter(Context context , List<SliderApiResponse> list ) {
       // mSize = list.size();
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

  /*  public SamplePagerAdapter(int count) {
        mSize = count;
    }*/

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        final SliderApiResponse sliderApiResponse = list.get(position);





        RelativeLayout relativeLayout = new RelativeLayout(view.getContext());


        ImageView imageView = new ImageView(view.getContext());
        // textView.setText(String.valueOf(position + 1));

        RelativeLayout.LayoutParams buttonParam1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        imageView.setLayoutParams(buttonParam1);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(context)
               // .load("https://cms.digitalag.ro//cms_websites//cms_radiozu//library//img//2018//03//morar-mama.jpg")

               .load(sliderApiResponse.getPoza())
                .placeholder(R.drawable.splash)
                .into(imageView);

        relativeLayout.addView(imageView);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context,"position ", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(context, WebViewActivity.class);
                intent1.putExtra("url",sliderApiResponse.getLink());
                context.startActivity(intent1);

            }
        });



        TextView textView = new TextView(view.getContext());
        // textView.setText(String.valueOf(position + 1));

        RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        buttonParam.setMargins(10,0,10,view.getContext().getResources().getDimensionPixelSize(R.dimen._30sdp));

        textView.setLayoutParams(buttonParam);
        textView.setGravity(Gravity.CENTER);


      //  textView.setText(sliderApiResponse.getTitlu());

        textView.setText(Html.fromHtml(sliderApiResponse.getTitlu()));
        //textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));

        textView.setTextColor(Color.WHITE);
        textView.setTextSize(17);
      //  textView.setShadowLayer(1.5f, -1, 1, Color.LTGRAY);

    //    textView.setShadowLayer(2.5f, -2.0f, 2.0f,  Color.parseColor("#FF3D803D"));
        textView.setShadowLayer(2.5f, -2.0f, 2.0f,  Color.parseColor("#000000"));
        relativeLayout.addView(textView);

        view.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return relativeLayout;
    }




  /*  public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }*/

 /*   public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }*/


   /* public void addingData(List<SliderApiResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }*/


}