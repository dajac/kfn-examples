package io.dajac.kfn.examples;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dajac.kfn.invoker.Function;
import io.dajac.kfn.invoker.KeyValue;

public class HashFieldFunction implements Function<String, GenericRecord, String, GenericRecord> {
    private static final Logger LOG = LoggerFactory.getLogger(HashFieldFunction.class);

    private final String ALGORYTHM_PROPERTY_NAME = "algorythm";
    private final String FIELD_PROPERTY_NAME = "field";

    private String algorythm;
    private String field;
    private MessageDigest digest;
    
    public void configure(Properties props) {
        this.algorythm = props.getProperty(ALGORYTHM_PROPERTY_NAME, "SHA-256");
        this.field = props.getProperty(FIELD_PROPERTY_NAME);
        
        try {
            this.digest = MessageDigest.getInstance(this.algorythm);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Unable to load {} algorythm", this.algorythm, e);
        }
    }
    
    public KeyValue<String, GenericRecord> apply(String key, GenericRecord value) {
        try {
            Object field = value.get(this.field);

            if (field instanceof String) {
                value.put(this.field, hash((String) field));
            } else if (field instanceof Utf8) {
                value.put(this.field, hash((Utf8) field));
            }

            return KeyValue.pair(key, value);
        } catch (Exception e) {
            LOG.error("Unable to processs {} because {}", value, e.getMessage(), e);
            return null;
        }
    }
    
    private Utf8 hash(Utf8 value) {
        return new Utf8(hash(value.toString()));
    }

    private String hash(String input) {
        byte[] result = digest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}