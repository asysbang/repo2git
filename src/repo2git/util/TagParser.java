package repo2git.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagParser {

	public static String getName(String txt) {
		String pattern = ".*name=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}
	
	public static String getPath(String txt) {
		String pattern = ".*path=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}
	
	public static String getRevision(String txt) {
		String pattern = ".*revision=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

}
