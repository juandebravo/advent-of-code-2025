import cats.effect.{IO, IOApp}
import cats.implicits._
import cats.effect.kernel.Ref
import fs2.text
import fs2.io.file.{Files, Path}

case class Instruction(direction: Char, steps: Int)

case class Range(low: Long, high: Long)

object Main extends IOApp.Simple:

  def run: IO[Unit] =
    for
      value1 <- calculateDay1("src/main/resources/day1_input.txt")
      _ <- IO.println(value1)
      value2 <- calculateDay2("src/main/resources/day2_input.txt")
      _ <- IO.println(value2)
    yield ()
  
  private def buildInstructions(contents: IO[List[(Char, Int)]]): IO[List[Instruction]] =
    contents.map(_.map((direction, steps) => Instruction(direction, steps % 100)))

  def move(value: Ref[IO, Int], total: Ref[IO, Int], instruction: Instruction): IO[Unit] = {
    def getNewValueFromInstruction(current: Int, instruction: Instruction): Int =
      val newValue = if instruction.direction == 'R' then current + instruction.steps else current - instruction.steps
      if newValue < 0 then 100 + newValue else if newValue >= 100 then newValue - 100 else newValue

    for
      newValue <- value.updateAndGet: current =>
        getNewValueFromInstruction(current, instruction)
      _ <- if newValue == 0 then total.update(_ + 1) else IO.unit
      total <- total.get
    yield ()
  }

  def calculateDay1(input: String): IO[Int] =
    for
      contents <- buildInstructions(readDay1File(input))
      value <- Ref.of[IO, Int](50)
      total <- Ref.of[IO, Int](0)
      _ <- contents.traverseVoid(instruction => move(value, total, instruction))
      result <- total.get
    yield result
    
  private def readDay1File(input: String): IO[List[(Char, Int)]] =
    Files[IO]
      .readAll(Path(input))
      .through(text.utf8.decode)
      .through(text.lines)
      .collect:
        case line if line.nonEmpty => (line.head, line.tail.toInt)
      .compile
      .toList

  def calculateDay2(input: String): IO[Long] =
    for
      contents <- readDay2File(input)
      total <- Ref.of[IO, Long](0)
      _ <- contents.traverseVoid(content => findInvalidIds(content, total))
      value <- total.get
    yield (value)

  private def findInvalidIds(content: Range, total: Ref[IO, Long]): IO[Unit] =
    val invalid = (content.low to content.high).filter(isInvalidId)
    total.update(_ + invalid.sum)

  private def isInvalidId(input: Long): Boolean = 
    val digits = input.toString
    val (firstHalf, secondHalf) = digits.splitAt(digits.length / 2)
    firstHalf === secondHalf
  
  private def readDay2File(input: String): IO[List[Range]] = {

    def buildRange(input: String): Range =
      input.trim.split("-") match {
        case Array(l, h) => Range(l.trim.toLong, h.trim.toLong)
        case _ => throw new IllegalArgumentException(s"Invalid range: $input")
      }

    Files[IO]
      .readAll(Path(input))
      .through(text.utf8.decode)
      .compile
      .string
      .map(_.split(",").map(buildRange).toList)
  }