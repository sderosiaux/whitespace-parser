package com.sderosiaux.whitespace.commands

import cats.{MonadError, SemigroupK}
import cats.data.StateT
import com.sderosiaux.parser.Parser
import com.sderosiaux.whitespace.{Stack, WhitespaceParser}
import cats.implicits._

class StackManipulationCommands[F[_]: MonadError[?[_], Unit]: SemigroupK] extends Commands[F] {
  override def trigger = Parser.spacy[F]
  override def commands = pushNumber <+> duplicate <+> swap <+> discard

  def pushNumber: WhitespaceParser[F] = for {
    _ <- Parser.spacy[F]
    sign <- Parser.spacy[F].as(1) <+> Parser.tab[F].as(-1)
    n <- Parser.many[F, String](Parser.spacy[F].as("0") <+> Parser.tab[F].as("1")).map { x => Integer.parseInt(x.mkString, 2) }
    _ <- Parser.lf[F]
    value = sign * n
    _ <- StateT.modify[F, (String, Stack)]{ case (program, stack) => (program, value +: stack) }
  } yield ""

  def duplicate: WhitespaceParser[F] = for {
    _ <- Parser.lf[F] *> Parser.spacy[F]
    _ <- StateT.modify[F, (String, Stack)](s => (s._1, s._2.head +: s._2))
  } yield ""

  def swap: WhitespaceParser[F] = for {
    _ <- Parser.lf[F] *> Parser.tab[F]
    _ <- StateT.modify[F, (String, Stack)]{ case (program, stack) => (program, stack match {
      case head :: head2 :: rest => head2 +: head +: rest
      case _ => stack // TODO(sd): errors are not handled
    }) }
  } yield ""

  def discard: WhitespaceParser[F]  = for {
    _ <- Parser.lf[F] *> Parser.lf[F]
    _ <- StateT.modify[F, (String, Stack)]{ case (program, stack) => (program, stack.tail) }
  } yield ""
}
