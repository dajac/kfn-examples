package io.dajac.kfn.examples;

import io.dajac.kfn.invoker.Function;
import io.dajac.kfn.invoker.KeyValue;

public class CopyFunction implements Function<byte[], byte[], byte[], byte[]> {
    @Override
    public KeyValue<byte[], byte[]> apply(byte[] key, byte[] value) {
        return KeyValue.pair(key, value);
    }
}