package literateScala

import java.io.File
import org.apache.commons.lang3.StringEscapeUtils
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object Main {

  def main(args:Array[String]) {
    val files = args.toList.map(new File(_))
    val commentRegexStr = """(?s)(?m)/\*(.*?)\*/|//(.*?)\n"""
    val commentRegex = commentRegexStr.r
    val contents = files.map(f => {
      val source = scala.io.Source.fromFile(f)
      val str = source.getLines.mkString("\n")
      def escape(s:String) = StringEscapeUtils.escapeHtml4(s)
      def cleanText(t:String) = {
        val text = t.trim
        if (text.startsWith("//")) {
          text.substring(2)
        } else {
          if (text.startsWith("/*")) {
            text.substring(2, text.length - 2)
          } else {
            throw new Exception("text is neither // nor /* style comment")
          }
        }.trim
      }
      def cleanCode(c:String) = {
        val cleaner = """(?s)(?m)(.*?)\s*""".r
        c match {
          case cleaner(res) => res
          case _ => throw new Exception("'" + c + "' did not match")
        }
      }
      val textBlocks = commentRegex.findAllIn(str).toList.map(cleanText).map(text => "<p>" + text + "</p>")
      val codeBlocks = str.split(commentRegexStr).map(cleanCode).filter("" !=).map { code => 
        "<pre>" + escape(code) + "</pre>"
      }
      f -> textBlocks.zipAll(codeBlocks, "", "").map { case (t,c) => t + c }.mkString
    })
    val htmlDocs = contents.map { case (f,c) => 
      f -> ("<html><head><title>literate scala</title></head><body>" + c + "</body></html>")
    }
    htmlDocs.foreach { case (file,htmlDoc) => {
      val outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file.getName + ".html"))))
      outFile.write(htmlDoc)
      outFile.close()
     
    }}
  }

}


