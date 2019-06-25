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
In case of `Car`, since there is `@Default` on a static method that return `Car`, DefaultJ use that to create an instance.
In case of `Driver`, its has a constructor requiring a `Car` instance so DefaultJ create a `Car` instance  and use that to create a `Driver` instance.
Calling default constructor is one of the strategy DefaultJ uses.
Factory method is another strategy.
DefaultJ has 14 built-in strategies for getting default values.
More detail about those strategies are discussed below.

It is important to note that DefaultJ does not require the annotation `@Default` to be
from DefaultJ.
The annotation can be from any package.
It just has to have `RUNTIME` retention.
This allow any code to specify default value providing strategy without depending on DefaultJ.

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
7. Singleton field -- a public static final field in the class that hold the default value.
8. Factory method -- a public static final method in the class that return the default value.
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

## Usage

### Using DefaultJ in a Gradle project

This project binary is published on [my maven repo](https://github.com/NawaMan/nawaman-maven-repository) hosted on GitHub. So to use DefaultJ you will need to ...

Add the dependencies to DefaultJ.

```Groovy
	compile 'io.defaultj:defaultj-core:2.0.0.0' // Please lookup for the latest version.
```

[UseNullableJGradle](https://github.com/NawaMan/UseNullableJGradle) is an example project that use NullableJ.
You can use that as a starting point.
Just add the dependency to DefaultJ to it.

### Using DefaultJ in a Maven project

Add the dependencies to DefaultJ.

```xml

	<dependency>
		<groupId>io.defaultj</groupId>
		<artifactId>defaultj-annotations</artifactId>
		<version>2.0.0.0</version>
	</dependency>
	<dependency>
		<groupId>io.defaultj</groupId>
		<artifactId>defaultj-core</artifactId>
		<version>2.0.0.0</version>
	</dependency>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.11</version>
		<scope>test</scope>
	</dependency>
```

[UseNullableJMaven](https://github.com/NawaMan/UseNullableJMaven) is an example project that use NullableJ.
You can use that as a starting point.
Just add the dependency to DefaultJ to it.

## Build

This project is developed as a gradle project on Eclipse
  so you can just clone and import it to your Eclipse.
Although, never tried, but I think it should be easy to import into IntelliJ.
Simply run `gradle clean build` to build the project (or use the build-in gradle wrapper).

## Versioning
The versioning of this project is not the commonly used semantic versioning.
Well, the last three digits are kind of semantic version.
But the first one represents a conceptual version of the library.
This is done this way as it was found that the version was updates too quickly
  and there is nothing indicates the fundamental change in concept or philosophy of the library.
  
- The first digit is the version of the concept - changed when there is a big changes across the library or in the fundamental ways.
- The second digit is the version of the API - changed when there is a breaking changes in the API.
- The third digit is the version of the implementation.
- The forth digit is the version of correction.

## Issues

Please use our [issues tracking page](https://github.com/NawaMan/DefaultJ/issues) to report any issues.

## Take what you need

You can import and use this library as you needed.
But if you just need a small part of it, feel free to fork it or just copy the part that you need. :-)


## Contribute

Feel free to join in.
Report problems, suggest solutions, suggest more functionalities, making pull requests ... anything is appreciated (please do it in [issues tracking page](https://github.com/NawaMan/DefaultJ/issues) or email me directly).

If this is useful to you and want to buy me a [coffee](https://www.paypal.me/NawaMan/2.00)
 or [lunch](https://www.paypal.me/NawaMan/10.00) or [help with my kids college fund](https://www.paypal.me/NawaMan/100.00) ... that would be great :-p

