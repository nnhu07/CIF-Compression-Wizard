import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Region implements Iterable<Pixel> {
	private LinkedList<Pixel> pixels;
	private long redVal;
	private long greenVal;
	private long blueVal;
	private int size;


	Region(Pixel p, Color c) {
		this.pixels = new LinkedList<>();
		this.pixels.add(p);
		this.redVal = c.getRed();
		this.greenVal = c.getGreen();
		this.blueVal = c.getBlue();
		this.size = 1;
	}

	private static Color colorFromVals(long redVal, long greenVal, long blueVal, int size) {

		int r = (int) (redVal / size);
		int g = (int) (greenVal / size);
		int b = (int) (blueVal / size);

		return new Color(r, g, b);

	}

	public static Color avgColor(Region r1, Region r2) {

		long redVal = r1.redVal + r2.redVal;
		long greenVal = r1.greenVal + r2.greenVal;
		long blueVal = r1.blueVal + r2.blueVal;
		int size = r1.size + r2.size;

		return colorFromVals(redVal, greenVal, blueVal, size);

	}

	public void union(Region other) {

		this.pixels.addAll(other.pixels);

		this.redVal += other.redVal;
		this.greenVal += other.greenVal;
		this.blueVal += other.blueVal;

		this.size += other.size;

	}

	public long getDistance(Region other) {
		long r = this.redVal - other.redVal;
		long g = this.greenVal - other.greenVal;
		long b = this.blueVal - other.blueVal;

		return Math.abs(r) + Math.abs(g) + Math.abs(b);
	}

	public int getSize() {
		return this.size;
	}

	public Color getColor() {
		return colorFromVals(redVal, greenVal, blueVal, size);
	}

	public Pixel getRoot() {
		return this.pixels.get(0);
	}

	public Iterator<Pixel> iterator() {
		return pixels.iterator();
	}

}