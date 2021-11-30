package hijackit.myjql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Strings {

	public static Object[] toStr(Object... args) {
		ArrayList<String> res = new ArrayList<String>();
		for (Object arg : args) {
			if (arg != null) {
				res.add(arg.toString());
			} else {
				res.add("null");
			}
		}
		return res.toArray(new String[] {});
	}
	
	public static String join(Object[] items, String separator) {
		return join(Arrays.asList(items), separator);
	}
	
	public static String join(Collection<? extends Object> items, String separator) {
		StringBuilder sb = new StringBuilder();
		Iterator<? extends Object> it = items.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			sb.append(item);
			sb.append(it.hasNext() ? separator : "");
		}
		return sb.toString();
	}
	
	public static String nullIfEmpty(String input) {
		if("".equals(input)) {
			return null;
		}
		else return input;
	}
	
}
