package com.wander.life.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wander.base.RxUtils.rxbus.EventThread;
import com.wander.base.RxUtils.rxbus.RxBus;
import com.wander.base.RxUtils.rxbus.Subscribe;
import com.wander.life.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseActivity extends AppCompatActivity {

    private TextView choose;
    private Object sum;

    @Override
    protected void onStart() {
        super.onStart();
        RxBus.getInstance().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        choose = (TextView) findViewById(R.id.text_choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getInstance().post(100, "go change text");

            }
        });

        getsum(20);
    }

    @Subscribe(tag = 100, thread = EventThread.MAIN_THREAD)
    private void changeText(String s) {
        choose.setText(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }


    List<Integer> list = new ArrayList();

    public void getsum(int sum) {
        int index = 0;
        for (int i : list) {
            int temp = sum - i;
            int index2 = list.indexOf(temp);
            if (index2 >0){
                break;
            }
            index++;
        }
    }
}
