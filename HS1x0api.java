import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

public class HS1x0api {
	String ip;
	int port;

	public HS1x0api(String ip) {
		this.ip = ip;
		this.port = 9999;
	}

	public HS1x0api(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String sendCommand(String command) {
		try {

			Socket s = new Socket(this.ip, this.port);

			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();

			byte[] commandByte = encrypt(command);

			os.write(commandByte);
			os.flush();

			byte[] buffer = new byte[4096];

			int r = is.read(buffer);
			if (r == -1) {
				s.close();
				return "ERROR";
			}

			String erg = decrypt(buffer);
			s.close();

			return erg;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}

	public String[] getSysInfo() {
		String temp = sendCommand("{\"system\":{\"get_sysinfo\":{}}}");

		String[] erg = null;

		if (temp.startsWith("{\"system\":")) {
			temp = temp.substring(temp.indexOf("\"get_sysinfo\":{") + 15, temp.indexOf("\",\"next_action")).trim()
					.trim();
			erg = temp.split("\",\"");
		} else {
			String ergError = "ERROR";
			erg = new String[1];
			erg[0] = ergError;
		}

		return erg;
	}

	public String[] getEnergy() {
		String temp = sendCommand("{\"emeter\":{\"get_realtime\":{}}}");

		String[] erg = null;

		if (temp.startsWith("{\"emeter\":{\"get_realtime\":{")) {
			temp = temp.substring(temp.indexOf("\"voltage_mv\""), temp.indexOf("\"err_code\":0")).replace("\"", "")
					.trim();
			erg = temp.split(",");
		} else {
			String ergError = "ERROR";
			erg = new String[1];
			erg[0] = ergError;
		}

		return erg;
	}

	public boolean setTimezone(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);

		String command = "{\"time\":{\"set_timezone\":{\"year\":" + cal.get(Calendar.YEAR) + ",\"month\":"
				+ cal.get(Calendar.MONTH) + ",\"mday\":" + cal.get(Calendar.DATE) + ",\"hour\":"
				+ cal.get(Calendar.HOUR) + ",\"min\":" + cal.get(Calendar.MINUTE) + ",\"sec\":"
				+ cal.get(Calendar.SECOND) + ",\"index\":42}}}";
		String erg = sendCommand(command);
		if (erg.contains("\"err_code\":0")) {
			return true;
		}
		return false;
	}

	public Date getTime() {
		String ergR = sendCommand("{\"time\":{\"get_time\":null}}").trim();
		if (!ergR.contains("\"err_code\":0")) {
			return null;
		}
		ergR = ergR.substring(ergR.indexOf("\"get_time\":{\"") + 12, ergR.indexOf("\"err_code\":0"));
		ergR = ergR.replace("\"", "");

		String[] temp = ergR.split(",");

		try {
			int year = Integer.parseInt(temp[0].substring(5));
			int month = Integer.parseInt(temp[1].substring(6));
			int day = Integer.parseInt(temp[2].substring(5));

			int hour = Integer.parseInt(temp[3].substring(5));
			int min = Integer.parseInt(temp[4].substring(4));
			int sec = Integer.parseInt(temp[5].substring(4));

			Calendar erg = Calendar.getInstance();
			erg.set(year, month, day, hour, min, sec);

			return erg.getTime();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean LED(boolean b) {
		int onoff = -1;

		onoff = b ? 0 : 1;

		String erg = sendCommand("{\"system\":{\"set_led_off\":{\"off\":" + onoff + "}}}");
		if (erg.contains("\"err_code\":0")) {
			return true;
		}
		return false;
	}

	///////////////////////////////////////////////////////////////////////

	private byte[] encrypt(String message) {
		byte[] data = new byte[message.length()];
		for (int i = 0; i < message.length(); i++) {
			data[i] = (byte) message.charAt(i);
		}
		byte[] erg = new byte[data.length + 4];

		erg[3] = (byte) data.length;

		for (int i = 0; i < data.length; i++) {
			erg[i + 4] = data[i];
		}

		byte key = -85;
		for (int i = 4; i < erg.length; i++) {
			erg[i] = (byte) (erg[i] ^ key);
			key = erg[i];
		}

		return erg;
	}

	private String decrypt(byte[] data) {
		if (data == null) {
			return null;
		}
		byte key = -85;
		byte nextKey = 0;
		for (int i = 4; i < data.length; i++) {
			nextKey = data[i];
			data[i] = (byte) (data[i] ^ key);
			key = nextKey;
		}
		StringBuilder erg = new StringBuilder();
		for (int i = 4; i < data.length; i++) {
			erg.append((char) data[i]);
		}
		return erg.toString();
	}

}
