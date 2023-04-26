package ntua.dblab.gskourts.streamingiot.service.consumers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomWindowTransformer implements Transformer<Integer, Integer, KeyValue<Integer, Integer>> {

    private ProcessorContext context;
    private long windowSize;
    private long advance;
    private Map<Integer, List<Integer>> windows;

    public CustomWindowTransformer(long windowSize, long advance) {
        this.windowSize = windowSize;
        this.advance = advance;
        this.windows = new HashMap<>();
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }


    @Override
    public KeyValue<Integer, Integer> transform(Integer key, Integer value) {
        List<Integer> values = windows.getOrDefault(key, new ArrayList<>());
        values.add(value);
        windows.put(key, values);

        // Remove out-of-window values
        while (!values.isEmpty() && context.timestamp() - values.get(0) > windowSize) {
            values.remove(0);
        }

        // Aggregate values in the window
        int aggregatedValue = values.stream().mapToInt(Integer::intValue).sum();

        // Advance the window
        if (context.timestamp() % advance == 0) {
            return KeyValue.pair(key, aggregatedValue);
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        // Any cleanup logic can be added here
    }
}

// this example uses a simple in-memory map for storing window state.
// For production, a state store backed by a persistent storage system (e.g., RocksDB) can be used, to handle larger volumes of data and ensure fault tolerance.