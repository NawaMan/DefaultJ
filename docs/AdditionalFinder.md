# Additional Default Finders

Additional default finders allows greater customization to the default provider without actually implementing a new `IProvideDefault` so it can take advantage of existing strategies.

This is done by allowing user to add finders (implementations of `IFindSupplier`) for suppliers of classes.
These finders will be asked for the suppliers of the required class.
If the finders know how to find such supplier, it can return the supplier for that class.
If the finders do not want to engage with that particular class, it can return null and the default strategies will kick in.

```
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
```

In this example, the finder knows how to find the default for `StringRandomizer`.
Note: that this example can be implemented by other means which as binding or `@ImplementedBy` or `@DefaultInterface` but with this strategy can be used for more complicated example, just that we want to make it simple.

Now, you can use it to create a provider that knows the `StringRandomizer`.

```
var finder   = new FinderThatKnowRandomizer();
var provider = DefaultProvider.instance
             .withAdditionalSupplier(finder);
var stringRandomizer = provider.get(StringRandomizer.class);
assertNotEquals(stringRandomizer.newRandomString(),
                stringRandomizer.newRandomString());
```

Since `DefaultProvider` implements `IFindSupplier`, this method allows use to plug existing providers

```
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
```
