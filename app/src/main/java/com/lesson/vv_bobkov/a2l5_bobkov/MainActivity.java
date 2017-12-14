package com.lesson.vv_bobkov.a2l5_bobkov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvNotesNo)
    TextView tvNotesNo;
    @BindView(R.id.lvNotes)
    ListView lvNotes;

    private App mApp;
    private NoteWithTitleAdapter mNoteWithTitleAdapter;
    private AbsListView.MultiChoiceModeListener mMultiChoiceModeListener
            = new AbsListView.MultiChoiceModeListener() {

        private Menu actionModeMenu;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionModeMenu = menu;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mApp.prepareMenu(menu);
            return true;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            if (mApp.mNoteWithTitleListIsEmpty()) {
                return;
            }
            if (checked) {
                mApp.addSelectedItem(
                        position, mApp.getmNoteWithTitleList().get(position)
                );
            } else {
                mApp.getmSelectedItems().remove(position);
            }
            mApp.prepareMenu(actionModeMenu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return onClickMenuItem(item, mApp);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            supportInvalidateOptionsMenu();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializationView();
    }

    private void initializationView() {
        mApp = App.getmApp();

        if (mApp.selectedItemsIsEmpty()) mApp.createmSelectedItems();
        ButterKnife.bind(this);

        mNoteWithTitleAdapter = new NoteWithTitleAdapter(getApplicationContext(), mApp);
        lvNotes.setAdapter(mNoteWithTitleAdapter);
        lvNotes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvNotes.setMultiChoiceModeListener(mMultiChoiceModeListener);
        mApp.setmLvNotes(lvNotes);
        mApp.setmTvNotesNo(tvNotesNo);
        mApp.setVisibilityViewOnMainActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mApp.setmMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mApp.prepareMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return onClickMenuItem(item, mApp);
    }

    private boolean onClickMenuItem(MenuItem item, App app) {

        Intent intent = new Intent(this, NoteActivity.class);

        switch (item.getItemId()) {

            case R.id.actionRem:

                if (!app.selectedItemsIsEmpty()) {

                    for (Iterator<HashMap.Entry<Integer, NoteWithTitle>> iterator =
                         app.getmSelectedItems().entrySet().iterator();
                         iterator.hasNext();) {

                        HashMap.Entry<Integer, NoteWithTitle> selectNote = iterator.next();

                        mNoteWithTitleAdapter.remNoteFromNoteWithTitleList(selectNote.getKey());
                        iterator.remove();
                        invalidateOptionsMenu();
                    }
                    return true;
                }
                return false;

            case R.id.actionOpen:

                if (!app.selectedItemsIsEmpty()) {

                    app.NOTES_MODE = app.NOTES_MODE_OPEN;
                    startActivity(intent);
                    return true;
                }
                return false;

            case R.id.actionEdit:

                if (!app.selectedItemsIsEmpty()) {

                    app.NOTES_MODE = app.NOTES_MODE_EDIT;
                    startActivity(intent);
                    return true;
                }
        }
        return false;
    }

    public void onClickAdd(View view) {

        Intent intent = new Intent(this, NoteActivity.class);
        App.NOTES_MODE = App.NOTES_MODE_EDIT;

        if (mApp.selectedItemsIsEmpty()) {
            mApp.createmSelectedItems();
        } else {
            mApp.getmSelectedItems().clear();
        }
        startActivity(intent);
    }
}
