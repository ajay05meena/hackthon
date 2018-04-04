package app.doc.example

import org.apache.spark.{SparkConf, SparkContext}


object PI {

  def run():Unit = {
    val sparkConf = new SparkConf().set("spark.cores.max", "4")
    val sparkContext = new SparkContext("spark://192.168.1.5:7077", "PI Calculation", sparkConf)
    val NUMBER_OF_SAMPLE = 10000000
    val count = sparkContext.parallelize(1 to NUMBER_OF_SAMPLE).filter{ _ =>
      val x = Math.random
      val y = Math.random
      x*x + y*y < 1
    }.count()
    println(s"PI is roughly ${4 * count/NUMBER_OF_SAMPLE}")
  }

}
