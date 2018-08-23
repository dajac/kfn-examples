package io.dajac.kfn.examples;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import io.dajac.kfn.invoker.Function;
import io.dajac.kfn.invoker.KeyValue;

public class JsonToAvroFunction implements Function<byte[], String, String, GenericRecord> {
    private static final Logger LOG = LoggerFactory.getLogger(JsonToAvroFunction.class);

    private final String SCHEMA_PROPERTY_NAME = "schema";
    private final String KEY_PROPERTY_NAME = "key";

    private final DecoderFactory DECODER_FACTORY = DecoderFactory.get();

    private String key;
    private Schema schema;
    
    public void configure(Properties props) {
        this.key = props.getProperty(KEY_PROPERTY_NAME);
        String schemaString = props.getProperty(SCHEMA_PROPERTY_NAME);
        Schema.Parser parser = new Schema.Parser();
        this.schema = parser.parse(schemaString);
    }
    
    public KeyValue<String, GenericRecord> apply(byte[] key, String value) {
        try {
            GenericRecord record = (GenericRecord) jsonToAvro(value, this.schema);
            String newKey = ((Utf8) record.get(this.key)).toString();
            return KeyValue.pair(newKey, record);
        } catch (Exception e) {
            LOG.error("Unable to parse {} with {}", value, this.schema, e);
            return null;
        }
    }

    private Object jsonToAvro(String jsonString, Schema schema) throws IOException {
        DatumReader<Object> reader = new GenericDatumReader<Object>(schema);
        Object object = reader.read(null, DECODER_FACTORY.jsonDecoder(schema, jsonString));

        if (schema.getType().equals(Schema.Type.STRING)) {
            object = ((Utf8) object).toString();
        }
        return object;
    }
}