package sparklin.kshell

import sparklin.kshell.org.jline.reader.Highlighter
import sparklin.kshell.org.jline.reader.LineReader
import sparklin.kshell.org.jline.utils.AttributedString
import sparklin.kshell.org.jline.utils.AttributedStringBuilder

class ContextHighlighter(private val isIncomplete: () -> Boolean): Highlighter {
    private val highlighters = mutableMapOf<Context, Highlighter>()

    override fun highlight(reader: LineReader, buffer: String): AttributedString {
        val context = if (isIncomplete() &&
                buffer.startsWith(":") &&
                !buffer.startsWith("::")) Context.COMMAND else Context.CODE
        return highlighters.getOrDefault(context, DEFAULT).highlight(reader, buffer)
    }

    fun bind(context: Context, highlighter: Highlighter) {
        highlighters[context] = highlighter
    }

    fun default(context: Context) {
        bind(context, DEFAULT)
    }

    companion object {
        private val DEFAULT = Highlighter { _, buffer ->
            val sb = AttributedStringBuilder()
            sb.append(buffer)
            sb.toAttributedString()
        }
    }

    enum class Context {
        CODE,
        COMMAND
    }
}