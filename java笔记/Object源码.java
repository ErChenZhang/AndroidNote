public class Test5 {
	public static void main(String[] args) {
//		equals������Դ��
//		public boolean equals(Object obj) {
//	        return (this == obj);
//	    }
		//��Ҫ��д��equals���������ȴ���Ķ���Ϊ�գ��Լ�������ʱ������ǻ���ǰ�ᣬһ��������ͬ������false
		//�����ж����������Ƿ���ȣ���ͬ������false���������һ�������Ƕ���Ļ�������ִ��equals����
		Person p = new Person();
		System.out.println(p.hashCode());
		System.out.println(p);
/*
 *JDK�����Դ���hashcode������һ��native�������޷��鿴��ôʵ�ֵ�
	
	 *������д��hashcode������ΪʲôҪ��дHashCode�ķ�����
		��дHashCode�����������ʵ��������ҪҪ����Hash�㷨�洢���߼�����������洢��������hashֵ����������ͬ��hashcode�����ͬһ�����ڣ�����ʱ������ͬһ����Ѱ�Ҷ���ʱ
		������ʹ��Set���ϴ洢�����ʱ�򣩣�����дhashcode������������дhashcoed����Ҳ�б׶ˣ�������дhashcode()�����Ķ�������ӵ�set������֮��Ͳ�Ҫ�޸Ķ����еĲ����ˣ�
		����޸��ˣ���洢��λ�þͻᷢ���仯��remove(obj)ʱ���������ҵ���λ�ã���û��ɾ�����ᵼ���ڴ�й©��
		*
 */
 * ------------------------------------------------------------
/*
 * ����û����дtoString��������ʹ��Object�е�toString������
 * ���ص���������������ʱ����+@+����hashcode��ʮ������
 * ��д��toString�������ص���"Person [age=" + age + ", name=" + name + "]"���е��������Լ���ӵ�
 */
	}
	
}
