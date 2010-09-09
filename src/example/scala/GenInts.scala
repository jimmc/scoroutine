import scala.util.continuations._

import net.jimmc.scoroutine.StandaloneGenerator

object GenInts {
    def main(args:Array[String]) = {
        val gen = new IntGen(4)
//        while (gen.hasNext)
//            println(gen.next)
        for (i <- gen)
            println(i)
    }
}

class IntGen(max:Int) extends StandaloneGenerator[Int] {
    generate {
        var x = 1
        while (x<=max) {
            yld(x)
            x = x + 1
        }
    }
}
