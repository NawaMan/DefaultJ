# DefaultJ

[![Build Status](https://travis-ci.org/nawaman/defaultj.svg?branch=master)](https://travis-ci.org/nawaman/defaultj)

## NOTE: This README.md is a work in progress.

DefaultProvider is a super simple but powerful dependency injection utility.
It can be used in many different way and many level of coupling to the code.
Inspired by Guice, but DefaultProvider try its best to decoupling itself from the code that use it.

```
  +--------+                             +--------+
  | Client | ---> (DefaultProvider) ---> | Source |
  +--------+                             +--------+
```

## Level 0 - The source has no coupling to DefaultProviderThe source can have not mention, no dependency the DefaultProvider at all.
When the Client ask DefaultProvider for an object of Source,
  DefaultProvider will look for any clue to create the object.

## Level 1 - The source can have matched-name annotations
DefaultProvider looks for clues in the source.
The source can use annotations to add more clue.
The annotations, however, does not need to be the ones packaged with DefaultProvider.
DefaultProvider will look for annotation with matched-names instead of the actual class.
This means that the Source can define and use its own annotations
  as long as the simple name matched what DefaultProvider expect,
  DefaultProvider will use it as the clues.

## Level 2 - The source can ask for Object by only depends on DefaultProvider API.
To accomplished its work the Source might need to ask for some objects from DefaultProvider.
DefaultProvider is designed so that a piece of code can depends in its API

