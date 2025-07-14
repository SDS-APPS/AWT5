package android.awt5example;

// ===================================================
// AWT5 class (Android) for native AWT5 lib calls
// (c) Alex Radzishevsky, 2007-2025
// U.S. Patent No. 11,978,461
// http://audiowatermarking.com (.info)
//
// Corresponds to AWT5 SDK 0.03.03+
// Update: 2025-03-28
// ===================================================

// ------------------------------------------------------
// ------------------------------------------------------
// Refer to the 'awt5_android_wrapper.c' or to 'awt5_sdk.h' for
// detailed comments and descriptions of the parameters.
// ------------------------------------------------------
// ------------------------------------------------------

public class AWT5Library {

    // 'lib_shared_AWT5_SDK.so' -- not including the 'lib' prefix or the '.so' extension
    static {
        System.loadLibrary("_shared_AWT5_SDK");
    }


    // Native methods list

    // ------------------------------------------------------
    // --- General AWT5 calls
    public static native void awt5_sn(byte[] serialNumber);


    // ------------------------------------------------------
    // --- AWT5 Encoder-related calls
    public static native int awt5_encode_stream_init(long[] enc_obj, int[] algorithmic_delay, int[] frame_size, int fs, int chans,
                                                     byte[] wm_payload, int bot_freq_hz, int top_freq_hz, int framesync_freq,
                                                     int payload_frames, int CRC_prcnt, float enc_aggr, float transient_guard,
                                                     int emboss_gain, int emphasis_gain, int synth_carrier_gain, int synth_carrier_gain_min,
                                                     int synth_carrier_gain_max, float synth_carrier_cycle, byte[] license);

    public static native int awt5_encode_stream_set_payload(long enc_obj, byte[] new_hex_payload, byte[] license);

    public static native int awt5_encode_stream_set_bypass(long enc_obj, int bypass);

    public static native int awt5_encode_stream_buffer(long enc_obj, float[] input_frame, float[] output_frame, int[] embedding_occurred);

    public static native int awt5_encode_stream_event_set(long enc_obj, byte[] hex_payload, float time_to_event, float event_preroll, int event_preroll_block);

    public static native void awt5_encode_stream_kill(long[] enc_obj);

    public static native int awt5_encode_status(long enc_obj, byte[] wm_payload, int[] bypass, int[] event_encoding);

    public static native int awt5_encode_stream_timestamp_init(long enc_obj, float  time_granule_sec, float event_preroll, int event_preroll_block, float  current_time_sec, float[] time_wrap_cycle_sec);

    public static native int awt5_encode_stream_timestamp_apply(long enc_obj);


    // ------------------------------------------------------
    // ---AWT5 Decoder-related calls

    public static native int awt5_decode_stream_init(long[] dec_obj, int[] frame_size, int wm_payload_len, int fs, int bot_freq_hz,
                                                     int top_freq_hz, int framesync_freq, int payload_frames, int CRC_prcnt,
                                                     float lookback_sec, int pitch_shift_prc, byte[] license);

    public static native int awt5_decode_stream_buffer(long dec_obj, float[] input_frame, byte[] wm_payload, float[] wm_reliability);

    public static native int awt5_decode_stream_event_buffer(long dec_obj, float[] input_frame, byte[] event_payload,
                                                             float[] event_reliability, byte[] raw_wm_payload, float[] raw_wm_reliability,
                                                             float event_preroll, int event_preroll_block, float block_reliability_thr, int event_preroll_blocks_thr);

    public static native void awt5_decode_stream_kill(long[] dec_obj);

    public static native int awt5_decode_stream_timestamp_init(long dec_obj, float  time_granule_sec, float  event_preroll, int event_preroll_block, float  block_reliability_thr,
                                                               int  preroll_blocks_thr, float  event_reliability_thr, long[] time_wrap_cycle_smpls, float[] time_wrap_cycle_sec);

    public static native int awt5_decode_stream_timestamp_buffer(long dec_obj, float[] input_frame, double[] accumulated_conveyed_stream_time_sec,
                                                                 int[] extracted, int[] adjusted, double[] inst_conveyed_stream_time_sec, float[] inst_conveyed_stream_time_reliability);



    // ------------------------------------------------------
    // --- AWT5 SaaS/Client-Server tied version

    public static native void awt5_license_request(int duration, byte[] result);

    public static native int awt5_license_info(byte[] license, int method, int[] valid, int[] duration);


}