package GrosseProg;

/**
 * 
 * Klasse zur Darstellung eines Punktes
 * 
 * @author Ketli Cenollari
 *
 */
public class Punkt {
	private double x;
	private double y;

	/**
	 * Konstruktor, erzeugt einen neuen Punkt mit x,- und y- Koordinate
	 * 
	 * @param _x
	 *            x-Koordinate von Punkt
	 * @param _y
	 *            y-Koordinate von Punkt
	 */

	public Punkt(double _x, double _y) {
		this.x = _x;
		this.y = _y;
	}

	/**
	 * Gibt die x-Koordinate zurück
	 * 
	 * @return x-Koordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Gibt die y-Koordinate zurück
	 * 
	 * @return y-Koordinate
	 */
	public double getY() {
		return this.y;
	}
}
