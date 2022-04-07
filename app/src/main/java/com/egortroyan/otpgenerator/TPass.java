package com.egortroyan.otpgenerator;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.O)
public class TPass extends Pass{
    private static final Instant T_PASS_EPOCH = Instant.parse("2000-01-01T00:00:00.00Z");
    private static final Duration TIME_STEP = TimeBasedOneTimePasswordGenerator.DEFAULT_TIME_STEP;
    private static final String TOTP_ALGORITHM = TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA256;

    private final String keyId;
    private final String seed;



    public TPass(String id, String key) {
        this.keyId = id;
        this.seed = key;
    }

    public String getOTP() {
        try {
            TimeBasedOneTimePasswordGenerator generator =
                    new TimeBasedOneTimePasswordGenerator(TIME_STEP, 6, TOTP_ALGORITHM);
            byte[] seed = DatatypeConverter.parseHexBinary(this.seed);
            SecretKeySpec secretKeySpec = new SecretKeySpec(seed, generator.getAlgorithm());
            Instant now = Instant.now();
            long duration = now.getEpochSecond() - T_PASS_EPOCH.getEpochSecond();
            return generator.generateOneTimePasswordString(secretKeySpec, Instant.ofEpochSecond(duration));
        } catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "0";
        }
    }

    public String getKeyId() {
        return keyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TPass tPass = (TPass) o;
        return keyId.equals(tPass.keyId) && seed.equals(tPass.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, seed);
    }
}
