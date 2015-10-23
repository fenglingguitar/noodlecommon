package org.fl.noodle.common.util.tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class NetUtils {

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
        LOCAL_ADDRESS = getLocalAddress0();
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
    
    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
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
                                        return address;
                                    }
                                } catch (Throwable e) {
                                }
                            }
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        } catch (Throwable e) {
        }
        return localAddress;
    }   
}