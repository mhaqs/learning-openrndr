import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap

class SpecialText(private val drawer: Drawer, var tSize: Double) {  //  class for all those fancy vectors and subscripts
    fun createBullet(writing: String, x: Double, y: Double) {
        drawer.fill = ColorRGBa.BLACK
        drawer.circle(x, y, 6.0)
        writer.text(writing, x + 11, y + 5)
    }

    fun createSubscript(bigLetter: String, subscript: String, x: Double, y: Double) {
        writer.text(bigLetter, x, y)
        val sw = writer.textWidth(bigLetter)
        writer.text(subscript, "file:data/fonts/Palatino-Linotype-Italic.ttf", tSize -5, x + sw, y + 5)
        writer.drawStyle.fontMap = FontImageMap.fromUrl("file:data/fonts/Palatino-Linotype-Italic.ttf", tSize)
    }

    fun createDivision(top: String, bottom: String, x: Double, y: Double) {
        writer.text(top, x, y - tSize)
        writer.text(bottom, x, y + tSize)
        val topWidth = writer.textWidth(top)
        val bottomWidth = writer.textWidth(bottom)

        if (topWidth >= bottomWidth) {
            drawer.stroke = ColorRGBa.BLACK
            drawer.lineSegment(x - topWidth / 2, y - 5, x + topWidth / 2, y - 5)
            drawer.stroke = null
        } else {
            drawer.stroke = ColorRGBa.BLACK
            drawer.lineSegment(x - bottomWidth / 2, y - 5, x + bottomWidth / 2, y - 5)
            drawer.stroke = null
        }
        //textAlign(LEFT)
    }

    fun createVector(vec: String, x: Double, y: Double) {
        writer.text(vec, x, y)
        val vecWidth = writer.textWidth(vec)
        drawer.stroke = ColorRGBa.BLACK
        drawer.lineSegment(x, y - tSize + 2, x + vecWidth, y - tSize + 2)
        drawer.lineSegment(x + vecWidth, y - tSize + 2, x + vecWidth - 2, y - tSize)
        drawer.stroke = null
    }

    fun createVectorWithSubscript(vec: String, subscript: String, x: Double, y: Double) {
        writer.text(vec, x, y)
        val vecWidth = writer.textWidth(vec)
        drawer.lineSegment(x, y - tSize + 2, x + vecWidth, y - tSize + 2)
        drawer.lineSegment(x + vecWidth, y - tSize + 2, x + vecWidth - 2, y - tSize)
        drawer.stroke = null
        writer.text(subscript, "file:data/fonts/Palatino-Linotype-Italic.ttf", tSize-5, x + vecWidth, y+5)
        writer.drawStyle.fontMap = FontImageMap.fromUrl("file:data/fonts/Palatino-Linotype-Italic.ttf", tSize)
    }
}