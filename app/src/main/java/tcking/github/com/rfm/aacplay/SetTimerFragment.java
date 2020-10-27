package tcking.github.com.rfm.aacplay;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 4/10/2018.
 */

public class SetTimerFragment extends Fragment implements WheelPicker.OnItemSelectedListener {

    String hours = "00", minutes = "00";
    List<String> hour_values = new ArrayList<>();
    List<String> minute_values = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_set_timer, container, false);
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        for (int i = 0; i < 24; i++) {
            if (i < 10)
                hour_values.add("0" + i);
            else
                hour_values.add("" + i);
        }

       TextView txt_set_timer = (TextView)view.findViewById(R.id.txt_set_timer);

        txt_set_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   Intent intent = new Intent(getActivity(), SetTimer.class);
                startActivity(intent);*/

             if(hours.equalsIgnoreCase("00")&& minutes.equalsIgnoreCase("00")){

                 Toast.makeText(getActivity(),"Please select valid time",Toast.LENGTH_SHORT).show();
             }else{
                 mListener.onTimeSetChangeListener(hours,minutes);
             }


            }
        });


        WheelPicker hour_picker = (WheelPicker)view.findViewById(R.id.hour_picker);
        hour_picker.setOnItemSelectedListener(this);


        hour_picker.setData(hour_values);
        hour_picker.setItemTextColor(getResources().getColor( R.color.line_clor));
        hour_picker.setIndicatorColor(getResources().getColor( R.color.black));
        hour_picker.setItemTextSize((int) getResources().getDimension(R.dimen._34sdp));
        hour_picker.setVisibleItemCount(3);
        hour_picker.setAtmospheric(true);
        hour_picker.setSelectedItemTextColor(getResources().getColor( R.color.black));


        for (int i = 0; i < 60; i++) {
            if (i < 10)
                minute_values.add("0" + i);
            else
                minute_values.add("" + i);
        }



        WheelPicker minute_picker = (WheelPicker)view.findViewById(R.id.minute_picker);
        minute_picker.setOnItemSelectedListener(this);
        minute_picker.setData(minute_values);
        minute_picker.setItemTextColor(getResources().getColor( R.color.line_clor));
        minute_picker.setIndicatorColor(getResources().getColor( R.color.black));
        minute_picker.setItemTextSize((int) getResources().getDimension(R.dimen._34sdp));
        minute_picker.setVisibleItemCount(3);
        minute_picker.setAtmospheric(true);
        minute_picker.setSelectedItemTextColor(getResources().getColor( R.color.black));

    }



    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {

        switch (picker.getId()) {
            case R.id.hour_picker:
                Log.d("value", "value " + String.valueOf(data));
                hours = String.valueOf(data);

                break;

            case R.id.minute_picker:
                Log.d("value", "value " + String.valueOf(data));
                minutes = String.valueOf(data);
                break;

        }
    }





    public interface OnTimeSetChangeListener {
        public void onTimeSetChangeListener(String hours, String minutes);


    }

    private OnTimeSetChangeListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnTimeSetChangeListener) {
            mListener = (OnTimeSetChangeListener) activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnSearchListener interface");
        }
    }

}
