import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.math.floor

open class AbstractShapeColor {
    fun createAlphaColorArray(c: ColorRGBa): ArrayList<ColorRGBa> {
        val array = ArrayList<ColorRGBa>()
        for (alphaValue in 0..255) {
            array.add(c.opacify(alphaValue / 255.0))
        }
        return array
    }

    open fun aply(drawer: Drawer, alphaValue: Double = 255.0) {

    }
}

class ShapeColor(strokeColor: ColorRGBa, fillColor: ColorRGBa) : AbstractShapeColor() {
    private var fillColorArray: ArrayList<ColorRGBa> = createAlphaColorArray(fillColor)
    private var strokeColorArray: ArrayList<ColorRGBa> = createAlphaColorArray(strokeColor)

    override fun aply(drawer: Drawer, alphaValue: Double) {
        val index = floor(alphaValue.coerceIn(0.0, 255.0)).toInt()
        drawer.stroke = (this.strokeColorArray[index])
        drawer.fill = (this.fillColorArray[index])
    }
}

class NoStrokeShapeColor(fillColor: ColorRGBa) : AbstractShapeColor() {
    private var fillColorArray: ArrayList<ColorRGBa> = createAlphaColorArray(fillColor)

    override fun aply(drawer: Drawer, alphaValue: Double) {
        drawer.stroke = null
        val index = floor(alphaValue.coerceIn(0.0, 255.0)).toInt()
        drawer.fill = (this.fillColorArray[index]);
    }
}

class NoFillShapeColor(strokeColor: ColorRGBa) : AbstractShapeColor() {
    private var strokeColorArray: ArrayList<ColorRGBa> = createAlphaColorArray(strokeColor)

    override fun aply(drawer: Drawer, alphaValue: Double) {
        val index = floor(alphaValue.coerceIn(0.0, 255.0)).toInt()
        drawer.stroke = (this.strokeColorArray[index]);
        drawer.fill = null
    }
}

class NullShapeColor : AbstractShapeColor() {
    override fun aply(drawer: Drawer, alphaValue: Double) {

    }
}