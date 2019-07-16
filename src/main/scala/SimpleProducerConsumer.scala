object SimpleProducerConsumer {

  def main(args: Array[String]): Unit = {
    //simpleProducerConsumer()
    betterProducerConsumer()
  }

  class Container {
    private var data: Int = 0

    def isEmpty: Boolean = data == 0

    def setData(newData: Int) = data = newData

    def getData = {
      val result = data
      data = 0
      result
    }
  }

  def simpleProducerConsumer(): Unit = {
    val container = new Container

    val consumer = new Thread(() => {
      println("[Consumer] waiting...")
      while(container.isEmpty) {
        println("[Consumer] still waiting...")
      }

      println("[Consumer] consumed data: " + container.getData)
    })

    val producer = new Thread(() => {
      println("[Producer] producing data...")
      Thread.sleep(500)
      val data = 100
      println("[Consumer] produced data: " + data)
      container.setData(data)
    })

    consumer.start()
    producer.start()
  }

  def betterProducerConsumer(): Unit = {
    val container = new Container

    val consumer = new Thread(() => {
      println("[Consumer] waiting...")

      container.synchronized {
        container.wait()
      }

      println("[Consumer] consumed data: " + container.getData)
    })

    val producer = new Thread(() => {
      println("[Producer] producing data...")
      Thread.sleep(1500)
      val data = 100

      container.synchronized {
        println("[Consumer] produced data: " + data)
        container.setData(data)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

}
