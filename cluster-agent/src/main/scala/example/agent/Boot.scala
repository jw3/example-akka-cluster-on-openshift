package example.agent

import akka.actor.{ActorSystem, Address}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.stream.ActorMaterializer


object Boot extends App {
  val clusterName = "ClusterSystem"

  implicit val system = ActorSystem(clusterName)
  implicit val materializer = ActorMaterializer()

  val cluster = Cluster(system)

  system.settings.config.getString("clustering.seeds").split(",").foreach { seed ⇒
    println(s"worker is joining seed $seed")
    cluster.join(
      new Address("akka.tcp", clusterName, seed, 2551)
    )
  }

  val echo = system.actorOf(EchoAgent.props(), "echo")
  ClusterClientReceptionist(system).registerService(echo)

  val modulo = system.actorOf(Modulo.props(), "mod")
  ClusterClientReceptionist(system).registerService(modulo)
}
