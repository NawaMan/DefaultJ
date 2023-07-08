# Default to Null
`@DefaultToNull` tell DefaultJ to use `null` as a default of the class.
It is true that DefaultJ will return `null` when it could not find a proper default anyway.
`@DefaultToNull` stop DefaultJ from going to the whole process to find a way to get the default
    as well as to not fall into using constructors.
