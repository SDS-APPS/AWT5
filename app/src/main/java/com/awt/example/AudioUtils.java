package com.awt.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioUtils {

    public static float[][] loadWavAsFloatMatrix(String filepath, int channels) throws IOException {
        FileInputStream fis = new FileInputStream(new File(filepath));

        byte[] header = new byte[44];
        if (fis.read(header, 0, 44) != 44) {
            throw new IOException("Invalid WAV file: couldn't read header");
        }

        // Check PCM format & 16-bit
        if (header[20] != 1 || header[34] != 16) {
            throw new IOException("Only PCM 16-bit WAV files supported.");
        }

        int dataSize = (int) (new File(filepath).length()) - 44;
        byte[] audioBytes = new byte[dataSize];
        if (fis.read(audioBytes) != dataSize) {
            throw new IOException("Couldn't read audio data");
        }
        fis.close();

        int totalSamples = dataSize / 2 / channels;
        float[][] floatData = new float[channels][totalSamples];

        for (int i = 0; i < totalSamples; i++) {
            for (int ch = 0; ch < channels; ch++) {
                int index = (i * channels + ch) * 2;
                int low = audioBytes[index] & 0xFF;
                int high = audioBytes[index + 1];
                int sample = (high << 8) | low;

                // Convert to signed 16-bit
                if (sample > 32767) sample -= 65536;
                floatData[ch][i] = sample / 32768f;
            }
        }

        return floatData;
    }
}
