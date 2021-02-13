package todo
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props

case class Todo(todoId: String, message: String, completed: Boolean = false)

object TodoActor {
  def props: Props = Props(new TodoActor)

  case object GetTodos
  case class GetTodoByID(id: String)
  case class CreateTodo(todo: Todo)
  case class DeleteTodoByID(id: String)
}

class TodoActor extends Actor with ActorLogging {
  import TodoActor._

  val todoList: Seq[Todo] = Seq.empty[Todo]

  override def preStart(): Unit = {
    log.info("Started todo actor...")
  }

  override def receive = active(todoList)

  def active(todoList: Seq[Todo]): Actor.Receive = {
    case GetTodos => sender() ! todoList
    case GetTodoByID(id) => {
      val todo = todoList
        .find(_.todoId == id)

      sender() ! todo
    }
    case CreateTodo(todo) => {
      context become active(todoList :+ todo)

      sender() ! todo
    }
    case DeleteTodoByID(id) => {
      if(todoList.map(_.todoId).contains(id)) {
        context become active(todoList.filter(_.todoId != id))
      }
      else {
        context become active(todoList)
      }
    }
  }
}
