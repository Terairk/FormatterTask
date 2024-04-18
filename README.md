# Formatter Task

## Project Description
This is my submission for the "**A basic parser and a formatter for F# language in Kotlin**" task for


# Setup
You may need to install the Kotest plugin if you're using IntelliJ IDEA to run the tests from File/IntelliJ (there should be a view for it in IntelliJ)
Alternatively, you can right click the test folder and hit run tests.
You should also be able to do ./gradlew clean test to see verbose tests.

You may need to reload gradle changes in build.gradle.kts (but should be done automatically). I didn't make a main application however the tests should be able to showcase the features off.

## Usage
Please read the comments at the top of the Main.kt file for some examples. Also you can consult the test cases if you're unsure about what indices are considered valid or not.

## Some Sidenotes
I didn't quite understand the assignment. My program makes you specify ranges from the original text to replace (where it only replaces whitespace characters). These whitespace changes are done lazily and are fixed in place ie
The ranges you specify (to delete or insert characters) all belong to the same original text. (Look at my tests for an example of what I mean)
If you tried to do this without these changes: Your indexes would start being off as you're repeatedly deleting and inserting thus changing
the ranges. So this makes the program more intuitive to use.

However, I'm not fully sure about my PositionInResult. You never have to specify change yourself which is nice and intuitive to use.
But from a programmer's perspective: It makes it quite weird to reason about it.
I certainly had a lot of problems reasoning about it. I always thought that since change is in the constructor, I had to use it in my testing.
I also had to include another offset in addition to the position.
There's probably a more efficient way to do this but I couldn't quite do exactly what the problem statement said to do.
Overall, I think my solution could be improved in my head (when reasoning about the problem), I felt that you needed to convert from
the resultTextPosition to the originalTextPosition and vice versa.

## What I could've done to improve on this project
- I've recently learnt some Compose Multiplatform so I could try add a GUI or a CLI where the positions are underneath the original text.
- Then you would specify the indices or ranges in a much obvious way rather than counting manually
- Add proper error handling

## Frameworks
- **Kotest**: Popular testing framework for Kotlin that supports property testing and DSL's for writing tests.
- **Detekt**: Popular library for static code analysis
- **Arrow**: I originally planned to use Arrow but ran out of time.

## Reasoning for Framework Choices
- **Kotest**:
  - I've used JUnit for creating tests so I wanted to try out some Kotlin- first-testing frameworks
  - Kotest supports DSL-based testing which I wanted to try out (and get more comfortable with using DSL's in Kotlin) - the syntax is also really intuitive
  - Kotest supports property-based testing so I can test multiple strings easily (Didn't end up using this)
  - Kotest easily supports Arrow's custom datatypes like Either<A,B> (Didn't end up using Arrow in the end due to this being more difficult than I thought)
    I wanted to broaden my horizon of using more Kotlin frameworks so I could get used to the Kotlin Ecosystem of libraries
  - (After the project) - I realised that I really liked being able to give my tests better names/descriptions since they were a string instead of a FunctionName
  - Allows me to use whitespace in test names which is nice for readability

- ** Arrow
  I originally wanted to use Arrow for some error handling but couldn't due to spending too much time getting stuck
- Planned to use this to catch multiple errors at the same time and collect them at the end

## TLDR;
I'm using this project as a form of testing ground so I can get more used to using 3rd party
frameworks in my Kotlin code (as I haven't properly used Arrow before).
