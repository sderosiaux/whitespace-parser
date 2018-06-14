package com.sderosiaux.whitespace

import org.scalatest.{FlatSpec, Matchers}
import cats.implicits._

class WhitespaceInterpreterTest extends FlatSpec with Matchers {
  "The whitespace parser" should "parse Hello, world!" in {
    val helloWorld = "   \t  \t   \n\t\n     \t\t  \t \t\n\t\n     \t\t \t\t  \n\t\n     \t\t \t\t  \n\t\n     \t\t \t\t\t\t\n\t\n     \t \t\t  \n\t\n     \t     \n\t\n     \t\t\t \t\t\t\n\t\n     \t\t \t\t\t\t\n\t\n     \t\t\t  \t \n\t\n     \t\t \t\t  \n\t\n     \t\t  \t  \n\t\n     \t    \t\n\t\n  \n\n\n"
    val Some((_, output)) = new WhitespaceInterpreter[Option].eval(helloWorld)
    output shouldBe "Hello, world!"
  }
}
