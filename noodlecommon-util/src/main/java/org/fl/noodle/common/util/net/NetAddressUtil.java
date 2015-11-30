package org.fl.noodle.common.util.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetAddressUtil {

	private final static Logger logger = LoggerFactory.getLogger(NetAddressUtil.class);
	
    public static final String LOCALHOST = "127.0.0.1";

    public static final String ANYHOST = "0.0.0.0";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null 
                && ! ANYHOST.equals(name)
                && ! LOCALHOST.equals(name) 
                && IP_PATTERN.matcher(name).matches());
    }
    
    private static volatile InetAddress LOCAL_ADDRESS = null;
    
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null)
            return LOCAL_ADDRESS;
        LOCAL_ADDRESS = getLocalAddressImpl();
        return LOCAL_ADDRESS;
    }
    
    public static String getLocalIp() {
        if (getLocalAddress() != null)
            return getLocalAddress().getHostAddress();
        return null;
    }
    
    public static String getLocalHostName() {
        if (getLocalAddress() != null)
            return getLocalAddress().getHostName();
        return null;
    }
    
    private static InetAddress getLocalAddressImpl() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
            	logger.info("getLocalAddressImpl -> InetAddress.getLocalHost get the localAddress -> localAddress:{}", localAddress);
                return localAddress;
            }
        } catch (Throwable e) {
        	logger.error("getLocalAddressImpl -> InetAddress.getLocalHost -> Exception:{}", e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                    	logger.info("getLocalAddressImpl -> NetworkInterface get the localAddress -> localAddress:{}", address);
                                        return address;
                                    }
                                } catch (Throwable e) {
                                	logger.error("getLocalAddressImpl -> addresses.nextElement -> Exception:{}", e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                    	logger.error("getLocalAddressImpl -> interfaces.nextElement -> Exception:{}", e);
                    }
                }
            }
        } catch (Throwable e) {
        	logger.error("getLocalAddressImpl -> NetworkInterface.getNetworkInterfaces -> Exception:{}", e);
        }
        
        logger.info("getLocalAddressImpl -> all not get the localAddress -> localAddress:{}", localAddress);
        return localAddress;
    }   
}