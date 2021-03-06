package net.fosdal.oslo

import java.text.DecimalFormat

import scala.annotation.tailrec

package object olong {

  private[this] val DefaultPrettyFactor = 1000

  implicit class LongOps(val n: Long) extends AnyVal {

    @tailrec
    private def fact(j: Long, result: Long = 1L): Long = if (j == 0) result else fact(j - 1, j * result)

    def ! : Long = fact(n)

    def choose(k: Long): Long = fact(n) / (fact(n - k) * fact(k))

    def times(f: => Unit): Unit = {
      @tailrec
      def times(j: Long, f: => Unit): Unit = {
        if (j > 0) {
          f
          times(j - 1, f)
        }
      }

      times(n, f)
    }

    def pretty(factor: Int): String = {
      val margin = 0.9D
      val units  = Seq("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
      val format = "%.2f"

      val formatter = new DecimalFormat(format)

      @tailrec
      def pretty(d: Double, u: Seq[String]): String = {
        val r = d / factor
        if (r < margin || u.length == 1) {
          s"${formatter.format(d)}${u.head}"
        } else {
          pretty(r, u.tail)
        }
      }

      pretty(n.toDouble, units)
    }

    def pretty: String = pretty(DefaultPrettyFactor)

  }

}
