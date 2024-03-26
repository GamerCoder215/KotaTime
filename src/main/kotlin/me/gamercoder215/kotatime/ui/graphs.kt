package me.gamercoder215.kotatime.ui

import androidx.compose.runtime.Composable
import org.knowm.xchart.PieChart
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.style.PieStyler
import org.knowm.xchart.style.PieStyler.LabelType
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.Styler.LegendPosition
import org.knowm.xchart.style.markers.Marker
import org.knowm.xchart.style.theme.Theme
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import javax.swing.JFrame

@Composable
fun Pie(width: Int = 500, height: Int = 500, callback: PieChart.() -> Unit = {}): JFrame {
    val chart = PieChart(width, height, GraphTheme)
    chart.callback()

    return SwingWrapper(chart).displayChart()
}

object GraphTheme : Theme {
    override fun getSeriesMarkers(): Array<Marker> {
        TODO("Not yet implemented")
    }

    override fun getSeriesLines(): Array<BasicStroke> {
        TODO("Not yet implemented")
    }

    override fun getSeriesColors(): Array<Color> {
        TODO("Not yet implemented")
    }

    override fun getBaseFont(): Font = font.normal.awt(TEXT_SIZE)

    override fun getChartBackgroundColor(): Color = Color(if (darkMode) DARK_BACKGROUND_2 else LIGHT_BACKGROUND_2)

    override fun getChartFontColor(): Color = Color(if (darkMode) DARK_TEXT else LIGHT_TEXT)

    override fun getChartPadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getChartTitleFont(): Font = font.bold.awt(H1_TEXT_SIZE)

    override fun isChartTitleVisible(): Boolean = true

    override fun isChartTitleBoxVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getChartTitleBoxBackgroundColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getChartTitleBoxBorderColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getChartTitlePadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getLegendFont(): Font = font.normal.awt(H4_TEXT_SIZE)

    override fun isLegendVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLegendBackgroundColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getLegendBorderColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getLegendPadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getLegendSeriesLineLength(): Int {
        TODO("Not yet implemented")
    }

    override fun getLegendPosition(): LegendPosition = LegendPosition.OutsideS

    override fun isPlotGridLinesVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPlotGridVerticalLinesVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPlotGridHorizontalLinesVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlotBackgroundColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getPlotBorderColor(): Color {
        TODO("Not yet implemented")
    }

    override fun isPlotBorderVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlotGridLinesColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getPlotGridLinesStroke(): BasicStroke {
        TODO("Not yet implemented")
    }

    override fun isPlotTicksMarksVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlotMargin(): Int {
        TODO("Not yet implemented")
    }

    override fun isXAxisTitleVisible(): Boolean = true

    override fun isYAxisTitleVisible(): Boolean = true

    override fun getAxisTitleFont(): Font {
        TODO("Not yet implemented")
    }

    override fun isXAxisTicksVisible(): Boolean = true

    override fun isYAxisTicksVisible(): Boolean = true

    override fun getAxisTickLabelsFont(): Font = font.light.awt(H4_TEXT_SIZE)

    override fun getAxisTickMarkLength(): Int {
        TODO("Not yet implemented")
    }

    override fun getAxisTickPadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getAxisTickMarksColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getAxisTickMarksStroke(): BasicStroke {
        TODO("Not yet implemented")
    }

    override fun getAxisTickLabelsColor(): Color {
        TODO("Not yet implemented")
    }

    override fun isAxisTicksLineVisible(): Boolean = false

    override fun isAxisTicksMarksVisible(): Boolean = true

    override fun getAxisTitlePadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getXAxisTickMarkSpacingHint(): Int {
        TODO("Not yet implemented")
    }

    override fun getYAxisTickMarkSpacingHint(): Int {
        TODO("Not yet implemented")
    }

    override fun isCursorEnabled(): Boolean = true

    override fun getCursorColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getCursorSize(): Float {
        TODO("Not yet implemented")
    }

    override fun getCursorFont(): Font = font.normal.awt(TEXT_SIZE)

    override fun getCursorFontColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getCursorBackgroundColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getAvailableSpaceFill(): Double {
        TODO("Not yet implemented")
    }

    override fun isOverlapped(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCircular(): Boolean = true

    override fun getStartAngleInDegrees(): Double {
        TODO("Not yet implemented")
    }

    override fun getPieFont(): Font = font.normal.awt(TEXT_SIZE)

    override fun getLabelsDistance(): Double {
        TODO("Not yet implemented")
    }

    override fun getLabelType(): LabelType = LabelType.NameAndPercentage

    override fun setForceAllLabelsVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDonutThickness(): Double {
        TODO("Not yet implemented")
    }

    override fun isSumVisible(): Boolean = true

    override fun getSumFont(): Font {
        TODO("Not yet implemented")
    }

    override fun getMarkerSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getErrorBarsColor(): Color {
        TODO("Not yet implemented")
    }

    override fun isErrorBarsColorSeriesColor(): Boolean = true

}