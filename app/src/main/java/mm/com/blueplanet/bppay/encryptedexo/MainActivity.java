package mm.com.blueplanet.bppay.encryptedexo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {


    private CipherOutputStream outputStream;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_encrypt).setOnClickListener(view -> {
            encrypt();
        });

        findViewById(R.id.btn_play).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
            MainActivity.this.startActivity(intent);
        });

        findViewById(R.id.btn_decrypt).setOnClickListener(view -> {
            decrypt();
        });
    }

    private void decrypt() {
        try {
            File encryptedFile = new File(getExternalFilesDir("haha"), "encrypt.mp4");
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(encryptedFile), cipher);

            File decryptedFile = new File(getExternalFilesDir("haha"), "decrypted.mp4");
            OutputStream outputStream = new FileOutputStream(decryptedFile);


            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            cipherInputStream.close();
            outputStream.close();

        } catch (Exception e) {

        }
    }


    private void encrypt() {
        try {


            File file = new File(getExternalFilesDir("haha"), "1.mp4");
            InputStream inputStream = new FileInputStream(file);

            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

            File encryptedFile = new File(getExternalFilesDir("haha"), "encrypt.mp4");
            OutputStream fos = new FileOutputStream(encryptedFile);
            outputStream = new CipherOutputStream(fos, cipher);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();

            Log.d(TAG, "DONE");
        } catch (Exception e) {

        }
    }

    private Cipher getCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec("0123456789012345".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("0123459876543210".getBytes());
        cipher.init(mode, keySpec, ivSpec);
        return cipher;
    }

}
