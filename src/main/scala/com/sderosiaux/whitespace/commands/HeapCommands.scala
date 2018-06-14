package com.sderosiaux.whitespace.commands

import cats.{MonadError, SemigroupK}
import com.sderosiaux.parser.Parser
import cats.implicits._

class HeapCommands[F[_]: MonadError[?[_], Unit]: SemigroupK] extends Commands[F] {
  override def trigger = Parser.tab[F] *> Parser.tab[F]
  override def commands = ???
}
