package com.egortroyan.otpgenerator;

import com.eatthepath.otp.HmacOneTimePasswordGenerator;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Objects;

public class EPass extends Pass{

    private final String keyId;
    private final String seed;
    private int counter;

    public EPass(int counter, String id, String key) {
        this.keyId = id;
        this.counter = counter;
        this.seed = key;
    }

    public String getOTP() {
        try {
            HmacOneTimePasswordGenerator generator =
                    new HmacOneTimePasswordGenerator(6);
            byte[] seed = DatatypeConverter.parseHexBinary(this.seed);
            SecretKeySpec secretKeySpec = new SecretKeySpec(seed, generator.getAlgorithm());

            return generator.generateOneTimePasswordString(secretKeySpec, counter++);
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
        EPass pass = (EPass) o;
        return counter == pass.counter && keyId.equals(pass.keyId) && seed.equals(pass.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, seed, counter);
    }
}
