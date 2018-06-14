package com.sderosiaux.whitespace

import cats.data.StateT
import cats.effect.IO
import cats.{Eval, MonadError, SemigroupK}
import com.sderosiaux.parser.Parser
import com.sderosiaux.whitespace.commands._
import cats.implicits._

import scala.util.Try

class WhitespaceParser[F[_]: MonadError[?[_], Unit]: SemigroupK] {

  val stackCommands = new StackManipulationCommands[F]
  val ioCommands = new IOCommands[F]
  val arithmeticCommands = new ArithmeticCommands[F]
  val heapCommands = new HeapCommands[F]
  val flowCommands = new FlowCommands[F]

  def imp: StateT[F, (String, Stack), String] = for {
    output <- stackCommands.all <+>
      ioCommands.all <+>
      arithmeticCommands.all <+>
      heapCommands.all <+>
      flowCommands.all
    rest <- imp <+> StateT.pure[F, (String, Stack), String]("")
  } yield output + rest

  def eval(s: String): F[((String, Stack), String)] = imp.run((s, List[Int]()))

}

object WhitespaceParserApp extends App {
  val helloWorld = "   \t  \t   \n\t\n     \t\t  \t \t\n\t\n     \t\t \t\t  \n\t\n     \t\t \t\t  \n\t\n     \t\t \t\t\t\t\n\t\n     \t \t\t  \n\t\n     \t     \n\t\n     \t\t\t \t\t\t\n\t\n     \t\t \t\t\t\t\n\t\n     \t\t\t  \t \n\t\n     \t\t \t\t  \n\t\n     \t\t  \t  \n\t\n     \t    \t\n\t\n  \n\n\n"
  val Some(((rest, stack), output)) = new WhitespaceParser[Option].eval(helloWorld)
  println(s"Stack: $stack")
  println(s"Output: $output")
}
