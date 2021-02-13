package todo

import akka.testkit.{TestKit, ImplicitSender}
import akka.actor.ActorSystem
import org.specs2.mutable.SpecificationLike
import org.specs2.specification.AfterAll
import akka.actor.Props
import todo.TodoActor
import akka.testkit.TestProbe
import scala.concurrent.duration._

class TodoActorSpec extends TestKit(ActorSystem("TodoActorSpec"))
  with ImplicitSender
  with SpecificationLike
  with AfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  sequential

  "Todo" should {
    "list todos when asked and no todos are added" in {
      var todo: TodoActor = null
      val props = Props {
        val t = new TodoActor()
        todo = t
        t
      }

      val actor = system.actorOf(props)

      actor ! TodoActor.GetTodos

      expectMsg(List.empty)
    }
  }
}
