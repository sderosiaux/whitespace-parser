# Whitespace parser

Example of a parser implementation of the Whitespace programming language using only `StateT` to convey the state, the stack and the output of the program.

Whitespace deals with 3 characters only: `" "`, ``"\t"`, and `"\n"`.

This parser relies massively upon `StateT` and misc combinators, as we can see in the root parser:

```scala
def imp: StateT[F, (String, Stack), String] = for {
   output <- stackCommands.all <+>
     ioCommands.all <+>
     arithmeticCommands.all <+>
     heapCommands.all <+>
     flowCommands.all
   rest <- imp <+> StateT.pure[F, (String, Stack), String]("")
} yield output + rest
```

The state of our parser contains the program and the stack of execution. The result is a `String` which is what is print to the screen.

Whitespace has 5 big chunks of commands to determine how to interpret the following characters.

For instance, one of the `ioCommands` (triggered by `"\t\n"`) is `"  "` which means: _print the character on top of the stack_.

## Hello, world!

As a typical example, here we are parsing the Hello world from [Wikipedia](https://en.wikipedia.org/wiki/Whitespace_(programming_language)):

```scala
val helloWorld = "   \t  \t   \n\t\n     \t\t  \t \t\n\t\n     \t\t \t\t  \n\t\n     \t\t \t\t  \n\t\n     \t\t \t\t\t\t\n\t\n     \t \t\t  \n\t\n     \t     \n\t\n     \t\t\t \t\t\t\n\t\n     \t\t \t\t\t\t\n\t\n     \t\t\t  \t \n\t\n     \t\t \t\t  \n\t\n     \t\t  \t  \n\t\n     \t    \t\n\t\n  \n\n\n"
val Some(((rest, stack), output)) = new WhitespaceParser[Option].eval(helloWorld)
println(s"Stack: $stack")
println(s"Output: $output")
```

```
Stack: List(33, 100, 108, 114, 111, 119, 32, 44, 111, 108, 108, 101, 72)
Output: Hello, world!
```

## Syntax

Details about the syntax here: https://hackage.haskell.org/package/whitespace-0.4/src/docs/tutorial.html
