package GrosseProg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Klasse, um eine Ausgabedatei zu erstellen
 * 
 * @author Ketli Cenollari Nr. 101 20602
 *
 */

public class Ausgabe {

	/**
	 * Attribute der Klasse Ausgabe
	 */

	public Einlesen einlesen;
	private List<Staat> alleStaaten;
	private Algorithmus algorithmus;
	private String kennwert;
	public static final String L_SEP = System.lineSeparator();
	// Da die Iterationsanzahl in dem Algorithmus auf 2000 gesetzt ist, wird der
	// Wert hier fest codiert
	private int iteration = 2000;

	/**
	 * Konstruktor für die Klasse Ausgabe. Hier werden die Daten initialisiert
	 * 
	 * @param einlesen
	 *            Die Eingabedatei, für die die Ausgabedatei erstellt werden
	 *            soll
	 */
	public Ausgabe(Einlesen einlesen) {
		this.einlesen = einlesen;
		algorithmus = new Algorithmus(einlesen);
		this.alleStaaten = algorithmus.getAlleStaaten();
		this.kennwert = einlesen.kennwertname;
	}

	/**
	 * Diese Methode erstellt mittels FileWriter eine neue Datei
	 * 
	 * @param pfad
	 *            Pfad, wo die erstellte Datei gespeichert werden soll
	 * @throws FileNotFoundException
	 *             Falls die Datei nicht erstellt werden kann
	 */

	public void ausgeben(String pfad) throws FileNotFoundException {
		File file = new File(pfad);
		try {
			FileWriter fw = new FileWriter(file);
			StringBuilder sb = new StringBuilder();
			// Der Inhalt wird mittels StringBuilder zusammengesetzt
			sb.append("reset");
			sb.append(L_SEP);
			sb.append("set xrange [" + algorithmus.getMin_x() + ":" + algorithmus.getMax_x() + "]");
			sb.append(L_SEP);
			sb.append("set yrange [" + algorithmus.getMin_y() + ":" + algorithmus.getMax_y() + "]");
			sb.append(L_SEP);
			sb.append("set size ratio 1.0");
			sb.append(L_SEP);
			sb.append("set title \"" + kennwert + ", Iteration: " + iteration + "\"");
			sb.append(L_SEP);
			sb.append("unset xtics");
			sb.append(L_SEP);
			sb.append("unset ytics");
			sb.append(L_SEP);
			sb.append("$data << EOD");
			sb.append(L_SEP);
			for (Staat s : alleStaaten) {
				String a = "" + s.getMittelpunkt().getX() + " " + s.getMittelpunkt().getY() + " " + s.getRadius() + " "
						+ s.getAutokennzeichen() + " " + s.getId();
				sb.append(a);
				sb.append(L_SEP);
			}
			sb.append("EOD");
			sb.append(L_SEP);
			sb.append("\n");
			sb.append("plot \\");
			sb.append(L_SEP);
			sb.append("\'$data\' using 1:2:3:5 with circles lc var notitle, \\");
			sb.append(L_SEP);
			sb.append("\'$data\' using 1:2:4:5 with labels font \"arial,9\" tc var notitle");
			fw.write(sb.toString());
			fw.flush();
		} catch (IOException e) {
			throw new StaatException("Ausgabedatei konnte nicht geschrieben werden!");
		}
	}
}
