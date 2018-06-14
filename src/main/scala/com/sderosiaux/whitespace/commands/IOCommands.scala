package com.sderosiaux.whitespace.commands

import cats.{MonadError, SemigroupK}
import cats.data.StateT
import com.sderosiaux.parser.Parser
import com.sderosiaux.whitespace.Stack
import cats.implicits._

class IOCommands[F[_]: MonadError[?[_], Unit]: SemigroupK] extends Commands[F] {
  override def trigger = Parser.tab[F] *> Parser.lf[F]
  override def commands = printChar <+> printNumber

  def printChar: StateT[F, (String, Stack), String] = for {
    s <- StateT.get[F, (String, Stack)]
    (_, stack) = s
    _ <- Parser.spacy[F] *> Parser.spacy[F]
  } yield stack.head.toChar.toString

  def printNumber: StateT[F, (String, Stack), String] = for {
    s <- StateT.get[F, (String, Stack)]
    (_, stack) = s
    _ <- Parser.spacy[F] *> Parser.tab[F]
  } yield stack.head.toString
}
