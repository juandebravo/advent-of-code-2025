import cats.effect.IO
import weaver._

object AdventOfCode2025 extends SimpleIOSuite:

  val day1Result = 1021
  val day2Result = 44487518055L

  test("make sure IO computes the right result"):
    IO.pure(1).map(_ + 2).map: result =>
      expect.eql(result, 3)

  test(s"day 1 should return $day1Result"):
    Main.calculateDay1("src/main/resources/day1_input.txt").map: result =>
      expect.eql(result, day1Result)
      
  test(s"day 2 should return $day2Result"):
    Main.calculateDay2("src/main/resources/day2_input.txt").map: result => 
      expect.eql(result, day2Result)