import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object FlaggingFromFile {
  val dataFilePath = "D:\\data\\Toy\\toy.csv"
  val flagFilePath = "D:\\data\\Toy\\flag.csv"

  val sc = new SparkConf().setMaster("local[*]").setAppName("Flagging")
  val ss = SparkSession.builder().config(sc).config("spark.local.dir", "D:/tmp").config("spark.driver.memory", "10g").getOrCreate()


}
