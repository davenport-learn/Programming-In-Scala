package ProgrammingInScala.Chapter15
import ProgrammingInScala.Chapter14.Element
import ProgrammingInScala.Chapter14.Element._
import ProgrammingInScala.Chapter15.Expr
/**
  * Created by chris on 4/3/16.
  */
class ExprFormatter {
  //Contains operators in groups of increasing precednece
  private val opGroups =
    Array(
      Set("|", "||"),
      Set("&", "&&"),
      Set("^"),
      Set("==", "!="),
      Set("<", "<=", ">", ">="),
      Set("+", "-"),
      Set("*", "%")
    )
  private val precedence = {
    val assocs =
      for {
        i <- opGroups.indices
        op <- opGroups(i)
      } yield op -> i
    assocs.toMap
  }

  private val unaryPrecendence = opGroups.length
  private val fractionPrecedence = -1

  private def format(e: Expr, enclPrc: Int): Element =
    e match {
      case Var(name) => elem(name)
      case Number(num) =>
        def stripDot(s: String) =
          if (s endsWith ".0") s.substring(0, s.length - 2)
          else s
        elem(stripDot(num.toString))
      case UnOp(op, arg) =>
        elem(op) beside format(arg, unaryPrecendence)
      case BinOp("/", left, right) =>
        val top = format(left, fractionPrecedence)
        val bot = format(right, fractionPrecedence)
        val line = elem('-', top.width max bot.width,1)
        val frac = top above line above bot
        if (enclPrc != fractionPrecedence) frac
        else elem(" ") beside frac beside elem(" ")
      case BinOp(op, left, right) =>
        val opPrec = precedence(op)
        val l = format(left, opPrec)
        val r = format(right, opPrec +1)
        val oper = l beside elem(" "+ op + " ") beside r
        if (enclPrc <= opPrec) oper
        else elem("(") beside oper beside elem(")")
    }

  def format(e: Expr): Element = format(e, 0)


}
