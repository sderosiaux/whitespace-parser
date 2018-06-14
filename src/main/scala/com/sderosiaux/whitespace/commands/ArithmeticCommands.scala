package com.sderosiaux.whitespace.commands

import cats.{MonadError, SemigroupK}
import cats.data.StateT
import com.sderosiaux.parser.Parser
import com.sderosiaux.whitespace.{Stack, WhitespaceParser}
import cats.implicits._

class ArithmeticCommands[F[_]: MonadError[?[_], Unit]: SemigroupK] extends Commands[F] {
  override def trigger = Parser.tab[F] *> Parser.spacy[F]
  override def commands = addition

  private def replaceWithSum(stack: Stack) = stack match {
    case a :: b :: rest => (a + b) +: rest
    case _ => stack // TODO(sd): errors are not handled
  }

  def addition: WhitespaceParser[F] = for {
    _ <- Parser.spacy[F] *> Parser.spacy[F]
    _ <- StateT.modify[F, (String, Stack)](s => (s._1, replaceWithSum(s._2)))
  } yield ""
}
