package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.api.{Add, Sum}

object Adder {
  def props() = Props(new Adder)
}

class Adder extends Actor with ActorLogging {
  def receive = {
    case Add(l, r) ⇒
      log.info("executing: {} + {}", l, r)
      sender ! Sum(l + r)
  }
}
