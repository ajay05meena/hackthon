package app.doc.example

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def run(): Unit = {
    val sparkConf = new SparkConf()
    val sparkContext = new SparkContext("spark://192.168.1.5:7077", "Word Count", sparkConf)
    val input = sparkContext.textFile("file:////Users/ajay.meena/Desktop/textFileToMine.txt")
    val itemsCount = input.count()
    val firstItem = input.first()
    val words = input.flatMap(line => line.split(" ")).collect()
    val wordCount = words.length
    val wordWithSherlock = words.count(w => w.equalsIgnoreCase("Sherlock"))
    ///val count = input.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey( _ + _)
    println()
    println(s"Total items ${itemsCount}, first item ${firstItem}, total words ${wordCount}, words with sherlock ${wordWithSherlock}")
    println()
   /// Data frames
   val input2 = sparkContext.textFile("file:////Users/ajay.meena/Desktop/textFileToMine.txt")
   val biggestLine = input2.map(line => line.size).reduce((a, b) => Math.max(a, b))
   val wordsCount = input2.flatMap(line => line.split(" ")).groupBy(x => x)
   println()
   println(s"Biggest line length is ${biggestLine}")
   println(s"freq ${wordsCount}")
  }
}
