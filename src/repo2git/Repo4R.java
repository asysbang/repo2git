package repo2git;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import repo2git.util.TagParser;

public class Repo4R {

	public Repo4R() {
		mRemoveProject = new RemoveProject();
		try {
			mReaderDefault = new BufferedReader(new FileReader(defaultXml));
			mReaderX86 = new BufferedReader(new FileReader(x86Xml));
			mWriterUnusedDefault = new BufferedWriter(new FileWriter("out/r/unused_default"));
			mWriterUsedDefault = new BufferedWriter(new FileWriter("out/r/used_default"));
			
			mWriterUnusedX86 = new BufferedWriter(new FileWriter("out/r/unused_x86"));
			mWriterUsedX86 = new BufferedWriter(new FileWriter("out/r/used_x86"));
			mWriterCloneDefault = new BufferedWriter(new FileWriter("out/r/clone_default"));
			mWriterCloneX86 = new BufferedWriter(new FileWriter("out/r/clone_x86"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final String defaultXml = "manifest/r/default.xml";

	private static final String x86Xml = "manifest/r/android-x86.xml";

	private BufferedWriter mWriterUsedDefault, mWriterUnusedDefault,   mWriterUsedX86, mWriterUnusedX86,mWriterCloneDefault,mWriterCloneX86;
	
	private BufferedReader mReaderDefault ,mReaderX86;

	private RemoveProject mRemoveProject;

	public static void main(String[] args) {
		new Repo4R().parse();
	}

	private void parse() {
		try {
			Map<String, String> removeMaps = mRemoveProject.getRemoveMaps();
			String line = null;
			while ((line = mReaderDefault.readLine()) != null) {
				String txt = line.trim();
				if (txt.startsWith("<project")) {
					String name = TagParser.getName(txt);
					if (name != null) {
						if (removeMaps.containsKey(name)) {
							continue;
						}
						mWriterUsedDefault.write(txt);
						mWriterUsedDefault.newLine();
						mWriterUsedDefault.flush();
						String path = TagParser.getPath(txt);
						if (path != null) {
							String joinCloneString = joinCloneString(name,path);
							mWriterCloneDefault.write(joinCloneString);
							mWriterCloneDefault.newLine();
							mWriterCloneDefault.flush();
						} else {
							throw new RuntimeException("=========RuntimeException=");
						}

					} else {
						throw new RuntimeException("=========RuntimeException=");
					}
				} else {
					mWriterUnusedDefault.write(txt);
					mWriterUnusedDefault.newLine();
					mWriterUnusedDefault.flush();
				}
			}
			mReaderDefault.close();
			while ((line = mReaderX86.readLine()) != null) {
				String txt = line.trim();
				if (txt.startsWith("<project")) {
					mWriterUsedX86.write(txt);
					mWriterUsedX86.newLine();
					mWriterUsedX86.flush();
					String name = TagParser.getName(txt);
					String path = TagParser.getPath(txt);
					String revision = TagParser.getRevision(txt);
					if (name!=null && path != null) {
						String joinCloneString = joinCloneString(name,path,revision);
						mWriterCloneX86.write(joinCloneString);
						mWriterCloneX86.newLine();
						mWriterCloneX86.flush();
					} else {
						throw new RuntimeException("=========RuntimeException=");
					}
					
				}
				else {
					mWriterUnusedX86.write(txt);
					mWriterUnusedX86.newLine();
					mWriterUnusedX86.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String joinCloneString(String name, String path, String revision) {
		StringBuilder sb = new StringBuilder();
		sb.append("git clone https://scm.osdn.net/gitroot/android-x86/");
		sb.append(name);
		sb.append(" --depth 1 ");
		sb.append(path);
		sb.append(" -b ");
		sb.append(revision);
		System.out.println(sb.toString());
		return sb.toString();
	}

	//git clone https://android.googlesource.com/platform/build --depth 1 build/make  -b android-9.0.0_r61 
	private String joinCloneString(String name, String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("git clone https://android.googlesource.com/");
		sb.append(name);
		sb.append(" --depth 1 ");
		sb.append(path);
		sb.append(" -b ");
		sb.append(" android-11.0.0_r48");
		System.out.println(sb.toString());
		return sb.toString();
	}

}
