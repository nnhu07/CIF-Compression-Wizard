public class Pixel implements Comparable<Pixel> {
	public final int x;
	public final int y;

	Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int compareTo(Pixel other) {
		return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
	}

}