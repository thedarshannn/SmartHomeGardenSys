package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String TAG = "EncryptionUtils";
    private static final String FIXED_KEY = "1234567890123456"; // 16-byte key for AES (128 bits)

    public static String encrypt(String data) throws Exception {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Encryption failed", e);
            throw e;
        }
    }
}
