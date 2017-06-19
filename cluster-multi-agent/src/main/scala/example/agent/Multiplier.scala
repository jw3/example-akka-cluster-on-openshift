package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.api.{Multiply, Product}

object Multiplier {
  def props() = Props(new Adder)
}

class Multiplier extends Actor with ActorLogging {
  def receive = {
    case Multiply(l, r) ⇒ sender ! Product(l * r)
  }
}
