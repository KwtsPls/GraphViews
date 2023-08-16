package interfaceAdapters.java.gr.uoa.di.entities.gspan.infrequentLabelRemoval;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import main.java.gr.uoa.di.entities.gspan.infrequentLabelRemoval.GSPanPreprocessor;
import main.java.gr.uoa.di.entities.gspan.infrequentLabelRemoval._GSPanPreprocessor;

class _PreprocessorDeserializer implements JsonDeserializer<GSPanPreprocessor> {

	@Override
	public GSPanPreprocessor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return context.deserialize(json, _GSPanPreprocessor.class);
	}

}
