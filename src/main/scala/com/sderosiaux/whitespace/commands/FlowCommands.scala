package com.sderosiaux.whitespace.commands

import cats.{MonadError, SemigroupK}
import com.sderosiaux.parser.Parser
import com.sderosiaux.whitespace.WhitespaceParser
import cats.implicits._

class FlowCommands[F[_]: MonadError[?[_], Unit]: SemigroupK] extends Commands[F] {
  override def trigger = Parser.lf[F]
  override def commands = endProgram

  def endProgram: WhitespaceParser[F] = for {
    _ <- Parser.lf[F] *> Parser.lf[F]
  } yield ""
}
