import scala.collection.mutable
import scala.util.Random

object ManyToManyProducerConsumer {

  def main(args: Array[String]): Unit = {
    manyToManyProduceConsumer(2, 5)
  }

  def manyToManyProduceConsumer(consumersCnt: Int, producersCnt: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 5

    (1 to consumersCnt).foreach(new Consumer(_, buffer).start())
    (1 to producersCnt).foreach(new Producer(_, capacity, buffer).start())
  }

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[Consumer $id] buffer is empty, waiting...")
            buffer.wait()
          }

          val value = buffer.dequeue()
          println(s"[Consumer $id] consumed: " + value)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(200))
      }
    }
  }

  class Producer(id: Int, capacity: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var cnt = 0

      while(true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[Producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          println(s"[Producer $id] producing " + cnt)

          buffer.enqueue(cnt)
          buffer.notify()

          cnt += 1
        }
        Thread.sleep(random.nextInt(100))
      }
    }
  }
}
