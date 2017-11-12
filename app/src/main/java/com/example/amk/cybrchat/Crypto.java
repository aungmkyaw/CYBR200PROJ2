package com.example.amk.cybrchat;
import android.util.Base64;

import org.libsodium.jni.SodiumConstants;
import org.libsodium.jni.crypto.Random;
import org.libsodium.jni.keys.KeyPair;
import org.libsodium.jni.keys.SigningKey;
import org.libsodium.jni.keys.VerifyKey;


/**
 * Created by Ming Yan Shu on 11/11/2017.
 */

public class Crypto {

    public static final int BASE64_SAFE_URL_FLAGS = Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP;

    public void generate() {
        byte[] seed = new Random().randomBytes(SodiumConstants.SECRETKEY_BYTES);
        /*seedView = (TextView) findViewById(R.id.seedKey);
        seedView.setText(Base64.encodeToString(seed, BASE64_SAFE_URL_FLAGS));*/
        generate(seed);
    }

    /**
     * Generates all sodium keys with a byte[] as seed
     */
    private void generate(byte[] seed) {
        generateEncryptionKeyPair(seed);
        generateSigningKeyPair(seed);
    }

    /**
     * Generate Encryption Key Pair
     *
     * @param seed as the seed we generated on generate()
     */
    private void generateEncryptionKeyPair(byte[] seed) {
        KeyPair encryptionKeyPair = new KeyPair(seed);
        byte[] encryptionPublicKey = encryptionKeyPair.getPublicKey().toBytes();
        byte[] encryptionPrivateKey = encryptionKeyPair.getPrivateKey().toBytes();
        /*publicKeyView = (TextView) findViewById(R.id.textViewPublic);
        privateKeyView = (TextView) findViewById(R.id.textViewPrivate);
        publicKeyView.setText(Base64.encodeToString(encryptionPublicKey, BASE64_SAFE_URL_FLAGS));
        privateKeyView.setText(Base64.encodeToString(encryptionPrivateKey, BASE64_SAFE_URL_FLAGS));*/
    }

    /**
     * Generate Sign Key Pair
     *
     * @param seed as the seed we generated on generate()
     */
    private void generateSigningKeyPair(byte[] seed) {
        SigningKey signingKey = new SigningKey(seed);
        VerifyKey verifyKey = signingKey.getVerifyKey();
        byte[] verifyKeyArray = verifyKey.toBytes();
        byte[] signingKeyArray = signingKey.toBytes();
        /*signKeyView = (TextView) findViewById(R.id.textViewSign);
        verifyKeyView = (TextView) findViewById(R.id.textViewVerify);
        signKeyView.setText(Base64.encodeToString(verifyKeyArray, BASE64_SAFE_URL_FLAGS));
        verifyKeyView.setText(Base64.encodeToString(signingKeyArray, BASE64_SAFE_URL_FLAGS));*/
    }

}
