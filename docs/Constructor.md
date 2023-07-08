# Constructor
Constructor strategy simply get the instance from a public constructor.

## @Default on Constructor
Fist DefaultJ will see if there is any constructor with `@Default`.
If multiple constructor has `@Default`, the first in the code will be used.

## Only Constructor
If no constructor with `@Default` is found and only one constructor is found.
The only one constructor will be used.

## Constructor with no argument
If multiple constructor has `@Default`, the one with no argument will be used.

## Multiple constructors with no `@Default`
If a class has multiple constructors but none of them has `@Default`,
    DefaultJ will will not choose any of the constructor.

## PostConstructor
Once an instance is created using any of the selected constructor,
    DefaultJ will look for any method with `@PostConstructor` and will call them.
