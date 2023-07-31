# transform repo to git for cloning android x86 code in China

manifest目录
不同版本的manifest文件


out目录
//为什么会有一个8.1的？？
git clone https://android.googlesource.com/platform/system/ca-certificates --depth 1 system/ca-certificates  -b android-8.1.0_r36


//build相关的怎么处理？？
git clone https://scm.osdn.net/gitroot/android-x86/platform/build --depth 1 build  -b cm-14.1-x86 
git clone https://android.googlesource.com/platform/build/blueprint --depth 1 build/blueprint  -b android-7.1.2_r36 
git clone https://android.googlesource.com/platform/build/soong --depth 1 build/soong  -b android-7.1.2_r36  
git clone https://github.com/LineageOS/android_build_kati --depth 1 build/kati  -b cm-14.1



//git.osdn.net/gitroot/android-x86/manifest