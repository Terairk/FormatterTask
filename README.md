# Formatter Task

## Project Description
This is my submission for the "**A basic parser and a formatter for F# language in Kotlin**" task for the Jetbrains Internship Application.

## Frameworks 
- **Kotest**: Popular testing framework for Kotlin that supports property testing and DSL's for writing tests.
- **Arrow**: Popular functional programming library that allows for functional error handling with nice DSL's.

## Reasoning for Framework Choices
- **Kotest**: 
  - I've used JUnit for creating tests so I wanted to try out some Kotlin first testing frameworks
  - Kotest supports DSL based testing which I wanted to try out (and get more comfortable to using DSL's in Kotlin)
  - Kotest supports property based testing so I can test multiple strings easily
  - Kotest easily supports Arrow's custom datatypes like Either<A,B>
  - Wanted to broaden my horizon of using more Kotlin frameworks so I can get used to the Kotlin Ecosystem of libraries

- **Arrow**: 
  - Having done Haskell in Imperial (before learning Kotlin),
  - I like how Arrow has support for some custom datatypes and has functions that allow for something similar to the do block in Haskell
  - I decided to read more about Arrow after that and I loved how Arrow handles Error handling and propagation.
  - Has the benefits of having more control about the control flow about programs compared to Kotlin's unchecked exceptions

## TLDR;
I'm using this project as a form of testing ground so I can get more used to using 3rd party
frameworks in my Kotlin code (as I haven't properly used Arrow before).

I may implement this code using regular Exceptions with tests but for now, I'm opting to use Arrow's error handling and see how it goes.
