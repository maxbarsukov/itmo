import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.knowm.xchart.style.lines.SeriesLines.SOLID;

public class Chart {
  private final List<Double> xs = new ArrayList<>();
  private final List<Double> ys = new ArrayList<>();

  private final XYChart chart;

  public Chart(String x, String y, String title) {
    this.chart = new XYChartBuilder().theme(Styler.ChartTheme.Matlab)
      .title(title)
      .xAxisTitle(x)
      .yAxisTitle(y)
      .build();

    chart.setCustomXAxisTickLabelsFormatter((xAxis) -> String.format("%.2f", xAxis));
    chart.setCustomYAxisTickLabelsFormatter((yAxis) -> String.format("%.2f", yAxis));
    chart.getStyler().setSeriesLines(new BasicStroke[]{SOLID});
  }

  public void addChart(String name, double a, double b, double h) {
    setupValues(a, b, h);
    chart.addSeries(name, xs, ys).setLineWidth(2);
  }

  public void polygonalChart(double a, double h) {
    xs.add(a);
    ys.add(h);
    chart.getStyler().setMarkerSize(0);
  }

  public void addHistogram(String name, double a, double b, double h) {
    setupValues(a, b, h);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
    chart.addSeries(name, xs, ys).setLineWidth(1);
  }

  public void plotPolygon(String name) {
    chart.addSeries(name, this.xs, this.ys).setLineWidth(2);
    savePng(name);
  }

  public void plot(String name) {
    savePng(name);
  }

  private void setupValues(double a, double b, double h) {
    xs.clear();
    ys.clear();
    xs.add(a);
    xs.add(b);
    ys.add(h);
    ys.add(h);
    chart.getStyler().setMarkerSize(0);
  }

  private void savePng(String name) {
    try {
      BitmapEncoder.saveBitmap(chart, name, BitmapEncoder.BitmapFormat.PNG);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
