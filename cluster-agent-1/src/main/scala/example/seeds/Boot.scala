package example.seeds

import akka.actor.{ActorSystem, Address}
import akka.cluster.Cluster
import akka.stream.ActorMaterializer


object Boot extends App {
  val clusterName = "ClusterSystem"

  implicit val system = ActorSystem(clusterName)
  implicit val materializer = ActorMaterializer()

  val cluster = Cluster(system)

  system.settings.config.getString("clustering.seeds").split(",").foreach { seed ⇒
    println(s"worker-1 is joining seed $seed")
    cluster.join(
      new Address("akka.tcp", clusterName, seed, 2551)
    )
  }
}
