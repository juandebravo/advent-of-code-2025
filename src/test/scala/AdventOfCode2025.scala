import cats.effect.IO
import weaver._

object AdventOfCode2025 extends SimpleIOSuite:

  val day1Result = 1021

  test("make sure IO computes the right result"):
    IO.pure(1).map(_ + 2).map: result =>
      expect.eql(result, 3)

  test(s"day 1 should return $day1Result"):
    Main.calculateDay1("src/main/resources/day1_input.txt").map: result =>
      expect.eql(result, day1Result)