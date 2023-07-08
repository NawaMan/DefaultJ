# Known Null Value

Known null value strategy returns a sensible non-new default for a set of class.
DefaultJ simply delegate this strategy to NullableJ KnownNullValuesFinder.

Generally:
  - all number classes will have 0 as a default value.
  - Space is used for `Character` and `char`.
  - Empty string is used for `String` and `CharSequence`.
  - `false` for `boolean` and `Boolean`.
  - Empty collections for abstract collection class.
  - Noop or return null for function interfaces.
  - Empty array for array classes.
