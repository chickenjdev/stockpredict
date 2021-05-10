package predict.stock.utils;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.vertx.micrometer.backends.BackendRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsHelper {
    private static final Logger logger = LogManager.getLogger(MetricsHelper.class);
    private static final String PREFIX = "telco";
    private static final String MODULE = KeyUtils.SystemInfo.MODULE_NAME;
    private static final String SERVER_IP = KeyUtils.SystemInfo.IP_ADDRESS;
    private static final String TAIL_COUNT = ".processed.count";
    private static final String TAIL_TIME = ".processing.time";
    private static Map<String, Counter> mapCounterEvent;
    private static final AtomicLong counterId = new AtomicLong(0);
    private static final ConcurrentHashMap<Long, Timer.Sample> mapSample = new ConcurrentHashMap<>();
    private static MetricsHelper ins;

    public MetricsHelper() {
        mapCounterEvent = new HashMap<>();
    }

    public static MetricsHelper getIns() {
        if (ins == null) {
            ins = new MetricsHelper();
        }
        return ins;
    }

    public void counter(String key, String... tags) {
        try {
            if (mapCounterEvent.containsKey(key)) {
                mapCounterEvent.get(key).increment();
            } else {
                var registry = BackendRegistries.getDefaultNow();
                var counter = registry.counter(String.format("%s.%s.%s.%s", PREFIX, MODULE, SERVER_IP, key),
                        tags);
                counter.increment();
                mapCounterEvent.put(key, counter);
            }
        } catch (Exception e) {
            logger.error("counter metrics error: " + e.getMessage());
        }
    }

    public void counterEvent(String key, String... tags) {
        try {
            String[] newTags = new String[tags.length + 4];

            for (int i = 0; i < tags.length; i++) {
                newTags[i] = tags[i];
            }

            newTags[tags.length] = "server_ip";
            newTags[tags.length + 1] = SERVER_IP;
            newTags[tags.length + 2] = "module";
            newTags[tags.length + 3] = MODULE;

            var registry = BackendRegistries.getDefaultNow();
            var counter = registry.counter(String.format("%s.%s", PREFIX, key), newTags);
            counter.increment();
        } catch (Exception e) {
            logger.error("counter metrics error: " + e.getMessage());
        }
    }

    public static long startTimerMetricsWithCounter(String name, String... tags) {
        try {
            Pair<Long, Timer.Sample> pair = buildKeyWithSample();

            if (pair.fst == null) {
                logger.info("Build Timer fail with id : " + pair.fst);
                return -1;
            }

            logger.trace("Build Timer with id : " + pair.fst);

            if (name != null && !name.isEmpty()) {
                getCounter(name + ".in", tags).increment();
            }

            mapSample.put(pair.fst, pair.snd);

            logger.info("start counter tracer");
            return pair.fst;
        } catch (Exception e) {
            logger.error("startTimerMetricsWithCounter error", e);
            return 0;
        }
    }

    public static void stopTimerMetrics(long id, String name, boolean counter, String... tags) {
        try {
            if (mapSample.get(id) == null) {
                logger.error("Cant find TimerMetrics with id  : " + id);
                return;
            }

            for (String tag : tags) {
                if (tag == null || tag.isEmpty()) {
                    logger.trace("Ignore track metrics with tag empty");
                    return;
                }
            }

            if (counter) {
                getCounter(name + ".out", tags).increment();
            }

            Timer.Sample sample = mapSample.remove(id);
            long processTime = sample.stop(Timer.builder(PREFIX + "."  + MODULE + "." + name + TAIL_TIME)
                    .tags(tags)
                    .register(getRegistry()));
            logger.trace("Stop TimerMetrics with id : " + id + " with time : " + processTime);
        } catch (Exception e) {
            logger.error("stopTimerMetrics error", e);
        }
    }

    public static Counter getCounter(String name, String... tags) {
        return Counter
                .builder(PREFIX + "."  + MODULE + "." + name + TAIL_COUNT)
                .tags(tags)
                .tags("server", SERVER_IP)
                .register(getRegistry());
    }

    private static MeterRegistry getRegistry() {
        return BackendRegistries.getDefaultNow();
    }

    private static Pair<Long, Timer.Sample> buildKeyWithSample() {
        Pair<Long, Timer.Sample> pair = new Pair<>(counterId.incrementAndGet(), Timer.start());

        return pair;
    }
}
