package sociability.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

public class RadarChartActivity extends AppCompatActivity implements View.OnClickListener{
    private RadarChart chart;
    private SparseIntArray factors = new SparseIntArray(5);
    private SparseIntArray scores = new SparseIntArray(5);
    private ArrayList<RadarEntry> entries = new ArrayList<>();
    private ArrayList<IRadarDataSet> dataSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        RadarChart chart = (RadarChart)findViewById(R.id.radar_chart);
        Button b = (Button)findViewById(R.id.goTo_quiz_button);
        b.setOnClickListener(this);

        factors.append(1, R.string.extraversion);
        factors.append(2, R.string.agreeableness);
        factors.append(3, R.string.conscientiousness);
        factors.append(4, R.string.openness);
        factors.append(5, R.string.neuroticism);

        scores.append(1, 18);
        scores.append(2, 26);
        scores.append(3, 35);
        scores.append(4, 40);
        scores.append(5, 48);

        drawChart();

    }

    private void drawChart() {

        entries.clear();

        for (int i = 1; i <= 5; i++) {
            entries.add(new RadarEntry(scores.get(i)));
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "");
        dataSet.setColor(R.color.colorPrimary);
        dataSet.setDrawFilled(true);

        dataSets.add(dataSet);

        RadarData data = new RadarData(dataSets);
        data.setValueTextSize(8f);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.goTo_quiz_button:
                //start the main activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
