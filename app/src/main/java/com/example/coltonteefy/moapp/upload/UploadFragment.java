package com.example.coltonteefy.moapp.upload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coltonteefy.moapp.MainActivity;
import com.example.coltonteefy.moapp.R;
import com.example.coltonteefy.moapp.utils.CreateCustomToast;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {
    private final String TAG = "UploadFragment";
    Button chooseFileBtn, uploadBtn;
    TextView filePath, fileChosenText, progress;
    File file;
    CircularProgressBar progressBar;
    Spinner spinner;

    EditText titleInput, descriptionInput;
    String genre;
    ImageView coverArt;
    Boolean dialogOpen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        fileChosenText = view.findViewById(R.id.fileChosenText);
        filePath = view.findViewById(R.id.filePath);
        progressBar = view.findViewById(R.id.progressBar);
        progress = view.findViewById(R.id.progress);


        chooseFileBtn = view.findViewById(R.id.chooseFileBtn);
        chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withRequestCode(1000)
                        .withHiddenFiles(true)
                        .start();
            }

        });

        uploadBtn = view.findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Uploader().execute();
            }
        });

        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(broadcastReceiver, new IntentFilter("file"));

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getActivity(), "No permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            filePath.setText(intent.getStringExtra("filePath"));
            file = new File(filePath.getText().toString());
            chooseFileBtn.setVisibility(View.INVISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            Log.d(TAG, "onReceive: " + filePath.getText() + " File: " + file);

            if (dialogOpen) {
                Bitmap bitmap = BitmapFactory.decodeFile(intent.getStringExtra("filePath"));
                Log.d(TAG, "onReceive: " + bitmap);
                coverArt.setImageBitmap(bitmap);
            }
        }
    };


    class Uploader extends AsyncTask<Void, Integer, Integer> implements AdapterView.OnItemSelectedListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);

            uploadBtn.setVisibility(View.GONE);
            filePath.setVisibility(View.GONE);
            fileChosenText.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            upload();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            final DialogPlus dialog;
            dialog = DialogPlus.newDialog(getContext())
                    .setGravity(Gravity.CENTER)
                    .setContentHolder(new ViewHolder(R.layout.layout_upload_song_details))
                    .setInAnimation(R.anim.slide_in_bottom)
                    .setOutAnimation(R.anim.slide_out_bottom)
                    .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)  // or any custom width ie: 300
                    .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                    .setContentBackgroundResource(R.color.darkPurple)
                    .setMargin(0, 0, 0, 0)
                    .create();

            dialog.show();

            spinner = getActivity().findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.genres, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            coverArt = getActivity().findViewById(R.id.coverArt);

            Button uploadCoverArtBtn = getActivity().findViewById(R.id.uploadCoverArtBtn);
            uploadCoverArtBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogOpen = true;
                    new MaterialFilePicker()
                            .withActivity(getActivity())
                            .withRequestCode(1000)
                            .withHiddenFiles(true)
                            .start();
                }
            });

            titleInput = getActivity().findViewById(R.id.titleInput);
            descriptionInput = getActivity().findViewById(R.id.descriptionInput);

            Button finishUploadBtn = getActivity().findViewById(R.id.finishUploadBtn);
            finishUploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishUpload(titleInput.getText().toString(), genre, descriptionInput.getText().toString());
                    uploadBtn.setVisibility(View.GONE);
                    chooseFileBtn.setVisibility(View.VISIBLE);
                    filePath.setVisibility(View.VISIBLE);
                    fileChosenText.setVisibility(View.VISIBLE);
                    filePath.setText(R.string.no_file_chosen);
                    file = new File("");
                    progressBar.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);

                    dialog.dismiss();
                }
            });
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(20);

            genre = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void upload() {
            String username = "see";
            OkHttpClient client = new OkHttpClient();

            String url = "https://afternoon-waters-54974.herokuapp.com/uploadMusic/" + username;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("song", file.getName(),
                            RequestBody.create(MediaType.parse("audio/mp3"), file))
                    .build();

            body = ProgressHelper.withProgress(body, new ProgressUIListener() {
                @Override
                public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                    String value = String.valueOf((int) (100 * percent));
                    int intValue = (int) (100 * percent);
                    progress.setText(value.concat("%"));
                    progressBar.setProgress(intValue);
                }
            });


            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                Log.i(TAG, "SUCCESS Response message: " + response.body().string());

            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "FAIL Response message: " + e.toString());
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (final Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        }


        public void finishUpload(String titleInput, String genre, String descriptionInput) {
            String username = "see";
            OkHttpClient client = new OkHttpClient();

            String url = "https://afternoon-waters-54974.herokuapp.com/updateMusicDetailsImmediately/" + username;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", titleInput)
                    .addFormDataPart("genre", genre)
                    .addFormDataPart("description", descriptionInput)
                    .addFormDataPart("coverArtUrl", file.getName(),
                            RequestBody.create(MediaType.parse("image/jpeg"), file))
                    .build();

            body = ProgressHelper.withProgress(body, new ProgressUIListener() {
                @Override
                public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                    String value = String.valueOf((int) (100 * percent));
                    int intValue = (int) (100 * percent);
                }
            });


            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                Log.i(TAG, "SUCCESS Response message: " + response.body().string());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (final Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }
}
