package com.sderosiaux.whitespace.commands

import cats.{MonadError, SemigroupK}
import cats.data.StateT
import com.sderosiaux.whitespace.Stack
import cats.implicits._

abstract class Commands[F[_]: MonadError[?[_], Unit]: SemigroupK] {
  def all = trigger >> commands
  def trigger: StateT[F, (String, Stack), Unit]
  def commands: StateT[F, (String, Stack), String]
}
