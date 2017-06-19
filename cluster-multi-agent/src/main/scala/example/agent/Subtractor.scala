package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.api.{Difference, Subtract}

object Subtractor {
  def props() = Props(new Adder)
}

class Subtractor extends Actor with ActorLogging {
  def receive = {
    case Subtract(l, r) ⇒
      log.info("executing: {} - {}", l, r)
      sender ! Difference(l - r)
  }
}
