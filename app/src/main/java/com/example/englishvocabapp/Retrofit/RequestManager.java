package com.example.englishvocabapp.Retrofit;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.englishvocabapp.Entities.Dictionary.DictionaryAPIResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestManager {
    public static final String PARAM_WORD = "word";
    private Context context;
    private Retrofit retrofit;
    private AlertError alertError;

    public RequestManager(Context context, String baseUrl, AlertError alertError) {
        this.context = context;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.alertError = alertError;
    }

    public void getWordMeanings(OnFetchDataListener listener, String word) {
        CallDictionary callDictionary = retrofit.create(CallDictionary.class);
        Call<List<DictionaryAPIResponse>> call = callDictionary.callMeanings(word);
        try {
            call.enqueue(new Callback<List<DictionaryAPIResponse>>() {
                @Override
                public void onResponse(Call<List<DictionaryAPIResponse>> call, Response<List<DictionaryAPIResponse>> response) {
                    if (!response.isSuccessful()) {
                        //Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                        alertError.showAlertError();
                        return;
                    }
                    listener.onFetchDictionaryData(response.body().get(0), response.message());
                }

                @Override
                public void onFailure(Call<List<DictionaryAPIResponse>> call, Throwable t) {
                    listener.onError("Request Failed!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An Error Occurred!", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CallDictionary {
        @GET("entries/en/{" + PARAM_WORD + "}")
        Call<List<DictionaryAPIResponse>> callMeanings(
                @Path(PARAM_WORD) String word
        );
    }

    public interface AlertError {
        void showAlertError();
    };
}
