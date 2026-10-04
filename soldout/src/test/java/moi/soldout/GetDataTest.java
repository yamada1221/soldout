package moi.soldout;

import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;

public class GetDataTest extends TestCase {
    public void testSuccessDoesNotRetry() {
        AtomicInteger calls = new AtomicInteger();
        assertEquals(123, GetData.getPopulation(3, () -> { calls.incrementAndGet(); return 123; }));
        assertEquals(1, calls.get());
    }

    public void testRetryReturnsTheSuccessfulResult() {
        AtomicInteger calls = new AtomicInteger();
        assertEquals(456, GetData.getPopulation(3, () -> calls.incrementAndGet() < 3 ? -1 : 456));
        assertEquals(3, calls.get());
    }

    public void testPermanentFailureStopsAtRetryLimit() {
        AtomicInteger calls = new AtomicInteger();
        assertEquals(-1, GetData.getPopulation(3, () -> { calls.incrementAndGet(); return -1; }));
        assertEquals(4, calls.get());
    }

    public void testZeroRetriesStillMakesInitialAttempt() {
        AtomicInteger calls = new AtomicInteger();
        assertEquals(-1, GetData.getPopulation(0, () -> { calls.incrementAndGet(); return -1; }));
        assertEquals(1, calls.get());
    }

    public void testNegativeRetryCountFailsBeforeFetching() {
        try {
            GetData.getPopulation(-1, () -> { fail("must not fetch"); return 0; });
            fail("negative retry count must be rejected");
        } catch (IllegalArgumentException expected) { }
    }
}
