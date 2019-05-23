package GrosseProg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.omg.Messaging.SyncScopeHelper;

/**
 * Diese Klasse enthält den Algorithmus zur Berechnung der Mittelpunkte
 * 
 * @author Ketli Cenollari Prüflingsnummer 101 20602
 *
 */

public class Algorithmus {

	/**
	 * Attribute des Algorithmus
	 */

	private Einlesen einlesen;
	private List<Staat> alleStaaten;
	private double min_x;
	private double max_x;
	private double min_y;
	private double max_y;

	/**
	 * Konstruktor für die Klasse Algorithmus. Anzahl von Iterationen
	 * festgesetzt auf 2000. Die IHK Beispiele werden hiermit gut berechnet. Die
	 * Anzahl von Iterationen kann als Attribut zu der Algorithmus hinzugefügt
	 * werden, und dann wird der Algorithmus entsprechend oft durchgeführt.
	 * 
	 * @param einlesen
	 *            Einlesen Objekt worauf der Algorithmus angewendet werden soll
	 */

	public Algorithmus(Einlesen einlesen) {
		this.einlesen = einlesen;
		this.alleStaaten = einlesen.alleStaaten;
		wendeKraft(2000);
		setRange();
	}

	/**
	 * 
	 * @return Eine Liste mit alle Staaten
	 */

	public List<Staat> getAlleStaaten() {
		return alleStaaten;
	}

	/**
	 * Berechnet die Kräfte für einen Staat
	 * 
	 * @param staat
	 *            Staat, für den die Kräfte berechnet werden sollen
	 * @return Ein Array mit Vektoren, wo die für einen Staat berechneten Kräfte
	 *         gespeichert sind
	 */

	public Punkt[] berechneKraefte(Staat staat) {
		List<Punkt> kraefte = new ArrayList<>();
		double factorAnz = 0.01;
		double factorAbst = 0.1;
		for (Staat s : alleStaaten) {
			if (staat != s) {
				double xDiff = s.getMittelpunkt().getX() - staat.getMittelpunkt().getX();
				double yDiff = s.getMittelpunkt().getY() - staat.getMittelpunkt().getY();
				double mDist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
				double sumRad = staat.getRadius() + s.getRadius();
				double rDist = mDist - sumRad;
				// x und y normieren und mit Länge multiplizieren
				double xForce = xDiff * (rDist / (mDist));
				double yForce = yDiff * (rDist / (mDist));
				// In diesem Fall gibt es eine Anziehungskraft
				if (rDist > 0 && staat.getNachbarn().contains(s)) {
					Punkt ret = new Punkt(xForce * factorAnz, yForce * factorAnz);
					kraefte.add(ret);
					// Sonst gibt es Überlappung zwischen zwei Staaten
				} else if (rDist < 0) {
					Punkt ret = new Punkt(xForce * factorAbst, yForce * factorAbst);
					kraefte.add(ret);
				}
			}
		}
		Punkt[] punkte = new Punkt[kraefte.size()];
		for (int i = 0; i < punkte.length; i++) {
			punkte[i] = kraefte.get(i);
		}
		return punkte;
	}

	/**
	 * Wendet die berechneten Kräfte auf jeden Staat an und setzt den
	 * Mittelpunkt
	 */

	public void wendeKraft() {
		List<Punkt[]> alleKraefte = new ArrayList<>();
		for (Staat s : alleStaaten) {
			alleKraefte.add(berechneKraefte(s));
		}
		for (int i = 0; i < alleKraefte.size(); i++) {
			double xtemp = alleStaaten.get(i).getMittelpunkt().getX();
			double ytemp = alleStaaten.get(i).getMittelpunkt().getY();
			for (int j = 0; j < alleKraefte.get(i).length; j++) {
				xtemp += alleKraefte.get(i)[j].getX();
				ytemp += alleKraefte.get(i)[j].getY();
			}
			alleStaaten.get(i).setMittelpunkt(new Punkt(xtemp, ytemp));
		}
	}

	/**
	 * Ruft die Funktion wendeKraft() iterativ mit übergegebenen Anzahl von
	 * Iterationen
	 * 
	 * @param iterationen
	 *            Wie oft der Funktion aufgerufen werden soll
	 */
	public void wendeKraft(int iterationen) {
		for (int i = 0; i < iterationen; i++) {
			wendeKraft();
		}
	}

	/**
	 * Setzt die minimalen und maximalen x- Werte für den Darstellungsbereich.
	 * 
	 */
	public void setxrange() {
		Staat first = alleStaaten.get(0);
		double x_min = first.getMittelpunkt().getX() - first.getRadius();
		double x_max = first.getMittelpunkt().getX() + first.getRadius();
		for (Staat staat : alleStaaten) {
			double x = staat.getMittelpunkt().getX() - staat.getRadius();
			if (x < x_min) {
				x_min = x;
			}

			double x_ = staat.getMittelpunkt().getX() + staat.getRadius();
			if (x_ > x_max) {
				x_max = x_;
			}

		}
		this.min_x = x_min;
		this.max_x = x_max;

	}

	/**
	 * Setzt die minimalen und maximalen y-Werte für den Darstellungsbereich.
	 */

	public void setyrange() {
		Staat first = alleStaaten.get(0);
		double y_min = first.getMittelpunkt().getY() - first.getRadius();
		double y_max = first.getMittelpunkt().getY() + first.getRadius();

		for (Staat staat : alleStaaten) {
			double y = staat.getMittelpunkt().getY() - staat.getRadius();
			if (y < y_min) {
				y_min = y;

			}
			double y_ = staat.getMittelpunkt().getY() + staat.getRadius();
			if (y_ > y_max) {
				y_max = y_;
			}
		}
		this.min_y = y_min;
		this.max_y = y_max;

	}

	/**
	 * Setzt die minimalen und maximalen x- und y-Werte für den
	 * Darstellungsbereich, falls die Bedingung x_max-x_min = y_max-y_min nicht
	 * erfüllt wird.
	 * 
	 */

	public void setRange() {
		setyrange();
		setxrange();
		double x_diff = this.max_x - this.min_x;
		double y_diff = this.max_y - this.min_y;
		if (x_diff != y_diff) {
			if (x_diff < y_diff) {
				this.max_x += (y_diff - x_diff) / 2;
				this.min_x -= (y_diff - x_diff) / 2;
			} else {
				this.max_y += (x_diff - y_diff) / 2;
				this.min_y -= (x_diff - y_diff) / 2;
			}
		}
	}

	/**
	 * 
	 * @return min_x
	 */
	public double getMin_x() {
		return min_x;
	}

	/**
	 * 
	 * @return max_x
	 */

	public double getMax_x() {
		return max_x;
	}

	/**
	 * 
	 * @return min_y
	 */
	public double getMin_y() {
		return min_y;
	}

	/**
	 * 
	 * @return max_y
	 */
	public double getMax_y() {
		return max_y;
	}

}
