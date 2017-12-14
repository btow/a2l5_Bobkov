package com.lesson.vv_bobkov.a2l5_bobkov;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lesson.vv_bobkov.a2l5_bobkov.R.color.colorLightGray;
import static com.lesson.vv_bobkov.a2l5_bobkov.R.color.colorWithe;

/**
 * Created by samsung on 28.11.2017.
 */

class NoteWithTitleAdapter extends BaseAdapter {

    private Context mCxt;
    private App mApp;
    private LayoutInflater layoutInflater;

    @BindView(R.id.tvNotesTitle)
    TextView tvNotesTitle;
    @BindView(R.id.llRowse)
    LinearLayout llRowse;

    NoteWithTitleAdapter(Context cxt, App app) {

        mCxt = cxt;
        mApp = app;
        layoutInflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            app.getmDBController().readeNoteWithTitleArrayListFromBd();
        } catch (Exception e) {
            app.setmNoteWithTitleList(new ArrayList<NoteWithTitle>());
        }

    }

    @Override
    public int getCount() {
        return mApp.getmNoteWithTitleList().size();
    }

    @Override
    public Object getItem(int i) {
        return mApp.getmNoteWithTitleList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view_item, viewGroup, false);
        }

        ButterKnife.bind(this, view);

        if (mApp.getmSelectedItems().containsKey(i)) {
            llRowse.setBackgroundColor(mCxt.getResources().getColor(colorLightGray));
            tvNotesTitle.setBackgroundColor(mCxt.getResources().getColor(colorLightGray));
        } else {
            llRowse.setBackgroundColor(mCxt.getResources().getColor(colorWithe));
            tvNotesTitle.setBackgroundColor(mCxt.getResources().getColor(colorWithe));
        }
        tvNotesTitle.setText(mApp.getmNoteWithTitleList().get(i).getmTitle());
        tvNotesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mApp.getmSelectedItems().containsKey(i)) {
                    llRowse.setBackgroundColor(mCxt.getResources().getColor(colorWithe));
                    v.setBackgroundColor(mCxt.getResources().getColor(colorWithe));
                    mApp.getmSelectedItems().remove(i);
                } else {
                    llRowse.setBackgroundColor(mCxt.getResources().getColor(colorLightGray));
                    v.setBackgroundColor(mCxt.getResources().getColor(colorLightGray));
                    mApp.addSelectedItem(
                            i, mApp.getmNoteWithTitleList().get(i)
                    );
                }
                notifyDataSetChanged();
                mApp.prepareMenu(mApp.getmMenu());
            }
        });
        return view;
    }

    public void remNoteFromNoteWithTitleList(int position) {
        long remId = mApp.getmNoteWithTitleList().get(position).getmId();
        mApp.remNoteFromNoteWithTitleList(position);

        do {
            try {
                NotesTable.deleteRecord(
                        mApp.getmDBController().getmSqLiteDatabase(),
                        new long[]{remId});
            } catch (Exception e) {
                e.printStackTrace();
                mApp.createAttentionDialog((MainActivity) mCxt, e.getMessage(), App.WITH_TO_RETRY).show();
            }
        } while (mApp.isResultAttentionDialog());

        if (mApp.getmNoteWithTitleList().size() == 0) {
            mApp.setVisibilityViewOnMainActivity();
        }
        notifyDataSetChanged();
    }
}
