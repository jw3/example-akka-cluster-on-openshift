package example.seeds

import akka.actor.ActorSystem
import akka.cluster.Cluster


object Boot extends App {
  implicit val system = ActorSystem("seed-1")
  val cluster = Cluster(system)

  println("boom")
}
