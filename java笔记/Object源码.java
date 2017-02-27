public class Test5 {
	public static void main(String[] args) {
//		equals方法的源码
//		public boolean equals(Object obj) {
//	        return (this == obj);
//	    }
		//需要重写的equals方法：首先传入的对象不为空，以及其运行时类相等是基本前提，一个条件不同即返回false
		//接着判断所有属性是否相等，不同即返回false，如果其中一个属性是对象的话，就再执行equals方法
		Person p = new Person();
		System.out.println(p.hashCode());
		System.out.println(p);
/*
 *JDK本身自带的hashcode方法是一个native方法，无法查看怎么实现的
	
	 *我们重写的hashcode方法，为什么要重写HashCode的方法？
		重写HashCode方法：当类的实例化对象要要求按照Hash算法存储或者检索（将对象存储的区域按照hash值划分区域，相同的hashcode会放在同一区域内，检索时会先在同一区域寻找对象）时
		（例如使用Set集合存储对象的时候），会重写hashcode方法，但是重写hashcoed方法也有弊端，就是重写hashcode()方法的对象在添加到set集合中之后就不要修改对象中的参数了，
		如果修改了，其存储的位置就会发生变化，remove(obj)时，并不会找到其位置，就没有删除，会导致内存泄漏。
		*
 */
 * ------------------------------------------------------------
/*
 * 对象没有重写toString方法，就使用Object中的toString方法，
 * 返回的是这个对象的运行时类名+@+它的hashcode的十六进制
 * 重写的toString方法返回的是"Person [age=" + age + ", name=" + name + "]"其中的属性是自己添加的
 */
	}
	
}
