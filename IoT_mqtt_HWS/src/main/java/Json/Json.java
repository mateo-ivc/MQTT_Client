package Json;

public class Json {
	private String temp;
	private String press;
	private String humidity;
	private String x;
	private String y;
	private String z;

	public Json(String temp, String press, String humidity, String x, String y, String z) {
		this.temp = temp;
		this.press = press;
		this.humidity = humidity;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getX() {
		return x;
	}

	public String getY() {
		return y;
	}

	public String getZ() {
		return z;
	}

	public String getTemp() {
		return temp;
	}

	public String getPress() {
		return press;
	}

	public String getHumidity() {
		return humidity;
	}

}
