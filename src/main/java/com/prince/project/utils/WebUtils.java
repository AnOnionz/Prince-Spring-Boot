package com.prince.project.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Random;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.prince.project.model.item;
import com.prince.project.model.TiGia;

public class WebUtils {
	private static final char[] symbols;

	static {
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();
	}

	private static final Random random = new Random();

	public static String prepareRandomString(int len) {
		char[] buf = new char[len];
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}
	public static int getRateUSD() throws Exception {
		URL obj = new URL("http://dongabank.com.vn/exchange/export");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String s = in.readLine();
		in.close();
		s = s.replace("(", "");
		s = s.replace(")", "");
		Gson gs = new Gson();
		try
		{
			TiGia dstg = gs.fromJson(s, TiGia.class);
			for (item list : dstg.items) {
				//USD to VND
				if (list.getType().equalsIgnoreCase("USD")) {
					return Integer.parseInt(list.getMuack());
				}

			}
		}
		catch (IllegalStateException | JsonSyntaxException exception)
		{
		
		
		}
		return 23220;

	}

	public static String toString(User user) {
        StringBuilder sb = new StringBuilder();
 
        sb.append("UserName:").append(user.getUsername());
 
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (GrantedAuthority a : authorities) {
                if (first) {
                    sb.append(a.getAuthority());
                    first = false;
                } else {
                    sb.append(", ").append(a.getAuthority());
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
}
