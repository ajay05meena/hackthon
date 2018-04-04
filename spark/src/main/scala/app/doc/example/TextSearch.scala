package app.doc.example

import org.apache.spark.{SparkConf, SparkContext}


object TextSearch {

  def run():Unit = {
      val sparkConf = new SparkConf()
      val sparkContext = new SparkContext("spark://192.168.1.5:7077", "Text Search", sparkConf)
      val input = sparkContext.textFile("file:///Users/ajay.meena/Desktop/myrepo/hackthon/spark/build.sbt")
      val word = "you"
      val count = input.flatMap(line => line.split(" ")).filter(w => w.equalsIgnoreCase(word)).count()
      println(s"word ${word} occurred  ${count} times")
    }


}
