package gr.uoa.di.entities.helpStructures.iterators;

import java.util.Iterator;

public class _IteratorNonNull<C> implements IteratorNonNull<C>{
	
	Iterator<C> iter;
	C next = null;
	
	_IteratorNonNull(Iterator<C> iter){
		this.iter=iter;
	}

	@Override
	public boolean hasNext() {
		if(next != null)
			return true;
		while(iter.hasNext()) {
			if((next=iter.next())!=null)
				return true;
		}
		return false;
	}

	@Override
	public C next() {
		if(next != null) {
			C out = next;
			next = null;
			return out;
		}
		while(iter.hasNext()) {
			if((next=iter.next())!=null) {
				C out = next;
				next = null;
				return out;
			}
		}
		return null;
	}
	
}
