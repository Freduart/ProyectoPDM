package sv.ues.fia.eisi.proyectopdm.Activity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Locale;

public class FormatterPersonal extends ValueFormatter {
    public FormatterPersonal() {
        super();
    }

    @Override
    public String getFormattedValue(float value) {
        return String.format(Locale.US,"%.2f",value);
    }

    @Override
    public String getPointLabel(Entry entry) {
        return getFormattedValue(entry.getX());
    }
}
