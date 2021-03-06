/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2013 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.util;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Philipp C. Heckel <philipp.heckel@gmail.com>
 */
public class StringUtil {    
    public static String toCamelCase(String str) {
        StringBuilder sb = new StringBuilder();

        for (String s : str.split("[_-]")) {
        	if (s.length() > 0) {
	            sb.append(Character.toUpperCase(s.charAt(0)));
	
	            if (s.length() > 1) {
	                sb.append(s.substring(1, s.length()).toLowerCase());
	            }
        	}
        }

        return sb.toString();
    }
    
    public static String toHex(byte[] bytes) {
    	if (bytes == null) {
    		return "";
    	}
    	else {
    		// Note: The BigInteger variant is more elegant, but struggles
    		// with extremely long byte arrays (~500 KB)
    		
    		StringBuilder str = new StringBuilder();
    		
    		for(int i = 0; i < bytes.length; i++) {
    			str.append(String.format("%02x", bytes[i]));
    		}
    		
    		return str.toString();
    	}
    }
    
    public static byte[] fromHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
        }
        
        return data;        
    }
	
	public static <T> String join(List<T> objects, String delimiter, StringJoinListener<T> listener) {
		StringBuilder objectsStr = new StringBuilder();
		
		for (int i=0; i<objects.size(); i++) {
			if (listener != null) {
				objectsStr.append(listener.getString(objects.get(i)));
			}
			else {
				objectsStr.append(objects.get(i).toString());
			}
			
			if (i < objects.size()-1) { 
				objectsStr.append(delimiter);
			}			
		}
		
		return objectsStr.toString();
	}   
	
	public static <T> String join(List<T> objects, String delimiter) {
		return join(objects, delimiter, null);
	} 
	
	public static <T> String join(T[] objects, String delimiter, StringJoinListener<T> listener) {
		return join(Arrays.asList(objects), delimiter, listener);
	}   
	
	public static <T> String join(T[] objects, String delimiter) {
		return join(Arrays.asList(objects), delimiter, null);
	}   
	
	public static interface StringJoinListener<T> {
		public String getString(T object);
	}	
}
