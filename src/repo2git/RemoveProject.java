package repo2git;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveProject {

	private static final String defaultXml = "manifest/r/default.xml";

	private static final String x86Xml = "manifest/r/android-x86.xml";

	public Map<String, String> getRemoveMaps() throws Exception {
		Map<String, String> map = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(x86Xml));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.indexOf("remove-project") > 0) {
				String name = getName(line);
				if (line.indexOf("<!--") > 0) {
					continue;
				}
				map.put(name, name);
			}
		}
		br.close();
		return map;
	}

	private String getName(String txt) {
		String pattern = ".*name=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

}
