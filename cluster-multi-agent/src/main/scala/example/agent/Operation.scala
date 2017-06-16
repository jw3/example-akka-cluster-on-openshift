package example.agent

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsonFormat}


trait Operation {
  def lhs: Int
  def rhs: Int
}

trait OpResult {
  def res: Int
}

object OpResult extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val format = new JsonFormat[OpResult] {
    import spray.json._

    val SumName = fname[Adder.Sum]
    val DiffName = fname[Subtractor.Difference]
    val ProdName = fname[Multiplier.Product]
    val QuotName = fname[Divider.Quotient]

    override def write(obj: OpResult): JsValue = obj match {
      case o: Adder.Sum ⇒ JsObject(SumName → o.toJson)
      case o: Subtractor.Difference ⇒ JsObject(DiffName → o.toJson)
      case o: Multiplier.Product ⇒ JsObject(ProdName → o.toJson)
      case o: Divider.Quotient ⇒ JsObject(QuotName → o.toJson)
    }

    override def read(json: JsValue): OpResult =
      json.asJsObject.fields.toSeq match {
        case Seq((SumName, js)) ⇒ js.convertTo[Adder.Sum]
        case Seq((DiffName, js)) ⇒ js.convertTo[Subtractor.Difference]
        case Seq((ProdName, js)) ⇒ js.convertTo[Multiplier.Product]
        case Seq((QuotName, js)) ⇒ js.convertTo[Divider.Quotient]
        case _ ⇒ throw new RuntimeException(s"unable to deserialize OpResult: $json")
      }
  }

  private def fname[T: Manifest]: String = manifest[T].runtimeClass.getSimpleName
}
