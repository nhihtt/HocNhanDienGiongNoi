package donguyennhathan.com.hocnhandiengiongnoi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import donguyennhathan.com.hocnhandiengiongnoi.databinding.ActivityGoogleRecognitionWithoutDialogBinding;
import donguyennhathan.com.model.VNCharacterUtils;

public class GoogleRecognitionWithoutDialogActivity extends AppCompatActivity
implements RecognitionListener
{

    ActivityGoogleRecognitionWithoutDialogBinding binding;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleRecognitionWithoutDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//------------------------------------------
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"vi_VN");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi_VN");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
//        -----------------------------------
        addEvents();
    }

    private void addEvents() {
        binding.btnNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnNoi.setText("Tui đang lắng nghe");
                speech.startListening(recognizerIntent);
                speech.stopListening();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";

        binding.txtKetQua.setText(text);
        binding.btnNoi.setText("Nhấn vào đây để nói tiếng Việt");
        binding.btnNoi.setBackgroundColor(Color.BLUE);
        String[] s = {VNCharacterUtils.removeAccent(
                binding.txtKetQua.getText().toString().replaceAll(" ","").toLowerCase())};
        lenhTatPhanMem(s);
        lenhDoiMau(s);

    }

    private void lenhDoiMau(String[] s) {
        String str="";
        String[] colorTV = {"xanh","do","vang","den","trang"};
        String[] colorTA = {"BLUE","RED","YELLOW","BLACK","WHITE"};
        for(int i =0; i<s.length;i++)
        {
            str+=s[i]+"\n";
            if(str.contains("doimau"))
            {
                try {
                    for(int j=0; j<colorTV.length;j++)
                    {
                        if(str.contains("mau"+colorTV[j]))
                        {
                            switch (colorTA[j])
                            {
                                case "BLUE":
                                    binding.txtColor.setBackgroundColor(Color.BLUE);
                                    break;
                                case "RED":
                                    binding.txtColor.setBackgroundColor(Color.RED);
                                    break;
                                case "YELLOW":
                                    binding.txtColor.setBackgroundColor(Color.YELLOW);
                                    break;
                                case "BLACK":
                                    binding.txtColor.setBackgroundColor(Color.BLACK);
                                    break;
                                case "WHITE":
                                    binding.txtColor.setBackgroundColor(Color.WHITE);
                                    break;
                                default:
                                    Toast.makeText(GoogleRecognitionWithoutDialogActivity.this, "Can't find color",
                                            Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void lenhTatPhanMem(String[] s)
    {
        String str="";
        for(int i =0; i<s.length;i++)
        {
            str+=s[i]+"\n";
            if(str.contains("tatphanmem"))
            {
                try {
                    Toast.makeText(GoogleRecognitionWithoutDialogActivity.this, "Tắt sau 2s",
                            Toast.LENGTH_SHORT).show();
//                    Thread.sleep(2000);
                    finishAffinity();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(LOG_TAG, "onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

}
