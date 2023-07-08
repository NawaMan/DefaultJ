# Factory Method

`@Default` can be put on to a public static method that when invoked to get the default value.

```
public static class BasicFactoryMethod {
    
    private static int counter = 0;
    
    private final String string;
    private BasicFactoryMethod(String string) {
        this.string = string;
    }
    
    @Default
    public static BasicFactoryMethod newInstance() {
        counter++;
        return new BasicFactoryMethod("factory#" + counter);
    }
}


System.out.println("factory#1", provider.get(BasicFactoryMethod.class).string);
System.out.println("factory#2", provider.get(BasicFactoryMethod.class).string);
```
