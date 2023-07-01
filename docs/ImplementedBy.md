# Implemented By

`@ImplementedBy` annotation can be used to tell DefaultJ to get default of another class as default of this class.

```
@ImplementedBy(TheClass.class)
public static interface TheInterface {
    
    public String getText();
    
}
public static class TheClass implements TheInterface {
    @Override
    public String getText() {
        return "I the class.";
    }
}


System.out.println(provider.get(TheInterface.class));   // prints "I the class."
```

It is also useful as a fallback for when `@DefaultImplementation` is used but the given class name is not found.


