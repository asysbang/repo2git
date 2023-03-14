package repo2git;

public class MainFun {

	public static void main(String[] args) {
		new ParserFun(VerConfig.Android9_R2.MANIFEST,VerConfig.Android9_R2.OUT_DIR,null).parser();
		
//		new ParserFun(VerConfig.Cm14Android71.MANIFEST,VerConfig.Cm14Android71.OUT_DIR,null).parser();
	}

}
