package com.wander.life.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wander.life.R;
import com.wander.life.ui.listeners.OnFragmentMainListener;

/**
 * 消息界面
 */
public class ChatFragment extends Fragment {

    private OnFragmentMainListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMainListener) {
            mListener = (OnFragmentMainListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentMainListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
