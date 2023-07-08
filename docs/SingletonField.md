# Singleton Field

`@Default` can be put on to a public static final field to hold the default value.

```
public static class BasicSingleton {
    @Default
    public static final BasicSingleton instance = new BasicSingleton("instance");
    
    private final String string;
    private BasicSingleton(String string) {
        this.string = string;
    }
}

assertEquals(BasicSingleton.instance, provider.get(BasicSingleton.class));
```