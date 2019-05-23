package GrosseProg;

/**
 * Diese Klasse dient als Grundklasse des gesamten Programmes. 
 * 
 * @author Ketli Cenollari
 *
 */
public class GrosseProgMain {
	/**
	 * Starte Programm
	 * @param args Eingabe,- und Ausgabedateien
	 */
	public static void main(String[] args) {
		Einlesen einlesen = new Einlesen();
		Ausgabe ausgabe;
		try {
			einlesen.einlesen(args[0]);
			ausgabe = new Ausgabe(einlesen);
			ausgabe.ausgeben(args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
