package com.wander.life.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wander.life.R;
import com.wander.life.bean.LetterItem;
import com.wander.life.ui.adapter.EditAdapter;
import com.wander.life.ui.adapter.cell.EditCell;
import com.wander.life.ui.widget.EditRecyclerView;
import com.wander.life.widget.recycler.Cell;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {

    private EditRecyclerView mRecyclerView;
    private EditAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initRecycler();
    }

    private void initRecycler() {
        mRecyclerView = (EditRecyclerView) findViewById(R.id.editor_recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EditAdapter();
        ArrayList<Cell> mData = new ArrayList<>();
        mData.add(new EditCell(new LetterItem()));
        mAdapter.setData(mData);
        mRecyclerView.setAdapter(mAdapter);
    }


}
