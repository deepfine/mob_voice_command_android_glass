package com.deepfine.voicecommand.utils

internal object RealWear {
  private val digitFollowedByLetter: Regex = "(\\p{Nd})(\\p{L})".toRegex()
  private val letterFollowedByDigit: Regex = "(\\p{L})(\\p{Nd})".toRegex()

  /**
   * 리얼웨어 음성인식을 위해 문자열 사이에 숫자가 포함된 경우 앞/뒤에 공백 추가
   * ex) "1번 보기", "항목1선택" -> "1 번 보기", "항목 1 선택"
   * @param keyword 명령어
   */
  internal fun normalizeVoiceCommand(keyword: String): String = keyword
    .replace(digitFollowedByLetter, "$1 $2")
    .replace(letterFollowedByDigit, "$1 $2")
    .replace(",\\s*".toRegex(), ",")
    .replace("\\s+".toRegex(), " ")
    .trim()

  /**
   * keyword 패턴(%d 포함)과 실제 음성 command가 매칭되는지 여부를 반환
   *
   * ex) keyword, command -> Boolean
   * "%d페이지", "2 페이지" -> true
   * "아이템 %d", "아이템 2" -> true
   * "항목%d선택", "항목 3 선택" -> true
   * "항목%d선택", "2 페이지" -> false
   *
   * @param keyword 비교할 명령어
   * @param command 수신받은 음성 명령어
   */
  internal fun matchesVoiceCommand(keyword: String, command: String): Boolean {
    val regexPattern: String = normalizeVoiceCommand(keyword)
      .split("%d")
      .joinToString("\\s*\\d+\\s*") { Regex.escape(it) }

    return Regex("^$regexPattern$").matches(normalizeVoiceCommand(command))
  }

  const val ACTION_SPEECH_EVENT = "com.realwear.wearhf.intent.action.SPEECH_EVENT"
  const val EXTRA_SPEECH_COMMAND = "command"
  const val HF_ADD_COMMANDS = "hf_add_commands:"
  const val HF_COMMANDS = "hf_commands:"
  const val HF_HIDE_GUIDANCE = "hf_no_number|hf_hide_help"
}
