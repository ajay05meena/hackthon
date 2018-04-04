name := "spark"

version := "1.0"

scalaVersion := "2.11.1"

//val sparkVersion = "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" %   "2.1.0"
resolvers += Resolver.mavenLocal