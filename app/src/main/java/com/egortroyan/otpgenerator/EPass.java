package com.egortroyan.otpgenerator;

import com.eatthepath.otp.HmacOneTimePasswordGenerator;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

public class EPass {
    private int counter;
    private String key;


    public EPass(int counter, String key) {
        this.counter = counter;
        this.key = key;
    }

    public String getOTP() {
        try {
            HmacOneTimePasswordGenerator generator =
                    new HmacOneTimePasswordGenerator(6);
            byte[] seed = DatatypeConverter.parseHexBinary(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(seed, generator.getAlgorithm());

            return generator.generateOneTimePasswordString(secretKeySpec, counter++);
        } catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "0";
        }
    }

    public int getCounter() {
        return counter;
    }
}
