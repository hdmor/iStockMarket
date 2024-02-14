package i.stock.market.presentation.company_info.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import i.stock.market.domain.model.IntraDayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(stockInfoList: List<IntraDayInfo> = emptyList(), modifier: Modifier = Modifier, graphColor: Color = Color.Black) {

    val spacing = 100f
    val transparentGraphColor = remember { graphColor.copy(alpha = .5f) }
    val upperValue = remember(stockInfoList) { (stockInfoList.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember(stockInfoList) { (stockInfoList.minOfOrNull { it.close }?.toInt() ?: 0) }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / stockInfoList.size
        /** writing horizontal texts in chart **/
        (0 until stockInfoList.size - 1 step 2).forEach { i ->
            val info = stockInfoList[i]
            val hour = info.date.hour
            drawContext.canvas.nativeCanvas.apply {
                drawText(hour.toString(), spacing + i * spacePerHour, size.height - 5, textPaint)
            }
        }
        val priceStep = (upperValue - lowerValue) / 5f
        /** writing vertical texts in chart **/
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(round(lowerValue + priceStep * i).toString(), 30f, size.height - spacing - i * size.height / 5f, textPaint)
            }
        }
        var lastX = 0f

        /** drawing exactly the path in chart **/
        val strokePath = Path().apply {
            val height = size.height
            for (i in stockInfoList.indices) {
                val info = stockInfoList[i]
                val nextInfo = stockInfoList.getOrNull(i + 1) ?: stockInfoList.last()
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)
                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if (i == 0) moveTo(x1, y1)
                lastX = (x1 + x2) / 2f
                quadraticBezierTo(x1, y1, lastX, (y1 + y2) / 2f)
            }
        }

        /** draw and filling the bottom of the path in chart **/
        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(lastX, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(colors = listOf(transparentGraphColor, Color.Transparent), endY = size.height - spacing)
        )
        drawPath(path = strokePath, color = graphColor, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
    }
}