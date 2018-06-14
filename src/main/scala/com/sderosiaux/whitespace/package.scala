package com.sderosiaux

import cats.data.StateT

package object whitespace {
  type Stack = List[Int]
  type WhitespaceParser[F[_]] = StateT[F, (String, Stack), String]
}
