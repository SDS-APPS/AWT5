package com.awt.example;
import static java.lang.Math.round;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.audio.AudioSink;
import androidx.media3.exoplayer.audio.DefaultAudioSink;
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer;
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector;
import androidx.media3.ui.PlayerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.awt.example.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.awt.example.databinding.ActivityMainBinding;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.Session;
import com.arthenica.ffmpegkit.ReturnCode;


// import the class in which we load AWT5 native lib
import  android.awt5example.AWT5Library;

public class MainActivity extends AppCompatActivity {

    // Here come some global AWT5 demo variables and settings
    // Possibly, not the best  programming practice, but it works and shows the concept

    // For AWT versions requiring encoding/decoding licenses from AWT Licensing Server
    // Set these according to your AWT package type
    // Consult with the AWT Vendor to obtains your unique credentials
    boolean ENC_REQUIRES_LICENSE = false;
    boolean DEC_REQUIRES_LICENSE = false;

    // AWT Licensing Server credentials (for evaluation purposes only, not for commercial use!)
    String AWT_Lic_Serv_URL = "here comes a URL";
    String AWT_Lic_Serv_psw = "12308410";

    // global vars to hold license data
    byte[] enc_license = null;
    byte[] dec_license = null;
    boolean AWT_server_connection_err_flag = false;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        com.awt.example.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // defining "Start" button variable
        Button myStartButton;

        // Find the button by its ID
        myStartButton = findViewById(R.id.button_start);


        // Set the onClickListener for the button
        myStartButton.setOnClickListener(view -> {

            // This is where we go when the button is clicked
            StartButtonClicked();

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

//    private void StartButtonClicked() {
//        //AWT_process();
//        String inputPath = "/storage/emulated/0/Download/webview/demo1.mp4";
//        String outputPath = "/storage/emulated/0/Download/EncodedAudio/videoToAudio.wav";
//        extractAudio(inputPath, outputPath);
//    }

//    private void StartButtonClicked() {
////        PlayerView playerView = findViewById(R.id.playerView);
////
////        // Create a new ExoPlayer instance
////        ExoPlayer player = new ExoPlayer.Builder(this).build();
////
////        // Set the player to the view
////        playerView.setPlayer(player);
////
////        // Stream URL
////       // String liveUrl = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8";
////
////     //   String videoUrl = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
////        // Create media item
////      //  String videoUrl = "https://115.187.52.252/sdscoder1/GOODNESSTV/video.m3u8";
////        String videoUrl = "https://live-hls-web-aje.getaj.net/AJE/index.m3u8";
////        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
////        player.setMediaItem(mediaItem);
////
////        // Prepare the player
////        player.prepare();
////
////        // Start playing
////        player.setPlayWhenReady(true); // 🔁 Ensure autoplay starts
//        extractAudioFromLiveStream();
//    }

    private void StartButtonClicked() {

        AWT_process();

    }
    @Override
    protected void onStop() {
        super.onStop();
        PlayerView playerView = findViewById(R.id.playerView);
        Player player = playerView.getPlayer();
        if (player != null) {
            player.release();
        }
    }



    // --------------------------------------------------
    // Converts a string to byte array
    // --------------------------------------------------
    public static byte[] AWT_StringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len+1]; // +1 for ending 0 as C expects
        for (int i = 0; i < len; i ++) {
            data[i] = (byte) s.charAt(i);
        }
        data[len] = 0; // add ending 0 as C expects
        return data;
    }

    // --------------------------------------------------
    // Converts a byte array dat into string (up to 0 byte, which designates end-of-line in C)
    // --------------------------------------------------
    public static String AWT_ByteArrayToString(byte[] input) {
        int length = 0;
        while (length < input.length && input[length] != 0) {
            length++;
        }

        return new String(input, 0, length, StandardCharsets.UTF_8);
    }

    // --------------------------------------------------
    // This function is to scroll the text down in the TextView object
    // --------------------------------------------------
    public void AWT_ScrollToTextEnd(TextView textView) {
        // Ensure the TextView has ScrollingMovementMethod set
        if (!(textView.getMovementMethod() instanceof ScrollingMovementMethod)) {
            textView.setMovementMethod(new ScrollingMovementMethod());
        }

        // Check if layout is initialized
        if (textView.getLayout() != null) {
            int scrollAmount = textView.getLayout().getLineTop(textView.getLineCount()) - textView.getHeight();
            // If there's no need to scroll, scrollAmount will be <= 0
            textView.scrollTo(0, Math.max(scrollAmount, 0));
        }
    }

    // --------------------------------------------------
    // This function is used to print data into TextView ("console") and move the scroller to the end of text
    // --------------------------------------------------
    public void AWT_print(TextView tv, String msg) {
        runOnUiThread(() -> {
            tv.append(msg + "\n");
            tv.invalidate(); // Force UI refresh
        });
    }


    // --------------------------------------------------
    // This function obtains AWT license from AWT Licensing Server
    // Arguments:
    //    method: 0 for Encoder, 1 for Decoder
    //    license_req -- license request string as obtain from awt5_license_request()
    // --------------------------------------------------
    private void AWT_request_license(int method, String license_req) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(AWT_Lic_Serv_URL + "?method=" + method + "&pswrd=" + AWT_Lic_Serv_psw + "&licreq=" + license_req);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        String ServerResponse = readStream(in);
                        // Use the main thread's handler to process the license key
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                // Now 'ServerResponse' contains the raw HTML response
                                // assigning the license
                                if (method == 0)
                                    enc_license = AWT_StringToByteArray(ServerResponse);
                                if (method == 1)
                                    dec_license = AWT_StringToByteArray(ServerResponse);

                                AWT_process();

                            }
                        });
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AWT_server_connection_err_flag = true; // setting global error flag
                    AWT_process();
                }
            }
        });

    }

    // --------------------------------------------------
    // Main AWT5 demo/test code demonstrating all API calls and workflow
    // --------------------------------------------------
    public void AWT_process() {
        TextView outputTextView = findViewById(R.id.TextView_output);
        outputTextView.setMovementMethod(new ScrollingMovementMethod());

        int WORK_MODE = 0;
        int audio_fs = 48000;
        int audio_chans = 2;
        int audio_duration_to_process_sec = 20;
        float pulse_every_sec = 0.3F;
        int silent_chan = 1;
        int audio_chan_to_analyze = 0;
        int bot_freq_hz = 1750;
        int top_freq_hz = 8750;
        int framesync_freq = 2500;
        int payload_frames = 2;
        int crc_prcnt = 30;
        float enc_aggr = 0.75f;
        float enc_transient_guard = 0.0f;
        int enc_emboss_gain = -96;
        int enc_emphasis_gain = 6;
        int enc_synth_carrier_gain = -96;
        int enc_synth_carrier_gain_min = -96;
        int enc_synth_carrier_gain_max = -96;
        float enc_synth_carrier_cycle = 1.0f;
        float dec_lookback_sec = 0.9F;
        int dec_pitchshift_prc = 2;

       // byte[] enc_wm_payload1 = AWT_StringToByteArray("0xABCDEF");
        byte[] enc_wm_payload1 = AWT_StringToByteArray("0x123456");
    //    byte[] enc_wm_payload1 = AWT_StringToByteArray("0xBABE00");

        int dec_wm_payload_len = 3;
        long[] enc_obj = new long[1];
        int[] enc_algo_delay = new int[1];
        int[] enc_frame_size = new int[1];
        String enc_license_request;
        long[] dec_obj = new long[1];
        int[] dec_frame_size = new int[1];
        String dec_license_request;
        byte[] dec_wm_payload = new byte[128];
        float[] dec_wm_reliabil = new float[1];

        int result;
        int[] per_chan_processed_sample_counter = new int[2];
        byte[] byte_buffer_temp = new byte[128];

        // ========== LICENSE ==========
        if (!ENC_REQUIRES_LICENSE) enc_license = AWT_StringToByteArray("\0");
        if (!DEC_REQUIRES_LICENSE) dec_license = AWT_StringToByteArray("\0");

        if (ENC_REQUIRES_LICENSE && enc_license == null && !AWT_server_connection_err_flag) {
            AWT5Library.awt5_license_request(audio_duration_to_process_sec, byte_buffer_temp);
            enc_license_request = AWT_ByteArrayToString(byte_buffer_temp);
            AWT_print(outputTextView, "Encoder License Request: " + enc_license_request);
            AWT_request_license(0, enc_license_request);
            return;
        }

        if (DEC_REQUIRES_LICENSE && dec_license == null && !AWT_server_connection_err_flag) {
            AWT5Library.awt5_license_request(audio_duration_to_process_sec, byte_buffer_temp);
            dec_license_request = AWT_ByteArrayToString(byte_buffer_temp);
            AWT_print(outputTextView, "Decoder License Request: " + dec_license_request);
            AWT_request_license(1, dec_license_request);
            return;
        }

        AWT5Library.awt5_sn(byte_buffer_temp);
        AWT_print(outputTextView, "AWT5 Serial: " + AWT_ByteArrayToString(byte_buffer_temp));

        if (AWT_server_connection_err_flag) {
            AWT_print(outputTextView, "❌ License server connection error");
            return;
        }

        // ========== INIT ENCODER ==========
        result = AWT5Library.awt5_encode_stream_init(enc_obj, enc_algo_delay, enc_frame_size, audio_fs, audio_chans, enc_wm_payload1,
                bot_freq_hz, top_freq_hz, framesync_freq, payload_frames, crc_prcnt,
                enc_aggr, enc_transient_guard, enc_emboss_gain, enc_emphasis_gain, enc_synth_carrier_gain,
                enc_synth_carrier_gain_min, enc_synth_carrier_gain_max, enc_synth_carrier_cycle, enc_license);
        if (result != 0) {
            AWT_print(outputTextView, "❌ Encoder Init Failed: " + result);
            return;
        }
        long encObj = enc_obj[0];

        // ========== INIT DECODER ==========
        result = AWT5Library.awt5_decode_stream_init(dec_obj, dec_frame_size, dec_wm_payload_len, audio_fs, bot_freq_hz, top_freq_hz,
                framesync_freq, payload_frames, crc_prcnt, dec_lookback_sec, dec_pitchshift_prc, dec_license);
        if (result != 0) {
            AWT_print(outputTextView, "❌ Decoder Init Failed: " + result);
            return;
        }
        long decObj = dec_obj[0];

        AWT5Library.awt5_encode_stream_set_bypass(encObj, 1);
        AWT5Library.awt5_encode_stream_set_bypass(encObj, 0);

        int total_frames = audio_duration_to_process_sec * audio_fs / enc_frame_size[0];
        int multichan_samples = enc_frame_size[0] * audio_chans;
        float[] inputBuffer = new float[multichan_samples];
        float[] outputBuffer = new float[multichan_samples];
        float[] decodeBuffer = new float[enc_frame_size[0]];
        int[] embeddingOccurred = new int[1];

        float[][] fullAudioData;
        try {
            fullAudioData = AudioUtils.loadWavAsFloatMatrix("/storage/emulated/0/Download/webview/Conbb.wav", 2);
        } catch (IOException e) {
            AWT_print(outputTextView, "❌ WAV Load Error: " + e.getMessage());
            return;
        }

        for (int ch = 0; ch < audio_chans; ch++) per_chan_processed_sample_counter[ch] = 0;

        List<float[]> outputFrames = new ArrayList<>();

        AWT_print(outputTextView, "🚀 Processing Started...");

        for (int frame_counter = 0; frame_counter < total_frames; frame_counter++) {

            for (int ch = 0; ch < audio_chans; ch++) {
                for (int i = 0; i < enc_frame_size[0]; i++) {
                    int idx = per_chan_processed_sample_counter[ch];
                    inputBuffer[ch * enc_frame_size[0] + i] = (idx < fullAudioData[ch].length)
                            ? fullAudioData[ch][idx]
                            : 0f;
                    per_chan_processed_sample_counter[ch]++;
                }
            }

            result = AWT5Library.awt5_encode_stream_buffer(encObj, inputBuffer, outputBuffer, embeddingOccurred);
            if (result != 0) {
                AWT_print(outputTextView, "❌ Encoder Error: " + result);
                return;
            }

            float[] frameCopy = new float[multichan_samples];
            System.arraycopy(outputBuffer, 0, frameCopy, 0, multichan_samples);
            outputFrames.add(frameCopy);

            if (WORK_MODE == 0) {
                System.arraycopy(outputBuffer, audio_chan_to_analyze * enc_frame_size[0],
                        decodeBuffer, 0, enc_frame_size[0]);

                result = AWT5Library.awt5_decode_stream_buffer(decObj, decodeBuffer, dec_wm_payload, dec_wm_reliabil);
                if (result != 0) {
                    AWT_print(outputTextView, "❌ Decoder Error: " + result);
                    return;
                }

                if (dec_wm_reliabil[0] > 0.01f) {
                    StringBuilder payloadHex = new StringBuilder();
                    for (int i = 0; i < dec_wm_payload_len; i++) {
                        payloadHex.append(String.format("%02X", dec_wm_payload[i]));
                    }
                    AWT_print(outputTextView, "✅ Watermark: 0x" + payloadHex +
                            " | Reliability: " + dec_wm_reliabil[0]);
                }
            }
        }

        // Combine all frames into single encoded audio
        int totalSamples = total_frames * enc_frame_size[0];
        float[][] encodedAudio = new float[audio_chans][totalSamples];

        for (int frame = 0; frame < total_frames; frame++) {
            float[] frameData = outputFrames.get(frame);
            for (int ch = 0; ch < audio_chans; ch++) {
                for (int i = 0; i < enc_frame_size[0]; i++) {
                    int sampleIndex = frame * enc_frame_size[0] + i;
                    encodedAudio[ch][sampleIndex] = frameData[ch * enc_frame_size[0] + i];
                }
            }
        }

        try {
            String outputPath = "/storage/emulated/0/Download/EncodedAudio/encoded_output_newbb.wav";
            saveFloatArrayAsWav(encodedAudio, audio_fs, outputPath);
            AWT_print(outputTextView, "💾 Encoded file saved to: " + outputPath);
        } catch (IOException e) {
            AWT_print(outputTextView, "❌ Save Failed: " + e.getMessage());
        }

        AWT_print(outputTextView, "\n✅ Completed: Processed " + total_frames + " frames");

        AWT5Library.awt5_encode_stream_kill(new long[]{encObj});
        AWT5Library.awt5_decode_stream_kill(new long[]{decObj});
        enc_license = null;
        dec_license = null;
    }

    public void saveFloatArrayAsWav(float[][] audioData, int sampleRate, String outputPath) throws IOException {
        int numChannels = audioData.length;
        int numSamples = audioData[0].length;
        int bitsPerSample = 16;
        int byteRate = sampleRate * numChannels * bitsPerSample / 8;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

        out.writeBytes("RIFF");
        out.writeInt(Integer.reverseBytes(36 + numSamples * numChannels * 2));
        out.writeBytes("WAVE");
        out.writeBytes("fmt ");
        out.writeInt(Integer.reverseBytes(16));
        out.writeShort(Short.reverseBytes((short) 1));
        out.writeShort(Short.reverseBytes((short) numChannels));
        out.writeInt(Integer.reverseBytes(sampleRate));
        out.writeInt(Integer.reverseBytes(byteRate));
        out.writeShort(Short.reverseBytes((short) (numChannels * bitsPerSample / 8)));
        out.writeShort(Short.reverseBytes((short) bitsPerSample));
        out.writeBytes("data");
        out.writeInt(Integer.reverseBytes(numSamples * numChannels * 2));

        for (int i = 0; i < numSamples; i++) {
            for (int ch = 0; ch < numChannels; ch++) {
                int sample = (int) (audioData[ch][i] * 32767.0);
                sample = Math.max(-32768, Math.min(32767, sample));
                out.writeShort(Short.reverseBytes((short) sample));
            }
        }

        out.flush();
        out.close();

        File file = new File(outputPath);
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(byteArrayOutputStream.toByteArray());
        fos.close();
    }
//    public void extractAudioFromVideo(String videoPath, String outputWavPath) {
//        String cmd = "-i " + videoPath + " -vn -acodec pcm_s16le -ar 48000 -ac 2 " + outputWavPath;
//
//        int rc = FFmpeg.execute(cmd);
//        if (rc == 0) {
//            Log.i("FFmpeg", "✅ Audio extracted to WAV successfully!");
//        } else {
//            Log.e("FFmpeg", "❌ Audio extraction failed! Code: " + rc);
//        }
//    }

    public static boolean extractAudio(String videoPath, String outputAudioPath) {
        MediaExtractor extractor = null;
        MediaMuxer muxer = null;

        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(videoPath);

            // Find the audio track index
            int audioTrackIndex = -1;
            MediaFormat audioFormat = null;
            int numTracks = extractor.getTrackCount();

            for (int i = 0; i < numTracks; i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("audio/")) {
                    audioTrackIndex = i;
                    audioFormat = format;
                    break;
                }
            }

            if (audioTrackIndex == -1) {
                return false; // No audio track found
            }

            // Set up the muxer for the output audio file
            muxer = new MediaMuxer(outputAudioPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            // Configure the extractor to read the audio track
            extractor.selectTrack(audioTrackIndex);

            // Add the audio track to the muxer
            int muxerTrackIndex = muxer.addTrack(audioFormat);
            muxer.start();

            // Read and write the data
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024); // 1MB buffer
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            boolean sawEOS = false;

            while (!sawEOS) {
                int sampleSize = extractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    sawEOS = true;
                    bufferInfo.size = 0;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                } else {
                    // Convert MediaExtractor flags to MediaCodec flags
                    int flags = 0;
                    int extractorFlags = extractor.getSampleFlags();

                    if ((extractorFlags & MediaExtractor.SAMPLE_FLAG_SYNC) != 0) {
                        flags |= MediaCodec.BUFFER_FLAG_KEY_FRAME;
                    }
                    if ((extractorFlags & MediaExtractor.SAMPLE_FLAG_PARTIAL_FRAME) != 0) {
                        flags |= MediaCodec.BUFFER_FLAG_PARTIAL_FRAME;
                    }

                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleSize;
                    bufferInfo.flags = flags;
                    bufferInfo.presentationTimeUs = extractor.getSampleTime();
                }

                muxer.writeSampleData(muxerTrackIndex, buffer, bufferInfo);

                if (!sawEOS) {
                    extractor.advance();
                }
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (extractor != null) {
                extractor.release();
            }
            if (muxer != null) {
                try {
                    muxer.stop();
                    muxer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void extractAudioFromLiveStream() {
        String streamUrl = "https://live-hls-web-aje.getaj.net/AJE/index.m3u8";
        String outputPath = getExternalFilesDir(null).getAbsolutePath() + "/extracted_audio.aac";

        FFmpegKitConfig.enableLogCallback(log -> {
            Log.d("FFmpegLog", log.getMessage());
        });

        String ffmpegCommand = "-i " + streamUrl + " -vn -c:a copy -t 00:01:00 \"" + outputPath + "\"";

        FFmpegKit.executeAsync(ffmpegCommand, session -> {
            ReturnCode returnCode = session.getReturnCode();

            if (ReturnCode.isSuccess(returnCode)) {
                Log.i("FFmpeg", "✅ Audio extracted successfully: " + outputPath);
            } else {
                Log.e("FFmpeg", "❌ Failed to extract audio");
                Log.e("FFmpeg", "Fail log: " + session.getAllLogsAsString());
            }
        });
    }


}


