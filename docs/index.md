# DefaultJ

![alt "Build status"](https://travis-ci.org/NawaMan/DefaultJ.svg?branch=master)

DefaultJ is a Java library to provide a default value of any class.
It support multiple strategies of getting the default value.
It can be use as a simple but powerful dependency injection utility.
In that aspect, it injects value base on the class, very much similar in principle with Guice.

## Quick example

```Java
	public class Car {
		@Default
		public static Car newInstance() { return new Car(); }
		
		public String go() {
			return "Zoom!";
		}
	}
	
	public class Driver {
		private Car car;
		public Driver(Car car) {
			this.car = car;
		}
		public String drive() {
			return car.go();
		}
	}
	
	...
	Car myCar = DefaultProvider.instance.get(Car.class);
	assertEquals("Zoom!", myCar.go());
	
	Driver myDriver = DefaultProvider.instance.get(Driver.class);
	assertEquals("Zoom!", myDriver.drive());
```

As you can see, DefaultJ can get default values of the `Car` and Driver.
In case of `Car`, since there is one constructor, DefaultJ use that to create an instance of that.
In case of `Driver`, its constructor requires a `Car` instance so DefaultJ create a `Car` instance  and use that to create a `Driver` instance.
Calling default constructor is one of the strategy DefaultJ uses.
More detail about those strategies are discussed below.

## Getting the default
There are strategies DefaultJ deploys to obtain a default value.
Calling the default constructor is just one of such strategies.
With the help of a few annotations,
  DefaultJ will try different strategy to obtains the default value.
The followings are the strategies in order that DefaultJ attempts.
If the any strategy is not applicable, DefaultJ will skip it to the next one.

1. **Bindings** -- Explicit binding of how the value is obtained.
2. **@DefaultImplementation** -- specify a class name for the be used as default (only if in the classpath).
3. **@ImplementationBy** -- specify a class that is the default implementation.
4. **Enum value** -- default value for enum.
5. **@DefaultInterface** -- specify that all methods in the interface are default methods.
6. Additional finder -- additional strategies.
7. **Singleton field** -- a public static final field in the class that hold the default value.
8. **Factory method** -- a public static final method in the class that return the default value.
9. **@DefaultToNull** -- specify that the default for this class is `null`.
10. **@Default on constructor** -- specify the default constructor to use.
11. **Only constructor** -- use the only constructor.
12. **Default constructor** -- use the default constructor (the constructor with no argument).
13. **KnowNullValue** -- [NullableJ](https://github.com/NawaMan/NullableJ) known null value.
14. **KnownNewValue** -- [NullableJ](https://github.com/NawaMan/NullableJ) known new value.

## Note on Annotation
As mentioned, DefaultJ check for annotations for more clue on how to provide default,
  there are a few points to be noted on annotation used with DefaultJ.
1. All the annotation is defined in DefaultJ's Annotations package.
2. DefaultJ check the annotation by name so you don't need to use the provided annotation.
   If desired, you can created and use yourown annotations.
   The name just have to match and the retention policy must be specified to Runtime (RetentionPolicy.RUNTIME).
   See many unit tests for example.
3. Annotation processors to validate the use of annotation are available in DefaultJ's Annotations package.
   Consult your IDE/build system on how to enable that.
   The supported validations are:
   1) @Default on methods/fields must be for `public` `static` and `final`,
   2) @DefaultInterface must be on an interface with all default methods,
   3) @ImplementationBy must contains the class that exists and compatible with the annotated class.

## Usage, Build, Issues and Contribute
Please refer to the main [README](https://github.com/NawaMan/DefaultJ) page for the information on
  the usage, build, issues and contribution.
