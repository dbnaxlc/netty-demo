package com.xzq;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 如果需要原子更新某个类里的某个字段时，需要用到对象的属性修改类型原子类。
 * 1、AtomicIntegerFieldUpdater:原子更新整形字段的更新器
 * 2、AtomicLongFieldUpdater：原子更新长整形字段的更新器
 * 3、AtomicStampedReference ：原子更新带有版本号的引用类型。
 * 该类将整数值与引用关联起来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
 * 要想原子地更新对象的属性需要两步：
 * 第一步，因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法 newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
 * 第二步，更新的对象属性必须使用 public volatile 修饰符。
 * 
 */
public class AtomicIntegerFieldUpdaterTest {

	public static void main(String[] args) {
		AtomicIntegerFieldUpdater<IntClass> updater = AtomicIntegerFieldUpdater.newUpdater(IntClass.class, "age") ;
		IntClass ic = new IntClass();
		ic.setAge(10);
		System.out.println(ic.getAge());
		updater.getAndAdd(ic, 1);
		System.out.println(ic.getAge());
	}

}

class IntClass {
	public volatile int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
