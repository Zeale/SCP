package scp.main.networkencoder;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class NetworkEncoder {
	public static byte[] encodeMessage(String message) {
		byte[] msg = message.getBytes(StandardCharsets.UTF_8),
				size = { (byte) ((msg.length & 0xFF000000) >>> 6), (byte) ((msg.length & 0xFF0000) >>> 4),
						(byte) ((msg.length & 0xFF00) >>> 2), (byte) (msg.length & 0xFF) };
		byte[] res = new byte[4 + msg.length];
		System.arraycopy(size, 0, res, 0, 4);
		System.arraycopy(msg, 0, res, 4, msg.length);
		return res;
	}

	public static String pollMessage(InputStream input) throws IOException {
		DataInputStream dis = new DataInputStream(input);
		byte[] bites = new byte[dis.readInt()];
		dis.read(bites);
		return new String(bites, StandardCharsets.UTF_8);
	}
}
