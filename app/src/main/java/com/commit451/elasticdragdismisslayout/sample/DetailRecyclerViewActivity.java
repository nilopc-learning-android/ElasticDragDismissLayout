package com.commit451.elasticdragdismisslayout.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commit451.elasticdragdismisslayout.ElasticDragDismissLinearLayout;
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Testing using a RecyclerView
 */
public class DetailRecyclerViewActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, DetailRecyclerViewActivity.class);
        return intent;
    }

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.draggable_frame)
    ElasticDragDismissLinearLayout mDragLayout;
    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recyclerview);
        ButterKnife.bind(this);
        mToolbar.setTitle("Countries");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CountriesAdapter adapter = new CountriesAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDragLayout.addListener(new ElasticDragDismissListener() {
            @Override
            public void onDrag(float elasticOffset, float elasticOffsetPixels, float rawOffset, float rawOffsetPixels) {
            }

            @Override
            public void onDragDismissed() {
                // if we drag dismiss downward then the default reversal of the enter
                // transition would slide content upward which looks weird. So reverse it.
                if (mDragLayout.getTranslationY() > 0) {
                    //TODO
                    //                            getWindow().setReturnTransition(
                    //                                    TransitionInflater.from(AboutActivity.this)
                    //                                            .inflateTransition(R.transition.about_return_downward));
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            mDragLayout.addListener(new SystemChromeFader(getWindow()));
        }
    }

    static class CountriesAdapter extends RecyclerView.Adapter<CountriesViewHolder> {

        String[] mCountries;
        public CountriesAdapter(Context context) {
            mCountries = context.getResources().getStringArray(R.array.countries);

        }

        @Override
        public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return CountriesViewHolder.inflate(parent);
        }

        @Override
        public void onBindViewHolder(CountriesViewHolder holder, int position) {
            holder.bind(mCountries[position]);
        }

        @Override
        public int getItemCount() {
            return mCountries.length;
        }
    }

    static class CountriesViewHolder extends RecyclerView.ViewHolder {

        public static CountriesViewHolder inflate(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_game, parent, false);
            return new CountriesViewHolder(view);
        }

        @Bind(R.id.text) TextView mText;

        public CountriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(String country) {
            mText.setText(country);
        }
    }
}
