import java.util.Arrays;
import java.util.Date;

public class test {

	public static void main(String[] args) {

		HS110api client = new HS110api("10.10.2.251", 9999);

		System.out.println(Arrays.toString(client.getEnergy()));
		System.out.println(Arrays.toString(client.getSysInfo()));
		
		System.out.println(client.getTime());
		System.out.println(client.setTimezone(new Date()));
		System.out.println(client.getTime());
	}
}
