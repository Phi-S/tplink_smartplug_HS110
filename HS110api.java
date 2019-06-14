import java.util.Calendar;
import java.util.Date;

public class HS110api extends HS110base {

	public HS110api(String ip, int port) {
		super(ip, port);
		// TODO Auto-generated constructor stub
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

	//Date since 2015 allwed. You get an Error if you try to set a time befor 2015. idk why :(
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
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}
}
