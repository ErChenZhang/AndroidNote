#测试
##android下编写单元测试代码的步骤
1. 编写测试类，extnds AndroidTestCase
2. 编写测试方法，修饰符必须是public，直接抛出异常给测试框架 throws Exception
3. 进行断言
4. 清单文件配置
	在application节点配置<uses-library android:name="android.test.runner"/>
	在manifest节点里面配置<instrumentation android:name="android.test.InstrumentationTestRunner"
	android:targetPackage="当前应用程序的包名"></instrumentation>
5.运行测试用例。绿条测试通过，红条测试失败	

