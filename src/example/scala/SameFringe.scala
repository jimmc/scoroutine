import scala.util.continuations._
import net.jimmc.scoroutine.StandaloneGenerator

sealed abstract class Tree[T]
case class Branch[T](left:Tree[T], right:Tree[T]) extends Tree[T]
case class Leaf[T](x:T) extends Tree[T]

object SameFringe {
    def main(args:Array[String]) = {
        val t1 = Branch(Branch(Leaf(1),Leaf(2)),Leaf(3))
        val t2 = Branch(Leaf(1),Branch(Leaf(2),Leaf(3)))
        val t3 = Branch(Leaf(1),Branch(Leaf(2),Leaf(4)))
        println("t1:"); for (x <- (new TreeFringe(t1))) println(x)
        println("t2:"); for (x <- (new TreeFringe(t2))) println(x)
        println("t3:"); for (x <- (new TreeFringe(t3))) println(x)
        println("sameFringe(t1,t2)="+sameFringe(t1,t2))
        println("sameFringe(t1,t3)="+sameFringe(t1,t3))
    }

    def sameFringe[T](tree1:Tree[T], tree2:Tree[T]):Boolean = {
        !((new TreeFringe(tree1)).zipAll(new TreeFringe(tree2),null,null).
            exists(p=>p._1!=p._2))
    }

    def XsameFringe[T](tree1:Tree[T], tree2:Tree[T]):Boolean = {
        val fringe1 = new TreeFringe(tree1)
        val fringe2 = new TreeFringe(tree2)
        while(fringe1.hasNext && fringe2.hasNext) {
            if (fringe1.next != fringe2.next)
                return false;
        }
        !(fringe1.hasNext || fringe2.hasNext)
            //The two trees are equal only if both fringes are done
    }
}

class TreeFringe[T](tree:Tree[T]) extends StandaloneGenerator[T] {
    generate {
        def walk(t:Tree[T]):Unit @suspendable = {
            t match {
                case Leaf(x) => yld(x)
                case Branch(left,right) => walk(left); walk(right)
            }
        }
        walk(tree)
    }
}
