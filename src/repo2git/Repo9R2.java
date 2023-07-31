package repo2git;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import repo2git.util.TagParser;

public class Repo9R2 {

	public static void main(String[] args) {
		Repo9R2 repo = new Repo9R2();
		repo.initIO();
		repo.parse();
		repo.closeIO();
	}

	private static final boolean SHOW_LOG = true;

	private static final boolean SHOW_DEBUG = true;

	private static final String BRANCH = "android-9.0.0_r54";

	private static final String manifestXml = "manifest/android9_r2/android-x86-9.0-r2.xml";

	private BufferedWriter mWriterUsed, mWriterUnused, mWriterClone;

	private BufferedReader mReader;

	private String root,name, path, remote, revision, upstream, branch;
	private int usedLines, nameLines, pathLines, remoteLines, revisionLines, upstreamLines;

	private Repo9R2() {

	}

	private void parse() {
		try {
			String line = null;
			while ((line = mReader.readLine()) != null) {
				String txt = line.trim();
				if (txt.startsWith("<project")) {
					mWriterUsed.write(txt);
					mWriterUsed.newLine();
					mWriterUsed.flush();
					usedLines++;
					name = TagParser.getName(txt);
					if (name != null) {
						nameLines++;
					}
					path = TagParser.getPath(txt);
					if (path != null) {
						pathLines++;
					} else {
						// 没有path的默认用name
						path = name;
					}

					remote = TagParser.getRemote(txt);
					if (remote != null) {
						remoteLines++;
						root = "https://scm.osdn.net/gitroot/android-x86/";
						// remote 不是空的 都是x86
					} else {
						root = "https://android.googlesource.com/";
					}
					revision = TagParser.getRevision(txt);
					if (revision != null) {
						revisionLines++;
					}
					upstream = TagParser.getUpstream(txt);
					if (upstream != null) {
						upstreamLines++;
						if (upstream.indexOf("android-9.0.0_r54") >= 0) {
							branch = "android-9.0.0_r54";
						} else if (upstream.indexOf("android-x86-9.0-r2") >= 0) {
							branch = "android-x86-9.0-r2";
						} else {
							branch = upstream;
						}
					}
					String cloneString = joinCloneString();
					mWriterClone.write(cloneString);
					mWriterClone.newLine();
					mWriterClone.flush();
				} else {
					mWriterUnused.write(txt);
					mWriterUnused.newLine();
					mWriterUnused.flush();
				}
			}
			if (SHOW_LOG) {
				System.out.println("---- usedLines : " + usedLines);
				System.out.println("---- nameLines : " + nameLines);
				System.out.println("---- pathLines : " + pathLines);
				System.out.println("---- remoteLines : " + remoteLines);
				System.out.println("---- revisionLines : " + revisionLines);
				System.out.println("---- upstreamLines : " + upstreamLines);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String joinCloneString() {
		StringBuilder sb = new StringBuilder();
		sb.append("git clone ");
		sb.append(root);
		sb.append(name);
		if (!"master".equalsIgnoreCase(branch)) {
			//x86_64-linux-glibc2.11-4.6 不能depth = 1
			sb.append(" --depth 1 ");
		}
		if ("kernel-4.19".equalsIgnoreCase(branch)) {
			//kernel 需要4.9-p的版本
			branch = "kernel-4.9-p";
		}
		sb.append(path);
		sb.append(" -b ");
		sb.append(branch);
		System.out.println(sb.toString());
		return sb.toString();
	}

	private void closeIO() {
		try {
			mReader.close();
			mWriterUsed.close();
			mWriterUnused.close();
			mWriterClone.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initIO() {
		try {
			mReader = new BufferedReader(new FileReader(manifestXml));
			mWriterUsed = new BufferedWriter(new FileWriter("out/android9_r2/used"));
			mWriterUnused = new BufferedWriter(new FileWriter("out/android9_r2/unused"));
			mWriterClone = new BufferedWriter(new FileWriter("out/android9_r2/clone"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
