package example.agent

import akka.actor.{ActorPath, ActorSystem}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.duration.DurationInt


object Boot extends App {
  val clusterName = "ClusterSystem"

  implicit val system = ActorSystem("cluster-client")
  implicit val mat = ActorMaterializer()

  val initialContacts = Set(
    ActorPath.fromString(s"akka.tcp://$clusterName@seed-1:2551/system/receptionist"),
    ActorPath.fromString(s"akka.tcp://$clusterName@seed-2:2551/system/receptionist")
  )

  val c = system.actorOf(ClusterClient.props(
    ClusterClientSettings(system).withInitialContacts(initialContacts)
  ), "client")

  implicit val timeout = Timeout(10 seconds)
  HttpInterface.up(c, "0.0.0.0", 9000)
}
