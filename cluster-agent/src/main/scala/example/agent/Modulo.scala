package example.agent

import akka.actor.{Actor, ActorLogging, Props}
import example.api.{Mod, Remainder}


object Modulo {
  def props() = Props(new Modulo)
}

class Modulo extends Actor with ActorLogging {
  def receive = {
    case Mod(l, r) ⇒
      log.info("executing: {} % {}", l, r)
      sender ! Remainder(l % r)
  }
}
