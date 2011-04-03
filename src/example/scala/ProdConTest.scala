import scala.util.continuations._

import net.jimmc.scoroutine.DefaultCoScheduler
import net.jimmc.scoroutine.CoQueue

object ProdConTest {
    def main(args:Array[String]) = {
        val max =   (if (args.length>0) Integer.parseInt(args(0)) else 4)
        val qSize = (if (args.length>1) Integer.parseInt(args(1)) else 2)
        println("max="+max+", qSize="+qSize)
        val prodcon = new ProdCon(max,qSize)
        prodcon.run
    }
}

class ProdCon(max:Int, qSize:Int) {
    val sched = new DefaultCoScheduler
    val buf = new CoQueue[Int](sched,qSize)

    def run() {
        sched.addRoutine("producer"){
            var i = 0
            while (i <= max) {
                println("producing "+i)
                buf.blockingEnqueue(i)
                i += 1
            }
        }
        sched.addRoutine("consumer"){
            var total = 0
            var i = 0
            while (i <= max) {
                val x = buf.blockingDequeue
                println("consumed "+x)
                total += x
                i += 1
            }
            println("consume total is "+total)
        }
        sched.runUntilBlockedOrDone
    }
}
