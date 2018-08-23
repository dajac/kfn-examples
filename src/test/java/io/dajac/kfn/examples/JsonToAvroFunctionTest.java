package io.dajac.kfn.examples;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.junit.Test;

import io.dajac.kfn.invoker.Function;
import io.dajac.kfn.invoker.KeyValue;;

public class JsonToAvroFunctionTest {

    @Test
    public void testParseRecord() {
        Schema schema = SchemaBuilder
            .record("User")
            .namespace("example")
            .fields()
                .name("name").type("string").noDefault()
                .name("number").type("string").noDefault()
            .endRecord();

        Properties props = new Properties();
        props.setProperty("schema", schema.toString());
        props.setProperty("key", "name");

        Function<byte[], String, String, GenericRecord> function = new JsonToAvroFunction();

        function.configure(props);

        byte[] inputKey = {0x00};
        String inputValue = "{\"name\": \"David\", \"number\": \"+41791234567\"}";

        KeyValue<String, GenericRecord> result = function.apply(inputKey, inputValue);

        assertEquals("David", result.key);
        assertEquals("David", result.value.get("name").toString());
        assertEquals("+41791234567", result.value.get("number").toString());
    }

    @Test
    public void testParseUnvalidRecord() {
        Schema schema = SchemaBuilder
            .record("User")
            .namespace("example")
            .fields()
                .name("name").type("string").noDefault()
                .name("number").type("string").noDefault()
            .endRecord();

        Properties props = new Properties();
        props.setProperty("schema", schema.toString());
        props.setProperty("key", "name");

        Function<byte[], String, String, GenericRecord> function = new JsonToAvroFunction();

        function.configure(props);

        byte[] inputKey = {0x00};
        String inputValue = "{\"name\": \"David\", \"dont_exist\": \"+41791234567\"}";

        KeyValue<String, GenericRecord> result = function.apply(inputKey, inputValue);
        
        assertNull(result);
    }
}