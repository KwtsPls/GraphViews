package gr.uoa.di.entities.dictionary;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


 class _InMemoryDictionary implements Dictionary {

	public BiMap<Object, Integer> entries = HashBiMap.create();
	private int counter = Integer.MIN_VALUE;
	private boolean insertionStage = true;

	_InMemoryDictionary() {
	}

	@Override
	public Object getContantOfId(int id) {
		return entries.inverse().get(id);
	}

	@Override
	public Integer getIdOfConstant(Object constant) throws TooManyConstantExceptiones {
		if(counter== Dictionary.firstVarId) {
			throw new TooManyConstantExceptiones();
		}
			
		return entries.compute(constant,
				(key, value) -> value == null ? (insertionStage ? counter++ : Dictionary.unexaminedConstant) : value);

	}

	@Override
	public void setStage(Stage stage) {
		switch (stage) {
		case INSERTION_STAGE:
			insertionStage = true;
			break;
		case CONTAINMENT_STAGE:
			insertionStage = false;
			break;
		}
	}
	
	static class TooManyConstantExceptiones extends Exception {

		private static final long serialVersionUID = 1L;
		
		TooManyConstantExceptiones() {
			super("Too many constnts");
		}
		
	}

}
