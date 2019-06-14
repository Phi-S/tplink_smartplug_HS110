import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HS110base {
	byte keyG = -85;

	String ip;
	int port;

	public HS110base(String ip, int port) {
		// TODO Auto-generated constructor stub
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

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

		byte key = keyG;
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
		byte key = keyG;
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
