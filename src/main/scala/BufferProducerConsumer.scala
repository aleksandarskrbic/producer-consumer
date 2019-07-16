import scala.collection.mutable
import scala.util.Random

object BufferProducerConsumer {

  def main(args: Array[String]): Unit = {
    bufferProducerConsumer()
  }

  def bufferProducerConsumer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 5

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[Consumer] buffer is empty, waiting...")
            buffer.wait()
          }

          val value = buffer.dequeue()
          println("[Consumer] consumed: " + value)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(200))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var cnt = 0

      while(true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[Producer] buffer is full, waiting...")
            buffer.wait()
          }
          println("[Producer] producing " + cnt)
          buffer.enqueue(cnt)
          buffer.notify()

          cnt += 1
        }
        Thread.sleep(random.nextInt(100))
      }
    })

    consumer.start()
    producer.start()
  }
}
