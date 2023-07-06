package repo2git;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFun {

	private File mManifestFile;
	private File mProjectFile, mUnusedFile;
	private BufferedWriter mWriterProject, mWriterUnused;
	private int totalLine, projectLine, unusedLine;
	private String mDefaultTags;

	public ParserFun(String filename, String outDir, String defaultTags) {
		totalLine = 0;
		projectLine = 0;
		unusedLine = 0;
		mDefaultTags = defaultTags;
		try {
			mManifestFile = new File("manifest/" + filename);
			mProjectFile = new File("out/" + outDir + File.separator + VerConfig.Cm14Android71.PROJECT);
			mWriterProject = new BufferedWriter(new FileWriter(mProjectFile));
			mUnusedFile = new File("out/" + outDir + File.separator + VerConfig.Cm14Android71.UNUSED);
			mWriterUnused = new BufferedWriter(new FileWriter(mUnusedFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parser() {
		try {
			readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readLine() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(mManifestFile));
		String line = null;
		while ((line = br.readLine()) != null) {
			dispatchLine(line);
		}
		br.close();
//		System.out.println("totalLine:" + totalLine + " , projectLine:" + projectLine + " , unusedLine:" + unusedLine);
	}

	public void release() throws Exception {
		mWriterProject.close();
		mWriterUnused.close();
	}

	private void dispatchLine(String line) throws Exception {
		String txt = line.trim();
		// System.out.println("line:" + txt);
		totalLine++;
		String name = null, path = null, upstream = null, remote = null, revision = null;
		if (txt.startsWith("<project")) {
			projectLine++;
			mWriterProject.write(txt);
			mWriterProject.newLine();
			mWriterProject.flush();
			remote = getRemote(txt);
			if (remote != null) {
				// System.out.println("remote : " + remote);
			} else {
				// System.out.println("remote is null : " + txt);
			}
			name = getName(txt);
			if (name != null) {
				// System.out.println("name:" + name);
			} else {
				System.out.println("name is null : " + txt);
			}
			path = getPath(txt);
			if (path != null) {
				// System.out.println("path:" + path);
			} else {
				// System.out.println("path is null : " + txt);
				path = name;
			}
			upstream = getUpstream(txt);
			if (upstream != null) {
				System.out.println("=====upstream:" + upstream);
			} else {
//				 System.out.println("=====upstream is null : " + txt);
			}
			revision = getRevision(txt);
			if (revision != null) {
//				System.out.println("==========revision:" + revision);
			} else {
//				System.out.println("=========revision is null : " + txt);
			}

		}
		boolean cloned = cloneGit(remote, name, path, upstream, revision);
		if (cloned) {
			return;
		}
		unusedLine++;
		mWriterUnused.write(txt);
		mWriterUnused.newLine();
		mWriterUnused.flush();
	}

	private String getName(String txt) {
		String pattern = ".*name=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

	private String getPath(String txt) {
		String pattern = ".*path=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

	// git clone platform/external/svox --depth 1 external/svox
	// cee78199bbfae81f54a40671db47096f5f32cdad
	private String getUpstream(String txt) {
		String pattern = ".*upstream=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

	private String getRemote(String txt) {
		String pattern = ".*remote=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

	private String getRevision(String txt) {
		String pattern = ".*revision=\"(\\S*)\".*";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}

	/**
	 * git clone
	 * 
	 * @param name
	 * @param path
	 * @param upstream !!! 这个可能为null
	 * @param revision
	 */
	private boolean cloneGit(String remote, String name, String path, String upstream, String revision) {
		// git clone https://github.com/LineageOS/android_development.git --depth 1
		// development
		StringBuilder sb = new StringBuilder();
		sb.append("git clone ");
		if (null == name) {
			return false;
		}
		if (remote == null || remote.startsWith("LineageOS")) {
			sb.append("https://android.googlesource.com/");
		} else if (remote.startsWith("x86")) {
			sb.append("https://scm.osdn.net/gitroot/android-x86/");
		} else if (remote.startsWith("aosp")) {
			sb.append("https://android.googlesource.com/");
		} else {
			return false;
		}
		sb.append(name);
		sb.append(" --depth 1 ");
		sb.append(path);
		sb.append(" ");
		if (upstream != null) {
			sb.append(" -b ");
			if (upstream.startsWith("refs/")) {
				int lastIndexOf = upstream.lastIndexOf("/");
				sb.append(upstream.substring(lastIndexOf + 1));
			} else {
				sb.append(upstream);
			}
		} else if (mDefaultTags != null) {
			sb.append(" -b ");
			sb.append(mDefaultTags);
			// upstream is null : <project groups="pdk" name="platform/external/svox"
			// path="external/svox" remote="aosp"
			// revision="cee78199bbfae81f54a40671db47096f5f32cdad"/>
			// git clone platform/external/svox --depth 1 external/svox
			// cee78199bbfae81f54a40671db47096f5f32cdad
			// System.out.println(sb.toString());

		}
		sb.append(" -b ");
		sb.append(revision);
		System.out.println(sb.toString());
		return true;
	}

	private String dealUpstream(String txt) {
		String pattern = ".*name=\"(\\S*)\".*path=\"(\\S*)\".*revision=\"(\\w*)\"";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(txt);
		if (matcher.find()) {
			// System.out.println("name:" + matcher.group(1) +" , path:"+matcher.group(2)+"
			// , revision:"+matcher.group(3));
		} else {
			System.out.println("no revision:" + txt);
		}
		return null;
	}

}
