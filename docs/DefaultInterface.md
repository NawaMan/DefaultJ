# Default Interface

This strategy is applied to an interface.
DefaultJ will try to create an instance of that interface.
This will only work if the interface has all its methods as default methods.
If the interface cannot be initialized, NonDefaultInterfaceException will be thrown.

```
@DefaultInterface
public static interface IGreet {
    public default String greet(String name) {
        return "Hello: " + name;
    }
}

assertEquals("Hello: there",  provider.get(IGreet.class).greet("there"));
```
