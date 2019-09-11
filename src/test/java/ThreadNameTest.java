import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class ThreadNameTest {

    @Test
    public void threadLocalTest(){
        ThreadContext.set("MAIN");
        AtomicBoolean hasNamechanged = new AtomicBoolean(false);
        IntStream.range(0,10000000).boxed().parallel().forEach(n->{
            if(!"MAIN".equals(ThreadContext.get())){
                hasNamechanged.set(true);
            }
        });
        Assert.assertTrue(hasNamechanged.get());
    }

    private static class ThreadContext {
        private static ThreadLocal<String> val = ThreadLocal.withInitial(() -> "empty");

        public ThreadContext() {
        }

        public static String get() {
            return val.get();
        }

        public static void set(String x) {
            ThreadContext.val.set(x);
        }
    }

}
