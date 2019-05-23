package GrosseProg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Klasse, um die Eingabedatei einzulesen
 * 
 * @author Ketli Cenollari Prüflingsnummer 101 20602
 *
 */

public class Einlesen {

	/**
	 * Attribute der Klasse Einlesen
	 */
	public List<Staat> alleStaaten = new ArrayList<Staat>();
	public String kennwertname;

	/**
	 * Liest die Eingabedatei ein und speichert die Daten in die Liste von Typ
	 * Staat
	 * 
	 * @param name
	 *            Die Datei, die eingelesen werden soll
	 */
	public void einlesen(String name) {
		File file = new File(name);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			String zeile;
			int counter = 0;
			int id = 0;
			while (sc.hasNextLine()) {
				zeile = sc.nextLine();
				if (counter == 0) {
					// Da wir die Syntax nicht überprüfen müssen, kann die erste
					// Zeile immer als Kennwertname gespeichert werden
					kennwertname = zeile;
					counter++;
				} else if (zeile.startsWith("#")) {
					// Kommentarzeilen werden ignoriert
					counter++;
				} else if (counter == 2) {
					String[] werte = zeile.split("\t");
					if (Double.parseDouble(werte[1]) > 0) {
						Staat staat = new Staat(werte[0].trim(), Double.parseDouble(werte[1]),
								new Punkt(Double.parseDouble(werte[2]), Double.parseDouble(werte[3])));
						staat.setId(id);
						id++;
						// Jeden Staat in die Liste hinzufügen
						alleStaaten.add(staat);
						for (Staat s : alleStaaten) {
							// Überprüfung ob 2 Staaten mit unterschiedlichen
							// Kennzeichen, aber gleichen Anfangspunkten gibt
							if (staat != s && staat.getAnfangpunkt().getX() == s.getAnfangpunkt().getX()
									&& staat.getAnfangpunkt().getY() == s.getAnfangpunkt().getY()
									&& !(staat.getAutokennzeichen().equals(s.getAutokennzeichen()))) {
								throw new StaatException(
										"Fehler: Es befinden sich 2 Staaten mit dem gleichen Anfangspunkt.");
								// Überprüfung, ob der gleiche Staat doppelt in
								// der Liste vorkommt. Sollte das der Fall sein,
								// wird der doppelte Eintrag ignoriert
							} else if (staat != s && staat.getAnfangpunkt().getX() == s.getAnfangpunkt().getX()
									&& staat.getAnfangpunkt().getY() == s.getAnfangpunkt().getY()
									&& staat.getAutokennzeichen().equals(s.getAutokennzeichen())) {
								alleStaaten.remove(s);
								// Überprüfung, ob zwei Einträge für den
								// gleichen Staat unterschiedliche Anfangspunkte
								// haben. Sollte das der Fall sein, wird ein
								// Fehler geworfen
							} else if (staat != s && staat.getAutokennzeichen().equals(s.getAutokennzeichen())
									&& (staat.getAnfangpunkt().getX() != s.getAnfangpunkt().getX()
											|| staat.getAnfangpunkt().getY() != s.getAnfangpunkt().getY())) {
								throw new StaatException(
										"Fehler, ein Staat kommt doppelt vor mit anderen Anfangspunkten");
							}

						}
					} else {
						throw new StaatException("Die Kennwert " + Double.parseDouble(werte[1]) + " ist negativ.");
					}

				} else if (counter == 3) {
					String key = zeile.split(": ")[0];
					List<String> values = Arrays.asList(zeile.split(": ")[1].split(" "));
					for (String namen : values) {
						// Überprüfung, ob es einen Staat mit diesem
						// Autokennzeichen bereits gibt
						if (alleStaaten.contains(GetStaatWhere(key)) && alleStaaten.contains(GetStaatWhere(namen))) {
							GetStaatWhere(namen).getNachbarn().add(GetStaatWhere(key));
							GetStaatWhere(key).getNachbarn().add(GetStaatWhere(namen));
						} else {
							throw new StaatException(
									"Fehler: In den Nachbarn befindet sich ein Kennzeichen, das keinem Staat zugeordnet ist.");
						}
					}
				}
			}

		} catch (IOException e) {
			throw new StaatException("Fehler: Eingabedatei konnte nicht am angegeben Ort gefunden werden. " + name);
		}
	}

	/**
	 * Gibt den Staat mit Autokennzeichen zurück
	 * 
	 * @param autokennzeichen
	 *            Für diesen Wert muss der Staat gefunden werden.
	 * @return Ein Staat
	 */

	public Staat GetStaatWhere(String autokennzeichen) {
		for (Staat s : alleStaaten) {
			if (s.getAutokennzeichen().equals(autokennzeichen)) {
				return s;
			}
		}
		return null;
	}
}
