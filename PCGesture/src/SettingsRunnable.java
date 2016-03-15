import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

public class SettingsRunnable implements Runnable {
	
	private final int port;
	
	public SettingsRunnable(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
            	handleSettings(ss.accept());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}
	
	private void handleSettings(final Socket socket) throws Exception {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					//send settings data to app
			    	Map<String, String> gestureMap = Classifier.getInstance().getCommandMap();
			    	
			    	String data = serializeSettings(gestureMap);
	                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

	                out.print(data);
	                out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(socket);
                }
			}
		};
		
        Utils.startThreadWithName(runnable, "send-settings-output");
	}
	
	private String serializeSettings(Map<String, String> gestureMap) {
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = gestureMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			sb.append(pair.getKey()+"!"+pair.getValue() + "?");
		}
		return sb.toString();
	}

}
