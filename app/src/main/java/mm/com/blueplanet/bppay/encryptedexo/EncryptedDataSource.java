package mm.com.blueplanet.bppay.encryptedexo;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedDataSource implements DataSource {
    private DataSource upStream;
    private CipherInputStream cipherInputStream;

    public EncryptedDataSource(DataSource upStream) {
        this.upStream = upStream;
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        Cipher cipher = getDecryptor();
        DataSourceInputStream inputStream = new DataSourceInputStream(upStream, dataSpec);
        cipherInputStream = new CipherInputStream(inputStream, cipher);
        inputStream.open();
        return Long.valueOf(C.LENGTH_UNSET);
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws IOException {

        int bytesRead = cipherInputStream.read(buffer, offset, readLength);
        if (bytesRead < 0) {
            return C.RESULT_END_OF_INPUT;
        } else {
            return bytesRead;
        }
    }

    @Override
    public void addTransferListener(TransferListener transferListener) {

    }

    @Nullable
    @Override
    public Uri getUri() {
        return upStream != null ? upStream.getUri() : null;
    }

    @Override
    public void close() throws IOException {
        if (cipherInputStream != null) {
            cipherInputStream = null;
            if (upStream != null) {
                upStream.close();
            }
        }
    }

    private Cipher getDecryptor() {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec("0123456789012345".getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec("0123459876543210".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher;
        } catch (Exception e) {
            return null;
        }

    }
}
