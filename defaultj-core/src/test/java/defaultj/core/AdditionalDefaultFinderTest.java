package defaultj.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Random;

import org.junit.Test;

import defaultj.api.IProvideDefault;
import defaultj.core.strategies.IFindSupplier;
import defaultj.core.utils.failable.Failable.Supplier;
import lombok.val;

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
                        val random = defaultProvider.optional(Random.class)
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
        val finder   = new FinderThatKnowRandomizer();
        val provider = DefaultProvider.instance
                     .withAdditionalSupplier(finder);
        val stringRandomizer = provider.get(StringRandomizer.class);
        assertNotEquals(stringRandomizer.newRandomString(),
                        stringRandomizer.newRandomString());
    }
    
    @Test
    public void testAdditionalSupplier_withBinding() {
        val finder   = new FinderThatKnowRandomizer();
        val noRandom = new Random() {
            private static final long serialVersionUID = 1L;
            public int nextInt() { return 0; }
        };
        val provider = DefaultProvider.instance
                     .withBinding(Random.class, noRandom)
                     .withAdditionalSupplier(finder);
        val stringRandomizer = provider.get(StringRandomizer.class);
        assertEquals(stringRandomizer.newRandomString(),
                     stringRandomizer.newRandomString());
    }
    
    @Test
    public void testAdditionalSupplier_withProvider() {
        val finder   = new FinderThatKnowRandomizer();
        val noRandom = new Random() {
            private static final long serialVersionUID = 1L;
            public int nextInt() { return 0; }
        };
        val providerWithBinding = DefaultProvider.instance
                                .withBinding(Random.class, noRandom);
        val provider = DefaultProvider.instance
                     .withAdditionalSupplier(finder, providerWithBinding);
        val stringRandomizer = provider.get(StringRandomizer.class);
        assertEquals(stringRandomizer.newRandomString(),
                     stringRandomizer.newRandomString());
    }
    
}
