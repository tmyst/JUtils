
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession, functions => SF}
import org.apache.spark.sql.catalyst.plans.{Cross, FullOuter, Inner, LeftOuter, LeftSemi, RightOuter}
import scala.util.matching.Regex.Match

object FlaggingFromFile extends App {
  val dataFilePath = "D:\\data\\Toy\\toy.csv"
  val flagFilePath = "D:\\data\\Toy\\flag2.csv"
  val flagColumnInitial = "value"
  val flagColumnOutcome = "value2"
  val flagColumnAfterTreat = "value3"
  val flagColumnBadCondition = "value4"
  val fillNaMap = Map("value" -> "無", "value2" -> 0, "value3" -> "2014-04-09", "value4" -> "FALSE")

  sealed abstract class Calc(val idx:Int, val str:String)
  case object min extends Calc(0, "min")
  case object max extends Calc(1, "max")
  case object mean extends Calc(2, "mean")

  val sc = new SparkConf().setMaster("local[*]").setAppName("Flagging")
  val ss = SparkSession.builder().config(sc).config("spark.local.dir", "D:/tmp").config("spark.driver.memory", "10g").getOrCreate()

  val df = ss.read.option("header", "true").csv(dataFilePath)
  val fl = ss.read.option("header", "true").option("encoding", "windows-31j").csv(flagFilePath)

  val joinCols = Seq("p")

  def replaceSymbols = "[(|)|,| ]".r replaceSomeIn ( _: String, {
    case Match("(") => Some("_")
    case Match(")") => Some("")
    case Match(",") => Some("_")
    case Match(" ") => Some("")
    case _ => None }
  )

  def replaceColumnStrings(dataFrame: DataFrame): DataFrame = {
    dataFrame.select(dataFrame.columns.map(x => col(x).as(replaceSymbols(x))): _*)
  }

  val newDf = df.join(fl, joinCols, joinType = LeftOuter.toString).na.fill(fillNaMap).
    groupBy(col("p").substr(1,1)).agg(Map("g" -> min.str, "value3" -> max.str)).
    transform(replaceColumnStrings)

  newDf.show(1000)

}
