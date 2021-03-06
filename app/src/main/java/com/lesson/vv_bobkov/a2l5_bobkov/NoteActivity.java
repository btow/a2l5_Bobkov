package com.lesson.vv_bobkov.a2l5_bobkov;

import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.acetNotesTitle)
    AppCompatEditText acetNotesTitle;
    @BindView(R.id.acetNotesAddress)
    AppCompatEditText acetNotesAddress;
    @BindView(R.id.acetNotesText)
    AppCompatEditText acetNotesText;
    @BindView(R.id.tvTitleOfNote)
    TextView tvTitleOfNote;
    @BindView(R.id.tvAddressOfNote)
    TextView tvAddressOfNote;
    @BindView(R.id.tvNotesText)
    TextView tvNotesText;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    private App mApp = App.getmApp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (
                App.START_FROM &&
                        widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_note);

        ButterKnife.bind(this);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (App.NOTES_MODE) {   //OPEN
            acetNotesTitle.setVisibility(View.GONE);
            acetNotesAddress.setVisibility(View.GONE);
            acetNotesText.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);

            for (Map.Entry<Integer, NoteWithTitle> currentSelectedItem :
                    mApp.getmSelectedItems().entrySet()) {

                tvTitleOfNote.setVisibility(View.VISIBLE);
                tvTitleOfNote.setText(currentSelectedItem.getValue().getmTitle());

                if (!currentSelectedItem.getValue().getmAddress().equals("")) {

                    tvAddressOfNote.setVisibility(View.VISIBLE);
                    tvAddressOfNote.setText(currentSelectedItem.getValue().getmAddress());
                }
                tvNotesText.setVisibility(View.VISIBLE);
                tvNotesText.setText(currentSelectedItem.getValue().getmNote());
            }
        } else {    //EDIT

            tvTitleOfNote.setVisibility(View.GONE);
            tvAddressOfNote.setVisibility(View.GONE);
            tvNotesText.setVisibility(View.GONE);

            acetNotesTitle.setVisibility(View.VISIBLE);
            acetNotesAddress.setVisibility(View.VISIBLE);
            acetNotesText.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);

            if (!mApp.selectedItemsIsEmpty()) {
                for (Map.Entry<Integer, NoteWithTitle> selectedItem :
                        mApp.getmSelectedItems().entrySet()) {

                    acetNotesTitle.setText(selectedItem.getValue().getmTitle());
                    acetNotesAddress.setText(selectedItem.getValue().getmAddress());
                    acetNotesText.setText(selectedItem.getValue().getmNote());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);

        switch (view.getId()) {

            case R.id.btnOk:

                if (mApp.selectedItemsIsEmpty()) {
                    NoteWithTitle newNoteWithTitle =
                            new NoteWithTitle(
                                    acetNotesTitle.getText().toString(),
                                    acetNotesAddress.getText().toString(),
                                    acetNotesText.getText().toString()
                            );
                    long id = -1;
                    try {
                        id = NotesTable.addRecord(
                                mApp.getmDBController().getmSqLiteDatabase(),
                                newNoteWithTitle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    newNoteWithTitle.setmId(id);
                    NoteAppWidgetProvider.updateAppWidget(
                            this, AppWidgetManager.getInstance(this), widgetID);
                    mApp.addNewNoteToNoteWithTitleList(newNoteWithTitle);
                } else {

                    for (Map.Entry<Integer, NoteWithTitle> currentSelectedItem :
                            mApp.getmSelectedItems().entrySet()) {
                        currentSelectedItem.getValue().setmTitle(acetNotesTitle.getText().toString());
                        currentSelectedItem.getValue().setmAddress(acetNotesAddress.getText().toString());
                        currentSelectedItem.getValue().setmNote(acetNotesText.getText().toString());
                        try {
                            NotesTable.updateRecord(mApp.getmDBController().getmSqLiteDatabase(),
                                    currentSelectedItem.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            case R.id.btnCancel:
                startActivity(intent);
                break;
        }
    }

    public void onClickMap(View view) {
        // Define string for URI scheme
        String geo = "geo:0,0?z=20&q=" + tvAddressOfNote.getText().toString();
        // Create intent object
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
        // You could specify package for use the GoogleMaps app, only
        //intent.setPackage("com.google.android.apps.maps");
        // Start maps activity
        try {
            this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            mApp.createAttentionDialog(this, e.getMessage(), App.WHITOUT_BUTTON).show();
        }
    }
}
