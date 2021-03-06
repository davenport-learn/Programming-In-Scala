package ProgrammingInScala.Chapter19

/**
  * Created by chris on 4/3/16.
  */

// Using private constructors
//class Queue[+T](private val leading: List[T], private val trailing: List[T]) {
//  private def mirror =
//    if (leading.isEmpty)
//      new Queue(trailing.reverse, Nil)
//    else
//      this
//
//  def head = mirror.leading.head
//
//  def tail = {
//    val q = mirror
//    new Queue(q.leading.tail, q.trailing)
//  }
//
//  def enqueue(x: T) =
//    new Queue( leading, x :: trailing)
//
//  def this(elems: T*) = this(elems.toList, Nil)
//}

//Using Traits and global objects
//trait Queue[T] {
//  def head: T
//  def tail: Queue[T]
//  def enqueue(x: T): Queue[T]
//}
//object Queue {
//  def apply[T](xs: T*): Queue[T] =
//    new QueueImpl[T](xs.toList, Nil)
//
//  private class QueueImpl[T](private val leading: List[T], private val trailing: List[T]) extends Queue[T]{
//    def mirror =
//      if (leading.isEmpty)
//        new QueueImpl[T](trailing.reverse, Nil)
//      else
//        this
//
//    def head: T = mirror.leading.head
//
//    def tail: QueueImpl[T] = {
//      val q = mirror
//      new QueueImpl[T](q.leading.tail, q.trailing)
//    }
//
//    def enqueue(x: T) =
//      new QueueImpl[T](leading, x :: trailing)
//  }
//}

trait OuputChannel[-T] {
  def write(x: T)
}

class Queue[+T] private (
                          private[this] var leading: List[T],
                          private[this] var trailing: List[T]){
  private def mirror() =
    if (leading.isEmpty){
      while (trailing.nonEmpty){
        leading = trailing.head :: leading
        trailing = trailing.tail
      }
    }

  def head: T = {
    mirror()
    leading.head
  }

  def tail: Queue[T] = {
    mirror()
    new Queue(leading.tail, trailing)
  }

  def enqueue[U >: T](x: U) =
    new Queue[U](leading, x :: trailing)
}