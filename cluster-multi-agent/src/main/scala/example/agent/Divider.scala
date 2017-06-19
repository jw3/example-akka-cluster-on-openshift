package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.api.{Divide, Quotient}


object Divider {
  def props() = Props(new Adder)
}

class Divider extends Actor with ActorLogging {
  def receive = {
    case Divide(l, r) ⇒ sender ! Quotient(l / r)
  }
}
