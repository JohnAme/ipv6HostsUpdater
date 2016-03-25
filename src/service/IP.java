package service;

import java.io.IOException;
import java.net.*;

/**
 * Created by padeoe on 2016/3/18.
 */
public class IP {
    public byte[] address;

    public IP(String ipString) {
        try {
            InetAddress addr = InetAddress.getByName(ipString);
            address = addr.getAddress();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public IP(byte[]address){
        this.address=address;
    }

    public static void main(String[] args) {
        IP ip = new IP("2400:8900::f03c:91ff:feb0:b2d9");
        System.out.println(ip.toString());
        System.out.println(ip.next().toString());
/*        System.out.println(ip.address);
        byte b=0x1F;
        String hex = Integer.toHexString(b & 0xFF);
        System.out.println("b="+hex);
        System.out.println(b);
        System.out.println(Character.digit('f',16));*/
    }

    public IP next() {
        byte[] result =address ;boolean needNext = true;int currentResult;int i;
        for (i = result.length - 1; i >= 0 && needNext; i--) {
            currentResult = result[i] + 1;
            result[i] = (byte) currentResult;
            needNext = currentResult == 0;
        }
        if (i == -1 && needNext) {
            System.out.println("溢出");
            return null;
        }
        return new IP(result);
    }

    public IP previous(){
        byte[] result =address ;boolean needNext = true;int currentResult;int i;
        for (i = result.length - 1; i >= 0 && needNext; i--) {
            currentResult = result[i] - 1;
            result[i] = (byte) currentResult;
            needNext = currentResult == -1;
        }
        if (i == -1 && needNext) {
            System.out.println("溢出");
            return null;
        }
        return new IP(result);
    }

    public String toString(){
        if(address.length==4){
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<address.length-1;i++){
                builder.append(address[i]& 0xFF);
                builder.append('.');
            }
            builder.append(address[address.length-1]& 0xFF);
            return builder.toString();
        }
        if(address.length==16){
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<address.length-3;i+=2){
                int high=(address[i]&0xff)<<8;
                int low=address[i+1]&0xff;
                int tmp=high+low;
                builder.append(Integer.toHexString(tmp));
                builder.append(':');
            }
            int high=(address[address.length-2]&0xff)<<8;
            int low=address[address.length-1]&0xff;
            int tmp=high+low;
            builder.append(Integer.toHexString(tmp));
            return builder.toString();
        }
        return null;

    }

    public boolean isReachable(int openPort, int timeOutMillis) {
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(this.toString(), openPort), timeOutMillis);
                soc.close();
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

}
