package literateScala

import java.io.File

object Main {

  def main(args:Array[String]) {
    val files = List(
        "/src/main/scala/sprouch/tutorial/comment",
        "/build.sbt"
    ).map("/home/k/workspaces/sprouch-tutorial/" +).map(new File(_)) //args.toList
      val commentRegexStr = """(?s)(?m)/\*(.*?)\*/|//(.*?)$"""
      val commentRegex = commentRegexStr.r
      val contents = files.map(f => {
      val source = scala.io.Source.fromFile("Main.scala")
      val str = source.getLines.mkString
      def escape(s:String) = StringEscapeUtils.escapeHtml4(s);
      val textBlocks = commentRegex.findAllIn(str).toList.map("<p>" + _ + "</p>")
      val codeBlocks = str.split(commentRegexStr).toList.map("<pre>" + _ + "</pre>")

      
    })
    
  }

}


