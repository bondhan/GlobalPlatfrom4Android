/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bondhan.research.common;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

public final class ByteUtils {

    public static String removeAllWhiteSpace(String rawString) {
        return rawString.replaceAll("\\s", "");
    }

    public static String dumpHex(byte byte0) {
        if ((byte0 & 0xff) < 16) {
            return "0" + Integer.toHexString(byte0 & 0xff);
        } else {
            return Integer.toHexString(byte0 & 0xff);
        }
    }

    public static String dumpHex(byte abyte0[], int i, int j) {
        StringBuffer stringbuffer = new StringBuffer();
        for (int k = i; k < abyte0.length && k < i + j; k++) {
            stringbuffer.append(dumpHex(abyte0[k]));
            stringbuffer.append(" ");
        }

        return stringbuffer.toString();
    }

    public static String dumpHex(byte abyte0[]) {
        return dumpHex(abyte0, 0, abyte0.length);
    }

    public static boolean isBytesEqual(byte abyte0[], byte abyte1[]) {
        if (abyte0.length != abyte1.length) {
            return false;
        }
        for (int i = 0; i < abyte0.length; i++) {
            if (abyte0[i] != abyte1[i]) {
                return false;
            }
        }

        return true;
    }

    public static final byte[] intToBytes(int i) {
        int j = (Integer.toHexString(i).length() + 1) / 2;
        byte abyte0[] = new byte[j];
        for (int k = 0; k < j; k++) {
            abyte0[k] = (byte) (i >>> 8 * (j - 1 - k) & 0xff);
        }

        return abyte0;
    }

    public static final int bytesToInt(byte abyte0[], int i, int j) {
        int k = 0;
        for (int l = 0; l < j; l++) {
            k = k << 8 | abyte0[l + i] & 0xff;
        }

        return k;
    }

    public static final byte[] toLengthOctets(int i) {
        byte abyte0[] = null;
        if (i < 128) {
            abyte0 = new byte[1];
            abyte0[0] = (byte) i;
        } else {
            byte abyte1[] = intToBytes(i);
            abyte0 = new byte[1 + abyte1.length];
            System.arraycopy(abyte1, 0, abyte0, 1, abyte1.length);
            abyte0[0] = (byte) (abyte1.length | 0x80);
        }
        return abyte0;
    }

    public static byte[] strToBytes(String s) {
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException("String length % 2 != 0");
        }
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        for (int i = 0; i < s.length(); i += 2) {
            byte byte0 = (byte) (Character.digit(s.charAt(i), 16) & 0xff);
            byte0 <<= 4;
            byte0 |= Character.digit(s.charAt(i + 1), 16);
            bytearrayoutputstream.write(byte0);
        }

        return bytearrayoutputstream.toByteArray();
    }
    private static String numbers = "0123456789abcdef";

    public static byte[] stoh(String s) {
        s = s.replaceAll(" ", "").toLowerCase();
        if (s == null) {
            return null;
        }
        if (s.length() % 2 != 0) {
            throw new RuntimeException("invalid length");
        }
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            int i1 = numbers.indexOf(s.charAt(i));
            if (i1 == -1) {
                throw new RuntimeException("invalid number");
            }
            int i2 = numbers.indexOf(s.charAt(i + 1));
            if (i2 == -1) {
                throw new RuntimeException("invalid number");
            }
            result[i / 2] = (byte) ((i1 << 4) | i2);
        }
        return result;
    }

    public static byte[] stoh_clean(String s) {
        s = removeAllWhiteSpace(s);

        s = s.replaceAll(" ", "").toLowerCase();
        if (s == null) {
            return null;
        }
        if (s.length() % 2 != 0) {
            throw new RuntimeException("invalid length");
        }
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            int i1 = numbers.indexOf(s.charAt(i));
            if (i1 == -1) {
                throw new RuntimeException("invalid number");
            }
            int i2 = numbers.indexOf(s.charAt(i + 1));
            if (i2 == -1) {
                throw new RuntimeException("invalid number");
            }
            result[i / 2] = (byte) ((i1 << 4) | i2);
        }
        return result;
    }

    public static String htos(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            if (i != bytes.length - 1) {
                sb.append(tmp + " ");
            } else {
                sb.append(tmp);
            }

        }
        return sb.toString().toUpperCase();
    }

    public static String htos2(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);

        }
        return sb.toString().toUpperCase();
    }

    public static String htos2(byte[] bytes, int offset, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = offset; i < (length + offset); i++) {
            String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }

        return sb.toString().toUpperCase();
    }

    public static String hexBytesToString(byte[] bytes, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    public static String hexBytesToString(byte[] bytes, int offset, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            String tmp = Integer.toHexString(((int) bytes[offset + i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    public static String toHex(String arg) {
        return String.format("%040x", new BigInteger(arg.getBytes())).toUpperCase();
    }

    public static String stringWithSpace(String arg) {
        String res = "";
        for (int i = 0; i < arg.length(); i++) {
            res += arg.charAt(i);
            if (i % 2 == 1) {
                res += " ";
            }
        }

        return res;
    }
    
    public static byte[] c2b(String s) {
		// TODO Auto-generated method stub
		if(s == null)
			return null;

		if(s.length() %2 != 0)
			throw new RuntimeException();
		byte[] result = new byte[s.length()/2];
		for(int i=0; i< s.length(); i+=2){
			int i1 = numbers.indexOf(s.charAt(i));
			if(i1 == -1)
				throw new RuntimeException("Invalid Number");
			int i2 = numbers.indexOf(s.charAt(i+1));
			if(i2 == -1)
				throw new RuntimeException("Invalid Number");
			result[i/2] = (byte) ((i1<<4) | i2);
		}
		return result;
	}

    //{{{ escapesToChars() method
	/**
	 * Converts "\n" and "\t" escapes in the specified string to
	 * newlines and tabs.
	 * @param str The string
	 * @since jEdit 2.3pre1
	 */
	public static String escapesToChars(String str)
	{
		StringBuilder buf = new StringBuilder();
		for(int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			switch(c)
			{
			case '\\':
				if(i == str.length() - 1)
				{
					buf.append('\\');
					break;
				}
				c = str.charAt(++i);
				switch(c)
				{
				case 'n':
					buf.append('\n');
					break;
				case 't':
					buf.append('\t');
					break;
				default:
					buf.append(c);
					break;
				}
				break;
			default:
				buf.append(c);
			}
		}
		return buf.toString();
	} //}}}

}
