package client

object Tools {
}

fun espeak(text: String) {
  val cmd = "C:\\Program_Files\\eSpeak\\command_line\\espeak.exe \"$text\""
  Runtime.getRuntime().exec(cmd)
}
