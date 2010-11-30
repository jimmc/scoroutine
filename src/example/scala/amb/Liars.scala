import scala.collection.Iterator
import scala.util.continuations._
import net.jimmc.scoroutine._

/* Exercise 4.42 from SICP:

Solve the following ``Liars'' puzzle (from Phillips 1934):

    Five schoolgirls sat for an examination. Their parents -- so they
    thought -- showed an undue degree of interest in the result. They
    therefore agreed that, in writing home about the examination,
    each girl should make one true statement and one untrue one. The
    following are the relevant passages from their letters:

        * Betty: ``Kitty was second in the examination. I was only third.''
        * Ethel: ``You'll be glad to hear that I was on top. Joan was second.''
        * Joan: ``I was third, and poor old Ethel was bottom.''
        * Kitty: ``I came out second. Mary was only fourth.''
        * Mary: ``I was fourth. Top place was taken by Betty.'' 

    What in fact was the order in which the five girls were placed? 
 */
class Liars extends AmbEval[List[(String,Int)]] {
    def distinct(vals:List[Int]):Boolean = {
        vals.distinct.length == vals.length
    }
    generate {
        val betty = amb(List(1,2,3,4,5))
        val ethel = amb(List(1,2,3,4,5))
        val joan = amb(List(1,2,3,4,5))
        val kitty = amb(List(1,2,3,4,5))
        val mary = amb(List(1,2,3,4,5))
        require(distinct(List(betty,ethel,joan,kitty,mary)))
        require((kitty==2 && betty!=3) || (kitty!=2 && betty==3))
        require((ethel==1 && joan!=2) || (ethel!=1 && joan==2))
        require((joan==3 && ethel!=5) || (joan!=3 && ethel==5))
        require((kitty==2 && mary!=4) || (kitty!=2 && mary==4))
        require((mary==4 && betty!=1) || (mary!=4 && betty==1))
        yld(List(
            ("betty",betty),
            ("ethel",ethel),
            ("joan",joan),
            ("kitty",kitty),
            ("mary",mary)))
    }
}

//output: List((betty,3), (ethel,5), (joan,2), (kitty,1), (mary,4))
