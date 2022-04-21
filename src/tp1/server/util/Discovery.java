


package tp1.server.util;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * <p>A class to perform service discovery, based on periodic service contact endpoint
 * announcements over multicast communication.</p>
 *
 * <p>Servers announce their *name* and contact *uri* at regular intervals. The server actively
 * collects received announcements.</p>
 *
 * <p>Service announcements have the following format:</p>
 *
 * <p>&lt;service-name-string&gt;&lt;delimiter-char&gt;&lt;service-uri-string&gt;</p>
 */

@Singleton
public class Discovery {
	private static Logger Log = Logger.getLogger(Discovery.class.getName());

	static {
		// addresses some multicast issues on some TCP/IP stacks
		System.setProperty("java.net.preferIPv4Stack", "true");
		// summarizes the logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
	}


	// The pre-aggreed multicast endpoint assigned to perform discovery.
	static final InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("226.226.226.226", 2266);
	static final int DISCOVERY_PERIOD = 1000;
	static final int DISCOVERY_TIMEOUT = 5000;

	// Used separate the two fields that make up a service announcement.
	private static final String DELIMITER = "\t";

	private InetSocketAddress addr;
	private String serviceName;
	private String serviceURI;

	private static ConcurrentHashMap<String, ArrayList<URI>> knowInterfaces;
	/**
	 * @param  serviceName the name of the service to announce
	 * @param  serviceURI an uri string - representing the contact endpoint of the service being announced
	 */

	private static Discovery instance = null;
	private Discovery(){
		this.instance = null;
		this.knowInterfaces = new ConcurrentHashMap();
	}
	private Discovery(InetSocketAddress addr, String serviceName, String serviceURI) {
		this.addr = addr;
		this.serviceName = serviceName;
		this.serviceURI  = serviceURI;
		this.knowInterfaces = new ConcurrentHashMap<String, ArrayList<URI>>();
		this.instance = null;
	}

	public static Discovery getInstance(){
		if (instance == null)
			instance = new Discovery();

		return instance;
	}

	/**
	 * Continuously announces a service given its name and uri
	 *
	 * @param serviceName the composite service name: <domain:service>
	 * @param serviceURI - the uri of the service
	 */
	public void announce(String serviceName, String serviceURI) {
		Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s\n", DISCOVERY_ADDR, serviceName, serviceURI));

		var pktBytes = String.format("%s%s%s", serviceName, DELIMITER, serviceURI).getBytes();

		DatagramPacket pkt = new DatagramPacket(pktBytes, pktBytes.length, DISCOVERY_ADDR);
		// start thread to send periodic announcements
		new Thread(() -> {
			try (var ds = new DatagramSocket()) {
				for (;;) {
					try {
						Thread.sleep(DISCOVERY_PERIOD);
						ds.send(pkt);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * Listens for the given composite service name, blocks until a minimum number of replies is collected.
	 * @param serviceName - the composite name of the service
	 * @param minRepliesNeeded - the minimum number of replies required.
	 * @return the discovery results as an array
	 */

	public void listener() {
		Log.info(String.format("Starting discovery on multicast group: %s, port: %d\n", DISCOVERY_ADDR.getAddress(), DISCOVERY_ADDR.getPort()));

		final int MAX_DATAGRAM_SIZE = 65536;
		var pkt = new DatagramPacket(new byte[MAX_DATAGRAM_SIZE], MAX_DATAGRAM_SIZE);

		new Thread(() -> {
			try (var ms = new MulticastSocket(DISCOVERY_ADDR.getPort())) {
				joinGroupInAllInterfaces(ms);
				for(;;) {
					try {
						pkt.setLength(MAX_DATAGRAM_SIZE);
						//recebe o pacotinho e mete no pkt criado em cima com o tamanho de MAX_DATAGRAM_SIZE
						ms.receive(pkt);

						var msg = new String(pkt.getData(), 0, pkt.getLength());
						/*cria uma mensagem com o identificador que quem mandou o pacotinho
								pkt.getAddress().getCanonicalHostName()
						com a adress de quem enviou o pacotinho esta adrees é IP
								pkt.getAddress().getHostAddress()
						e a mensagem do pacotinho
								msg
						*/
						System.out.printf( "FROM %s (%s) : %s\n", pkt.getAddress().getCanonicalHostName(),
								pkt.getAddress().getHostAddress(), msg);
						var tokens = msg.split(DELIMITER);

						if (tokens.length == 2) {
							if(knowInterfaces.containsKey(tokens[0])){
								int size = knowInterfaces.get(tokens[0]).size();
								boolean flag = false;
								for(int i = 0; i < size; i++ ){
									String[] h = knowInterfaces.get(tokens[0]).get(i).toString().split("#");
									if(h[0].equals(tokens[1])){
										knowInterfaces.get(tokens[0]).remove(i);
										knowInterfaces.get(tokens[0]).add(new URI(tokens[1] + "#" + System.currentTimeMillis()));
										//knowInterfaces.get(tokens[0]).set(i, new URI(tokens[1] + "#" + System.currentTimeMillis()));
										flag = true;
									}
								}
								if(!flag)
									knowInterfaces.get(tokens[0]).add(new URI(tokens[1] + "#" + System.currentTimeMillis()));
							}
							else{
								ArrayList<URI> aux = new ArrayList<URI>();
								aux.add(new URI(tokens[1] + "#" + System.currentTimeMillis()));
								knowInterfaces.put(tokens[0], aux);
							}
							checkForInterfacesNoneUsed();
							//TODO: to complete by recording the received information from the other node.
						}
					} catch (IOException e) {
						e.printStackTrace();
						try {
							Thread.sleep(DISCOVERY_PERIOD);
						} catch (InterruptedException e1) {
							// do nothing
						}
						Log.finest("Still listening...");
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void checkForInterfacesNoneUsed(){
		int size1 = knowInterfaces.size();
		Iterator<String> it = knowInterfaces.keys().asIterator();
		while(it.hasNext()){
			String aux = it.next();
			for (int f = 0; f < knowInterfaces.get(aux).size(); f++) {
				String[] a = knowInterfaces.get(aux).get(f).toString().split("#");
				long aux2 = Long.parseLong(a[1]);
				if ((System.currentTimeMillis() - aux2) > DISCOVERY_TIMEOUT) {
					knowInterfaces.get(aux).remove(f);
				}
				if (knowInterfaces.get(aux).size() == 0) {
					knowInterfaces.remove(aux);
				}
			}
		}
	}
	/**
	 * Returns the known servers for a service.
	 *
	 * @param  serviceName the name of the service being discovered
	 * @return an array of URI with the service instances discovered.
	 *
	 */
	public static URI[] knownUrisOf(String serviceName) throws URISyntaxException {
		//iterar a knowInterfaces e pronto
		if (!knowInterfaces.containsKey(serviceName)) {
			throw new Error("No such service Name" + knowInterfaces.containsKey(serviceName) + " nome passado para ver se ha: " + serviceName);
		}
		else{
			ArrayList<URI> help = knowInterfaces.get(serviceName);
			if(help == null ) return new URI[0];

			URI[] aux = new URI[help.size()];
			for(int i = 0; i < help.size(); i++){
				//pode haver erro aqui
				aux[i] = new URI(help.get(i).toString().split("#")[0]);
				System.out.println(aux[i]);
			}
			return aux;
		}
	}

	private void joinGroupInAllInterfaces(MulticastSocket ms) throws SocketException {
		Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
		while (ifs.hasMoreElements()) {
			NetworkInterface xface = ifs.nextElement();
			try {
				ms.joinGroup(DISCOVERY_ADDR, xface);
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
	}

	/**
	 * Starts sending service announcements at regular intervals...
	 */
	public void start() {
		announce(serviceName, serviceURI);
		listener();
	}



}
