package example.agent

import akka.actor.{Actor, ActorLogging, Props}

object EchoAgent {
  def props() = Props(new EchoAgent)
}

class EchoAgent extends Actor with ActorLogging {
  def receive = {
    case s: String ⇒
      sender ! s"""Echo says: "$s""""
  }
}
