package com.demo.imagetransdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.demo.imagetransdemo.adapter.CustomTransform;
import com.demo.imagetransdemo.MyApplication;
import com.demo.imagetransdemo.adapter.MyImageLoad;
import com.demo.imagetransdemo.adapter.MyImageTransAdapter;
import com.demo.imagetransdemo.R;
import com.demo.imagetransdemo.view.DragParentView;

import java.util.ArrayList;
import java.util.List;

import it.liuting.imagetrans.ImageTrans;
import it.liuting.imagetrans.ScaleType;
import it.liuting.imagetrans.listener.SourceImageViewParam;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public static final int DEFAULT_MIN_SIZE = MyApplication.dpToPx(100);
    Toolbar toolbar;
    DragParentView dragParentView;
    ImageView imageView;
    AppCompatSeekBar widthSeekBar;
    AppCompatSeekBar heightSeekBar;
    int maxSize = MyApplication.getScreenWidth();
    ScaleType scaleType = ScaleType.CENTER_CROP;
    String drawableId = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496996959&di=13c094ba73675a24df2ad1d2c730c02c&imgtype=jpg&er=1&src=http%3A%2F%2Fdasouji.com%2Fwp-content%2Fuploads%2F2015%2F07%2F%25E9%2595%25BF%25E8%258A%25B1%25E5%259B%25BE-6.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dragParentView = (DragParentView) findViewById(R.id.drag_parent_view);
        widthSeekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar_width);
        heightSeekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar_height);
        widthSeekBar.setOnSeekBarChangeListener(this);
        heightSeekBar.setOnSeekBarChangeListener(this);
        widthSeekBar.setMax(maxSize);
        heightSeekBar.setMax(maxSize);
        imageView = new ImageView(this);
        dragParentView.getLayoutParams().height = MyApplication.getScreenWidth();
        dragParentView.setLayoutParams(dragParentView.getLayoutParams());
        dragParentView.setView(imageView, DEFAULT_MIN_SIZE, DEFAULT_MIN_SIZE);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final List<String> images = new ArrayList<>();
        images.add(drawableId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTrans.with(MainActivity.this)
                        .setImageList(images)
                        .setSourceImageViewParam(new SourceImageViewParam() {
                            @Override
                            public View getSourceView(int position) {
                                return imageView;
                            }

                            @Override
                            public ScaleType getScaleType(int position) {
                                return scaleType;
                            }
                        })
                        .setImageLoad(new MyImageLoad())
                        .show();
            }
        });
        loadImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_local_album: {
                Intent intent = new Intent(MainActivity.this, PhotoAlbumActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_net_album: {
                Intent intent = new Intent(MainActivity.this, NetImageActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_time_line: {
                Intent intent = new Intent(MainActivity.this, TimeLineActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_bar_width: {
                dragParentView.requestChildWidth(seekBar.getProgress());
                break;
            }
            case R.id.seek_bar_height: {
                dragParentView.requestChildHeight(seekBar.getProgress());
                break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seekBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imageView != null)
                    loadImage();
            }
        }, 300);
    }

    public void clickToScaleType(View view) {
        switch (view.getId()) {
            case R.id.center_crop:
                scaleType = ScaleType.CENTER_CROP;
                break;
            case R.id.top_crop:
                scaleType = ScaleType.START_CROP;
                break;
            case R.id.bottom_crop:
                scaleType = ScaleType.END_CROP;
                break;
            case R.id.fix_xy:
                scaleType = ScaleType.FIT_XY;
                break;
        }
        loadImage();
    }

    private void loadImage() {
        Glide.with(this).load(drawableId)
                .transform(new CustomTransform(this, scaleType))
                .into(imageView);
    }

}
