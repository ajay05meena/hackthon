package app

import app.doc.example.{PI, TextSearch, WordCount}


object Application {

  def main(args: Array[String]): Unit = {
    println("Hello spark")
    WordCount.run()
    //PI.run()
    //TextSearch.run()
  }

}
