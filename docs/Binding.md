# Explicit Default Binding

Explicit default binding allows us to explicitly specify how the default value can be obtained.

## Instance Binding
Instance binding binds an instance to a class.

```
var bindings = new Bindings.Builder().bind(String.class, "Hello worlds!").build();
var provider = new DefaultProvider.Builder().bingings(bindings).build();
System.out.println(provider.get(String.class)); // This will print "Hello worlds!".
```

## Type Binding
Type binding binds a class to another class.
This means that when the class was asked for a default,
  DefaultJ will try to get it from another class.

```
public static class MyRunnable implements Runnable {
    @Override
    public void run() {
        ...
    }
}

var bindings = new Bindings.Builder().bind(Runnable.class, MyRunnable.class).build();
var provider = new DefaultProvider.Builder().bingings(bindings).build();
System.out.println(provider.get(Runnable.class) instanceof MyRunnable);     // This will print true.
```

## Factory Binding
Factory binding allows a class to be bind to a factory.

```
    public static class IncrementalIntegerFactory implements ICreateDefault<Integer> {
        private AtomicInteger integer = new AtomicInteger(0);
        @Override
        public Integer create(IProvideDefault defaultProvider) {
            return integer.getAndIncrement();
        }
    }
    
    var factoryBinding = new FactoryBinding<>(integerFactory);
    var bindings       = new Bindings.Builder().bind(Integer.class, factoryBinding).build();
    var provider       = new DefaultProvider.Builder().bingings(bindings).build();
    
    assertTrue(0 == provider.get(Integer.class));
    assertTrue(1 == provider.get(Integer.class));
    assertTrue(2 == provider.get(Integer.class));
```
