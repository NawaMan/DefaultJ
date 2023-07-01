package defaultj.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Random;

import org.junit.Test;

import defaultj.api.IProvideDefault;
import defaultj.core.strategies.IFindSupplier;
import defaultj.core.utils.failable.Failable.Supplier;

public class AdditionalDefaultFinderTest {
    
    static interface StringRandomizer {
        public String newRandomString();
    }
    
    static class FinderThatKnowRandomizer implements IFindSupplier {
        private static final Random random = new Random();
        
        @SuppressWarnings("unchecked")
        @Override
        public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
                Class<TYPE> theGivenClass, IProvideDefault defaultProvider) {
            if (theGivenClass == StringRandomizer.class) {
                return () -> {
                    return (TYPE)(StringRandomizer)(()->{
                        var random = defaultProvider.optional(Random.class)
                                     .orElse (FinderThatKnowRandomizer.random);
                        return "#" + random.nextInt();
                    });
                };
            }
            return null;
        }
    }
    
    @Test
    public void testAdditionalSupplier() {
        var finder   = new FinderThatKnowRandomizer();
        var provider = DefaultProvider.instance
                     .withAdditionalSupplier(finder);
        var stringRandomizer = provider.get(StringRandomizer.class);
        assertNotEquals(stringRandomizer.newRandomString(),
                        stringRandomizer.newRandomString());
    }
    
    @Test
    public void testAdditionalSupplier_withBinding() {
        var finder   = new FinderThatKnowRandomizer();
        var noRandom = new Random() {
            private static final long serialVersionUID = 1L;
            public int nextInt() { return 0; }
        };
        var provider = DefaultProvider.instance
                     .withBinding(Random.class, noRandom)
                     .withAdditionalSupplier(finder);
        var stringRandomizer = provider.get(StringRandomizer.class);
        assertEquals(stringRandomizer.newRandomString(),
                     stringRandomizer.newRandomString());
    }
    
    @Test
    public void testAdditionalSupplier_withProvider() {
        var finder   = new FinderThatKnowRandomizer();
        var noRandom = new Random() {
            private static final long serialVersionUID = 1L;
            public int nextInt() { return 0; }
        };
        var providerWithBinding = DefaultProvider.instance
                                .withBinding(Random.class, noRandom);
        var provider = DefaultProvider.instance
                     .withAdditionalSupplier(finder, providerWithBinding);
        var stringRandomizer = provider.get(StringRandomizer.class);
        assertEquals(stringRandomizer.newRandomString(),
                     stringRandomizer.newRandomString());
    }
    
}
