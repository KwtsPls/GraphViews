package gr.uoa.di.entities.graph.regular.helpers;

import java.net.URI;

public class CompactString{

	
	public static String apply(Object object) {
		if(URI.class.isInstance(object)) {			
			String output=object.toString();
			String[] outputs=output.split("/");			
			output=outputs[outputs.length-1];
			outputs=output.split("#");
			output=outputs[outputs.length-1];
			return output;			
		}else if(String.class.isInstance(object)){			
			String literalString=((String)object);
			if(literalString.length()<12) {
				return "'"+literalString+"'";
			}else {
				return "'"+literalString.substring(0, 10)+"..'";
			}
			
		}else {
			
			return object.toString();
		}
		
	}

}
