import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class test {

	public static void main(String[] args) {

		HS1x0api client = new HS1x0api("10.10.4.220", 9999);

		System.out.println(Arrays.toString(client.getAPs()));
		
		List<String[]> erg = client.getAPsKeys();
		for (int i = 0; i < erg.size(); i++) {
			System.out.println(Arrays.toString(erg.get(i)));
		}
		
		System.out.println(Arrays.toString(client.getEnergy()));
		
		
		System.out.println(client.getTime());
		System.out.println(client.setTimezone(new Date()));
		System.out.println(client.getTime());
		System.out.println(client.LED(false));	
		
		System.out.println(Arrays.toString(client.getEnergy()));
		

		System.out.println(client.LED(true));	
		System.out.println(client.LED(false));	
		
		System.out.println(Arrays.toString(client.getSysInfo()));
		System.out.println(client.setAlias("ThePlug"));
		System.out.println(Arrays.toString(client.getSysInfo()));
				
		System.out.println(client.reboot());
	}
}
