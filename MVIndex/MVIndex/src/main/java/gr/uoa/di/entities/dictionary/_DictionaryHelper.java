package gr.uoa.di.entities.dictionary;

import java.util.Iterator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

class _DictionaryHelper {
	
	
	static Iterator<Variable> getVarIdIterator() {
		return new Iterator<Variable>() {
			
			int i=Dictionary.firstVarId;

			@Override
			public boolean hasNext() {
				if(i==Dictionary.lastVarId)
					return false;
				else
					return true;
			}

			@Override
			public Variable next() {
				if(i<=Dictionary.lastVarId)
					return new Variable(i++);
				else
					return null;
			}
			
		};
	}

}
