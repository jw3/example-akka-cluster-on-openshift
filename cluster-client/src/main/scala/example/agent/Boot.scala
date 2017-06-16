package example.agent

import akka.actor.{ActorPath, ActorSystem}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}


object Boot extends App {
  val clusterName = "ClusterSystem"

  implicit val system = ActorSystem("cluster-client")

  val initialContacts = Set(
    ActorPath.fromString(s"akka.tcp://$clusterName@seed-1:2551/system/receptionist"),
    ActorPath.fromString(s"akka.tcp://$clusterName@seed-2:2551/system/receptionist")
  )

  val c = system.actorOf(ClusterClient.props(
    ClusterClientSettings(system).withInitialContacts(initialContacts)
  ), "client")

  c ! ClusterClient.Send("/user/add", "hello from cluster-client", localAffinity = true)
}
