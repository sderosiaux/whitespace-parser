package com.sderosiaux.parser

import cats.{MonadError, SemigroupK}
import cats.data.StateT
import com.sderosiaux.whitespace.{Stack, WhitespaceParser}
import cats.implicits._

object Parser {

  def spacy[F[_]: MonadError[?[_], Unit]: SemigroupK] = exactChar[F](' ').as(())
  def tab[F[_]: MonadError[?[_], Unit]: SemigroupK] = exactChar[F]('\t').as(())
  def lf[F[_]: MonadError[?[_], Unit]: SemigroupK] = exactChar[F]('\n').as(())

  def oneChar[F[_]: MonadError[?[_], Unit]: SemigroupK]: WhitespaceParser[F] = for {
    current <- StateT.get[F, (String, Stack)]
    (c, _) = current
    _ <- if (c.nonEmpty)
      StateT.modify[F, (String, Stack)]{ case (program, stack) => (program.tail, stack) }
    else {
      ().raiseError[StateT[F, (String, Stack), ?], Unit]
    }
  } yield c.head.toString

  private def matchChar[F[_]: MonadError[?[_], Unit]: SemigroupK](p: Char => Boolean): WhitespaceParser[F] = for {
    char <- oneChar[F]
    _ <- if (p(char.head))
      StateT.pure[F, (String, Stack), String](char)
    else
      ().raiseError[StateT[F, (String, Stack), ?], Unit]
  } yield char

  def exactChar[F[_]: MonadError[?[_], Unit]: SemigroupK](c: Char): WhitespaceParser[F] = matchChar[F](p => p == c)

  def many[F[_]: MonadError[?[_], Unit]: SemigroupK, A](s: StateT[F, (String, Stack), A]): StateT[F, (String, Stack), List[A]] = for {
    head <- s
    tail <- many(s) <+> StateT.pure[F, (String, Stack), List[A]](List.empty[A])
  } yield head :: tail
}
