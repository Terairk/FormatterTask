# Formatter Task

## Project Description
This is my submission for the "**A basic parser and a formatter for F# language in Kotlin**" task for the Jetbrains Internship Application
You may need to install the Kotest plugin if you're using IntelliJ IDEA to run the tests. (Although I think it should work without it) 

## Some Sidenotes
I didn't quite fully understand the assignment. The way I ended up doing it in the end (along with my testing) is that you specify ranges
from the original text to replace (only replacing whitespace characters). These whitespace changes are done lazily and are like fixed in place ie 
The ranges you specify (to delete or insert characters) all belong to the same original text. (Look at my tests for example of what I mean)
If you tried to do this without these changes: Your indexes would start being off as you're repeatedly deleting and inserting thus changing
the ranges. So this makes the program more intuitive to use.

However, I'm not fully sure about my PositionInResult. You never have to specify change yourself which is nice and intuitive to use. 
But from a programmer's perspective: It makes it quite weird to reason about it.
I certainly had a lot of problems reasoning about it. I always thought that since change is in the constructor, I had to use it in my testing.
I also had to include another offset in addition to position.
There's probably a more efficient way to do this but I couldn't quite do exactly what the problem statement said to do.
Overall I think my solution could be improved like in my head (when reasoning about the problem), I felt that you needed to convert from
the resultTextPosition to the originalTextPosition and vice versa.

## Frameworks 
- **Kotest**: Popular testing framework for Kotlin that supports property testing and DSL's for writing tests.
- **Detekt**: Popular library for static code analysis
- **Arrow**: Originally planned to use Arrow but ran out of time.

## Reasoning for Framework Choices
- **Kotest**: 
  - I've used JUnit for creating tests so I wanted to try out some Kotlin first testing frameworks
  - Kotest supports DSL based testing which I wanted to try out (and get more comfortable to using DSL's in Kotlin) - the syntax is also really intuitive
  - Kotest supports property based testing so I can test multiple strings easily (Didn't end up using this)
  - Kotest easily supports Arrow's custom datatypes like Either<A,B> (Didn't end up using Arrow in the end due to this being more difficult than I thought)
  - Wanted to broaden my horizon of using more Kotlin frameworks so I can get used to the Kotlin Ecosystem of libraries
  - (After the project) - I realised that I really liked being able to give my tests better names/descriptions since they were a string instead of a FunctionName
  - Allows me to use whitespace in test names which is nice for readability

 - ** Arrow
  - Originally wanted to use Arrow for some error handling but couldn't due to spending too much time getting stuck
  - Planned to use this to catch multiple errors at the same time and collect them at the end

## TLDR;
I'm using this project as a form of testing ground so I can get more used to using 3rd party
frameworks in my Kotlin code (as I haven't properly used Arrow before).


