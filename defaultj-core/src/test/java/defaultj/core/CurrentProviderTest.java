package defaultj.core;

import org.junit.Test;

import defaultj.core.Bindings;
import defaultj.core.DefaultProvider;
import defaultj.core.bindings.TypeBinding;

import static org.junit.Assert.assertEquals;

import lombok.val;

@SuppressWarnings("javadoc")
public class CurrentProviderTest {
    
    public static interface Greet {
        public String greet();
    }
    
    public static class Hello implements Greet {
        @Override
        public String greet() {
            return "Hello";
        }
    }
    public static class Hi extends Hello {
        @Override
        public String greet() {
            return "Hi";
        }
    }
    
    @Test
    public void testThatSubsequnceGetStillWithTheSameProvider() {
        val bindings = new Bindings.Builder()
            .bind(Greet.class, new TypeBinding<Hello>(Hello.class))
            .bind(Hello.class, new TypeBinding<Hello>(Hi.class))
            .build();
        val provider = DefaultProvider.instance.wihtBindings(bindings);
        assertEquals("Hi", provider.get(Greet.class).greet());
    }
    
}
