package gr.uoa.di.interfaceAdapters.controllers.dataVault.diskIterator;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

public abstract class DiskIterator<V> implements Closeable {

	protected GsonBuilder builder;
	private Gson gson;
	private RandomAccessFile dataFile;
	private Type valueType;

	
	protected DiskIterator(String fileString, Type valueType, boolean freshStart) throws IOException {
		initialize(fileString, valueType);
		if(freshStart)
			dataFile.setLength(0l);
	}

	public void initialize(String fileString, Type valueType) throws IOException {
		this.builder = new GsonBuilder().enableComplexMapKeySerialization();
		registerTypeAdapters();
		this.gson = getBuilder().create();
		this.valueType = valueType;
		this.dataFile = new RandomAccessFile(fileString, "rw");
	}

	public abstract void registerTypeAdapters();

	public void put(V value) {
		try {
			// Writes Data File
			long objectStart = dataFile.length();
			dataFile.seek(objectStart);			
			String str = gson.toJson(value);
			byte[] bytes = str.getBytes();
			dataFile.writeInt(bytes.length);
			dataFile.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void putAll(Collection<V> values) {
		values.forEach(value->put(value));
	}

	@Override
	public void close() throws IOException {
		dataFile.close();
	}

	public Iterator<V> valueIterator() {
		return new Iterator<V>() {
			long position = 0;
			V nextElement = null;

			@Override
			public boolean hasNext() {
				if (nextElement != null)
					return true;
				else
					nextElement = getNext();
				return nextElement != null;
			}

			@Override
			public V next() {
				if (nextElement != null) {
					V output = nextElement;
					nextElement = null;
					return output;
				}
				return getNext();
			}

			private V getNext() {
				try {
					dataFile.seek(position);
					if(position == dataFile.length())
						return null;
					int len = dataFile.readInt();
					byte[] bytes = new byte[len];
					dataFile.read(bytes, 0, len);
					V output = gson.fromJson(new String(bytes), valueType);
					position += Integer.BYTES+len; 
					return output;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

		};
	}

	public GsonBuilder getBuilder() {
		return builder;
	}
	
}
