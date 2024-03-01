package me.gamercoder215.kotatime.ui

import androidx.compose.runtime.Composable
import org.knowm.xchart.PieChart
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.style.PieStyler
import org.knowm.xchart.style.Styler
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

    override fun getBaseFont(): Font {
        TODO("Not yet implemented")
    }

    override fun getChartBackgroundColor(): Color = Color(if (darkMode) DARK_BACKGROUND_2 else LIGHT_BACKGROUND_2)

    override fun getChartFontColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getChartPadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getChartTitleFont(): Font {
        TODO("Not yet implemented")
    }

    override fun isChartTitleVisible(): Boolean {
        TODO("Not yet implemented")
    }

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

    override fun getLegendFont(): Font {
        TODO("Not yet implemented")
    }

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

    override fun getLegendPosition(): Styler.LegendPosition {
        TODO("Not yet implemented")
    }

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

    override fun isXAxisTitleVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isYAxisTitleVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAxisTitleFont(): Font {
        TODO("Not yet implemented")
    }

    override fun isXAxisTicksVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isYAxisTicksVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAxisTickLabelsFont(): Font {
        TODO("Not yet implemented")
    }

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

    override fun isAxisTicksLineVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAxisTicksMarksVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAxisTitlePadding(): Int {
        TODO("Not yet implemented")
    }

    override fun getXAxisTickMarkSpacingHint(): Int {
        TODO("Not yet implemented")
    }

    override fun getYAxisTickMarkSpacingHint(): Int {
        TODO("Not yet implemented")
    }

    override fun isCursorEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCursorColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getCursorSize(): Float {
        TODO("Not yet implemented")
    }

    override fun getCursorFont(): Font {
        TODO("Not yet implemented")
    }

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

    override fun isCircular(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getStartAngleInDegrees(): Double {
        TODO("Not yet implemented")
    }

    override fun getPieFont(): Font {
        TODO("Not yet implemented")
    }

    override fun getLabelsDistance(): Double {
        TODO("Not yet implemented")
    }

    override fun getLabelType(): PieStyler.LabelType {
        TODO("Not yet implemented")
    }

    override fun setForceAllLabelsVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDonutThickness(): Double {
        TODO("Not yet implemented")
    }

    override fun isSumVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSumFont(): Font {
        TODO("Not yet implemented")
    }

    override fun getMarkerSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getErrorBarsColor(): Color {
        TODO("Not yet implemented")
    }

    override fun isErrorBarsColorSeriesColor(): Boolean {
        TODO("Not yet implemented")
    }

}