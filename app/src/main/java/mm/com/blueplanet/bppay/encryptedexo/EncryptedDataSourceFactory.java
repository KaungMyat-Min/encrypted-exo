package mm.com.blueplanet.bppay.encryptedexo;

import com.google.android.exoplayer2.upstream.DataSource;

public class EncryptedDataSourceFactory implements DataSource.Factory {
    private DataSource mDataSource;

    public EncryptedDataSourceFactory(DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public DataSource createDataSource() {
        return new EncryptedDataSource(mDataSource);
    }
}
