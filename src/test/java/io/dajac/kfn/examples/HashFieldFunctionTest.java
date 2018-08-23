package io.dajac.kfn.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Test;

import io.dajac.kfn.invoker.Function;
import io.dajac.kfn.invoker.KeyValue;;

public class HashFieldFunctionTest {

    @Test
    public void testHashField() {
        Schema schema = SchemaBuilder
            .record("User")
            .namespace("example")
            .fields()
                .name("name").type("string").noDefault()
                .name("number").type("string").noDefault()
            .endRecord();

        Properties props = new Properties();
        props.setProperty("field", "number");
        props.setProperty("algorythm", "sha-256");

        Function<String, GenericRecord, String, GenericRecord> function = new HashFieldFunction();

        function.configure(props);

        String inputKey = "David";
        GenericRecord inputRecord = new GenericData.Record(schema);
        inputRecord.put("name", "David");
        inputRecord.put("number", "+41791234567");

        KeyValue<String, GenericRecord> result = function.apply(inputKey, inputRecord);

        assertEquals(inputKey, result.key);
        assertEquals("David", result.value.get("name").toString());
        assertEquals("1a08ef565c13a1e790d8501276243c2c7907e1f6d093cdaec4f23c87e4ea1303", result.value.get("number").toString());
    }

    @Test
    public void testHashUnknownField() {
        Schema schema = SchemaBuilder
            .record("User")
            .namespace("example")
            .fields()
                .name("name").type("string").noDefault()
                .name("number").type("string").noDefault()
            .endRecord();

        Properties props = new Properties();
        props.setProperty("field", "blabla");
        props.setProperty("algorythm", "sha-256");

        Function<String, GenericRecord, String, GenericRecord> function = new HashFieldFunction();

        function.configure(props);

        String inputKey = "David";
        GenericRecord inputRecord = new GenericData.Record(schema);
        inputRecord.put("name", "David");
        inputRecord.put("number", "+41791234567");

        KeyValue<String, GenericRecord> result = function.apply(inputKey, inputRecord);

        assertEquals(inputKey, result.key);
        assertEquals("David", result.value.get("name").toString());
        assertEquals("+41791234567", result.value.get("number").toString());
    }
}