package example.agent

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.client.ClusterClient
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import example.api._


object HttpInterface extends HttpInterface {
  def up(ref: ActorRef, host: String, port: Int)(implicit system: ActorSystem, mat: ActorMaterializer, timeout: Timeout) = {
    import system.dispatcher
    Http().bindAndHandle(routes(ref), host, port)
  }
}

trait HttpInterface extends LazyLogging {
  import StatusCodes._
  import akka.http.scaladsl.server.Directives._
  import akka.pattern.ask


  def routes(c: ActorRef)(implicit timeout: Timeout): Route = {
    pathPrefix("api") {
      path("op" / IntNumber / Segment / IntNumber) { (lhs, op, rhs) ⇒
        val f: (Int, Int) ⇒ Operation = op match {
          case "add" ⇒ Add.apply
          case "sub" ⇒ Subtract.apply
          case "mul" ⇒ Multiply.apply
          case "div" ⇒ Divide.apply
          case "mod" ⇒ Mod.apply
        }

        logger.debug(s"received op request [$lhs $op $rhs]")

        val r = c ? ClusterClient.Send(s"/user/$op", f(lhs, rhs), localAffinity = false)
        onSuccess(r) {
          case ec: StatusCode ⇒ complete(ec)
          case r: OpResult ⇒
            logger.debug(s"received result of op($lhs $op $rhs) --> $r")
            complete(OK → r.toString)
        }
      }
    }
  }
}
