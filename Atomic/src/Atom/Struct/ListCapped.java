package Atom.Struct;

import java.util.ArrayList;
import java.util.Collection;

public class ListCapped<T> extends ArrayList<T> {
	protected int max = Integer.MAX_VALUE;
	protected boolean trimFromTheEnd;
	
	public ListCapped(int max) {
		this(max, false);
	}
	
	public ListCapped(int max, boolean trimFromBack) {
		this.max = max;
		this.trimFromTheEnd = trimFromBack;
	}
	
	public ListCapped() {
		trimFromTheEnd = false;
	}
	
	public ListCapped<T> setTrimFromTheEnd(boolean trimFromTheEnd) {
		this.trimFromTheEnd = trimFromTheEnd;
		return this;
	}
	
	public void trim() {
		if (trimFromTheEnd) subList(max - 1, super.size() - 1).clear();
		else subList(0, super.size() - max).clear();
	}
	
	@Override
	public T get(int index) {
		trimCheck();
		return super.get(index);
	}
	
	public boolean trimCheck() {
		
		if (super.size() > max){
			trim();
		}
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean b = super.addAll(index, c);
		trimCheck();
		return b;
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (trimFromTheEnd && super.size() >= max) return false;
		boolean b = super.addAll(c);
		trimCheck();
		return b;
	}
	
	@Override
	public boolean add(T t) {
		if (trimFromTheEnd && super.size() >= max) return false;
		boolean b = super.add(t);
		trimCheck();
		return b;
	}
}
