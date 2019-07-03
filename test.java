import java.util.Arrays;
import java.util.Date;

public class test {

	public static void main(String[] args) {

		HS1x0api client = new HS1x0api("10.10.2.251", 9999);

		System.out.println(Arrays.toString(client.getEnergy()));
		System.out.println(Arrays.toString(client.getSysInfo()));
		
		System.out.println(client.getTime());
		System.out.println(client.setTimezone(new Date()));
		System.out.println(client.getTime());
		System.out.println(client.LED(false));	
	}
}
